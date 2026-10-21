/**
 * Cloudflare Worker Backend Proxy for Cox's Bazar Tourism & Route System
 * 
 * Functions provided:
 * 1. OSRM Driving Route Proxy & Cache (/route/v1/driving/...)
 * 2. OpenStreetMap Tile Proxy & Edge Caching (/tiles/...)
 * 3. Rate limiting and origin header protection
 * 4. Health & Status endpoint (/health)
 * 
 * Deployment Instructions (Cloudflare Dashboard):
 * 1. Log in to your Cloudflare Dashboard (https://dash.cloudflare.com).
 * 2. Navigate to "Workers & Pages" -> "Create Application" -> "Create Worker".
 * 3. Name your worker (e.g. `cox-route-proxy`).
 * 4. Paste this complete file into the Cloudflare Worker code editor.
 * 5. Click "Deploy".
 * 6. Copy your Worker URL (e.g. `https://cox-route-proxy.<your-subdomain>.workers.dev/`).
 * 7. In the Android app, enter this URL in the Admin / Settings routing configuration or BuildConfig!
 */

const OSRM_UPSTREAM = "https://router.project-osrm.org";
const OSM_TILE_UPSTREAM = "https://tile.openstreetmap.org";

// Rate limiting: Simple sliding window memory map per worker instance
const requestCounts = new Map();
const RATE_LIMIT_WINDOW_MS = 60000; // 1 minute
const MAX_REQUESTS_PER_MINUTE = 120; // 120 requests/minute per client IP

export default {
  async fetch(request, env, ctx) {
    // Handle CORS Preflight
    if (request.method === "OPTIONS") {
      return handleCors();
    }

    if (request.method !== "GET") {
      return new Response(JSON.stringify({ error: "Method not allowed" }), {
        status: 405,
        headers: getCorsHeaders({ "Content-Type": "application/json" })
      });
    }

    const url = new URL(request.url);
    const clientIp = request.headers.get("CF-Connecting-IP") || "unknown";

    // 1. Health check endpoint
    if (url.pathname === "/health" || url.pathname === "/") {
      return new Response(JSON.stringify({
        status: "ok",
        service: "Cox's Bazar Route & Tile Proxy",
        upstream_osrm: OSRM_UPSTREAM,
        upstream_tiles: OSM_TILE_UPSTREAM,
        timestamp: new Date().toISOString()
      }), {
        status: 200,
        headers: getCorsHeaders({ "Content-Type": "application/json" })
      });
    }

    // 2. Simple Rate Limiting Check
    const rateLimitExceeded = checkRateLimit(clientIp);
    if (rateLimitExceeded) {
      return new Response(JSON.stringify({
        code: "RateLimitExceeded",
        message: "অনুরোধের সীমা অতিক্রম করেছে। অনুগ্রহ করে ১ মিনিট পর আবার চেষ্টা করুন।"
      }), {
        status: 429,
        headers: getCorsHeaders({
          "Content-Type": "application/json",
          "Retry-After": "60"
        })
      });
    }

    // 3. Routing Proxy: /route/v1/driving/...
    if (url.pathname.startsWith("/route/")) {
      return handleOsrmRouting(request, url, ctx);
    }

    // 4. OpenStreetMap Tile Proxy: /tiles/{z}/{x}/{y}.png
    if (url.pathname.startsWith("/tiles/")) {
      return handleTileProxy(request, url, ctx);
    }

    return new Response(JSON.stringify({
      error: "Not Found",
      message: "Valid endpoints: /route/v1/driving/{coords}, /tiles/{z}/{x}/{y}.png, /health"
    }), {
      status: 404,
      headers: getCorsHeaders({ "Content-Type": "application/json" })
    });
  }
};

/**
 * Handles OSRM route caching & forwarding.
 * Caches successful GeoJSON routes at the Cloudflare edge for 2 hours.
 */
async function handleOsrmRouting(request, url, ctx) {
  const cache = caches.default;
  const cacheKey = new Request(url.toString(), request);
  let response = await cache.match(cacheKey);

  if (response) {
    const cachedResponse = new Response(response.body, response);
    cachedResponse.headers.set("X-Proxy-Cache", "HIT");
    return addCors(cachedResponse);
  }

  // Construct upstream URL
  const upstreamUrl = `${OSRM_UPSTREAM}${url.pathname}${url.search}`;
  
  try {
    const upstreamRequest = new Request(upstreamUrl, {
      method: "GET",
      headers: {
        "User-Agent": "CoxBazarApp-CloudflareProxy/1.0 (contact: aiyubuddin08@gmail.com)",
        "Accept": "application/json"
      }
    });

    const upstreamResponse = await fetch(upstreamRequest);

    if (upstreamResponse.status === 200) {
      const responseBody = await upstreamResponse.text();
      const newResponse = new Response(responseBody, {
        status: 200,
        headers: getCorsHeaders({
          "Content-Type": "application/json",
          "Cache-Control": "public, max-age=7200, s-maxage=7200", // Cache 2 hours
          "X-Proxy-Cache": "MISS"
        })
      });

      // Asynchronously store in Cloudflare Edge Cache
      ctx.waitUntil(cache.put(cacheKey, newResponse.clone()));
      return newResponse;
    } else {
      // Return upstream error gracefully
      const errText = await upstreamResponse.text();
      return new Response(errText, {
        status: upstreamResponse.status,
        headers: getCorsHeaders({ "Content-Type": "application/json" })
      });
    }
  } catch (err) {
    return new Response(JSON.stringify({
      code: "ProxyConnectionError",
      message: "রাউটিং সার্ভারের সাথে সংযোগ করা সম্ভব হয়নি: " + err.message
    }), {
      status: 502,
      headers: getCorsHeaders({ "Content-Type": "application/json" })
    });
  }
}

/**
 * Handles OpenStreetMap tile proxy with edge caching.
 */
async function handleTileProxy(request, url, ctx) {
  const cache = caches.default;
  const tilePath = url.pathname.replace("/tiles", "");
  const upstreamTileUrl = `${OSM_TILE_UPSTREAM}${tilePath}`;

  const cacheKey = new Request(upstreamTileUrl, request);
  let response = await cache.match(cacheKey);

  if (response) {
    const cachedResponse = new Response(response.body, response);
    cachedResponse.headers.set("X-Tile-Cache", "HIT");
    return addCors(cachedResponse);
  }

  try {
    const tileResponse = await fetch(upstreamTileUrl, {
      headers: {
        "User-Agent": "CoxBazarApp-CloudflareTileProxy/1.0"
      }
    });

    if (tileResponse.status === 200) {
      const imageBytes = await tileResponse.arrayBuffer();
      const newResponse = new Response(imageBytes, {
        status: 200,
        headers: getCorsHeaders({
          "Content-Type": "image/png",
          "Cache-Control": "public, max-age=604800, s-maxage=604800", // Cache 7 days
          "X-Tile-Cache": "MISS"
        })
      });

      ctx.waitUntil(cache.put(cacheKey, newResponse.clone()));
      return newResponse;
    }

    return tileResponse;
  } catch (e) {
    return new Response("Tile fetch error", { status: 502 });
  }
}

function checkRateLimit(clientIp) {
  const now = Date.now();
  let clientData = requestCounts.get(clientIp);

  if (!clientData || now - clientData.startTime > RATE_LIMIT_WINDOW_MS) {
    requestCounts.set(clientIp, { count: 1, startTime: now });
    return false;
  }

  clientData.count++;
  if (clientData.count > MAX_REQUESTS_PER_MINUTE) {
    return true;
  }

  return false;
}

function handleCors() {
  return new Response(null, {
    status: 204,
    headers: getCorsHeaders()
  });
}

function getCorsHeaders(extraHeaders = {}) {
  return {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type, Authorization, User-Agent",
    "Access-Control-Max-Age": "86400",
    ...extraHeaders
  };
}

function addCors(response) {
  const headers = new Headers(response.headers);
  headers.set("Access-Control-Allow-Origin", "*");
  return new Response(response.body, {
    status: response.status,
    statusText: response.statusText,
    headers
  });
}
