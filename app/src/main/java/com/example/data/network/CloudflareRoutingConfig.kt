package com.example.data.network

/**
 * Cloudflare Worker & OSRM Routing Configuration.
 *
 * Allows switching between the direct public OSRM routing endpoint
 * and a custom Cloudflare Worker backend proxy.
 * The Cloudflare Worker proxy provides:
 * 1. Edge caching for popular Cox's Bazar routes (reducing public server load).
 * 2. Rate limiting and protection against server throttling.
 * 3. CORS and secure routing provider token encapsulation.
 */
object CloudflareRoutingConfig {

    // Default public OSRM demo endpoint (testing and zero-config development)
    const val DEFAULT_OSRM_BASE_URL = "https://router.project-osrm.org/route/v1/driving/"

    // Optional Cloudflare Worker Proxy URL.
    // When configured (e.g. "https://cox-route-proxy.your-name.workers.dev/route/v1/driving/"),
    // all routing queries are routed through your Cloudflare Edge Worker.
    @Volatile
    var customCloudflareWorkerUrl: String? = null

    /**
     * Resolves the active routing base URL.
     * Prioritizes custom Cloudflare Worker proxy if set, otherwise falls back to default OSRM.
     */
    val activeBaseUrl: String
        get() = customCloudflareWorkerUrl?.takeIf { it.isNotBlank() } ?: DEFAULT_OSRM_BASE_URL

    /**
     * Whether a Cloudflare Worker proxy is currently active.
     */
    val isUsingCloudflareProxy: Boolean
        get() = !customCloudflareWorkerUrl.isNullOrBlank()

    /**
     * Updates the custom Cloudflare Worker proxy URL.
     */
    fun setProxyUrl(url: String?) {
        customCloudflareWorkerUrl = if (url.isNullOrBlank()) null else {
            if (url.endsWith("/")) url else "$url/"
        }
    }
}
