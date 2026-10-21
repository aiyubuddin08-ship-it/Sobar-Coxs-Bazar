# Tourist Information Collection System (সবার কক্সবাজার)

A comprehensive hotel and lodging intelligence system for tourists in Cox's Bazar, integrating authorized Google Maps Platform / Places API and Google Search data via a secure server-side Cloudflare Worker proxy, presented in Bengali with real-time comparison, verified pricing metadata, and full policy compliance.

## User Review & Critical Decisions

> [!IMPORTANT]
> **Key Architecture Decisions & Confirmations:**
> 1. **Google Maps Platform Policy & Map Display**: Google Maps Platform Terms of Service prohibit rendering Google Places data on third-party map tiles (such as OpenStreetMap/Leaflet). To remain 100% compliant while keeping our current crash-proof OpenStreetMap/Leaflet routing system intact, hotel information will be rendered in rich Material 3 cards, detailed sheets, and comparison views with accurate Google attribution and deep-links to Google Maps ("গুগল ম্যাপে দেখুন"), while internal road navigation and distance calculations continue seamlessly using our verified OpenStreetMap/OSRM routing engine.
> 2. **Backend Proxy & Zero Frontend Key Exposure**: As mandated, no Google API keys will be hardcoded in frontend code. A dedicated Cloudflare Worker backend proxy configuration is provided (`CloudflareHotelProxy`) that encapsulates Google Places API (New) / Text Search / Place Details and Google Custom Search, applying rate-limiting, 30-day ephemeral caching, and Bengali localization headers. In the absence of a configured worker URL or billing-active Google key, the system gracefully falls back to the local verified Cox's Bazar hotel database without breaking or showing simulated fake data.
> 3. **Pricing Transparency (Verified vs. Estimated)**: Actual hotel room rates show the verified source, contact number, and verification date. Where live room rates cannot be confirmed from official APIs, estimates are prominently badged as "আনুমানিক মূল্য (Estimated)" with explicit advisories.
> 4. **No Booking or Payment**: In strict accordance with user requirements, all booking and payment flows are completely excluded. Tourists are provided with direct hotel phone hotlines and address copy actions.

---

## 1. Overview & Core Concept

- **What It Does**: Enhances the existing "সবার কক্সবাজার" hotel directory with an automated information collection architecture. Tourists can search, browse, filter, and compare hotels in Cox's Bazar (budget, family-friendly, sea-view, luxury resort, boutique) with verified phone contacts, check-in/out times, room types, verified vs. estimated pricing tags, and Google Search travel advice.
- **Target Audience / Persona**: Domestic and international tourists visiting Cox's Bazar, families planning beach vacations, budget backpackers, and corporate visitors seeking transparent lodging details without hidden commissions or fake data.
- **Key Value**: Eliminates tourist harassment and deceptive pricing by providing transparent, source-attributed, verified hotel information with a side-by-side comparison tool and real road distance from their current GPS location.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Hotel Directory & Category Filtering**:
   - The user opens the Hotels screen (`HotelsScreen.kt`) and sees prominent category chips: "সবগুলো" (All), "বাজেট হোটেল" (Budget), "সমুদ্র দৃশ্য (Sea View)", "পরিবারবান্ধব" (Family Friendly), "প্রিমিয়াম রিসোর্ট" (Premium Luxury), and "ইকো রিসোর্ট" (Eco Resort).
   - Filter chips reflect real criteria (e.g., Sea View checks `hasOceanView` in room types, Budget filters `priceFrom <= 2500`, Premium filters `priceFrom >= 8000`).
2. **Verified vs. Estimated Pricing Badges**:
   - Each hotel card displays a prominent badge:
     - Verified: Green badge `✓ যাচাইকৃত ভাড়া: ৳৩,৫০০ হতে (উৎস: হোটেল কাউন্টার/ওয়েবসাইট • ১০ অক্টো ২০২৪)`
     - Estimated: Amber badge `⚠️ আনুমানিক ভাড়া: ৳২,০০০ - ৳২,৫০০ (সিজনভেদে পরিবর্তনশীল)`
3. **Hotel Comparison Tool (তুলনা করুন)**:
   - Users can tap a "তুলনা যোগ করুন" (Compare) button on up to 3 hotels.
   - A floating comparison bar appears at the bottom with selected hotel thumbnails and a "তুলনা দেখুন" (View Comparison) button.
   - Tapping it opens the `HotelComparisonSheet` presenting a side-by-side comparison matrix:
     - Nightly starting rate & pricing status
     - Proximity to beach (লাবণী, সুগন্ধা, কলাতলী, ইনানী)
     - Ocean view availability
     - Key amenities (সুইমিং পুল, ওয়াইফাই, রেস্তোরাঁ, জেনারেটর ব্যাকআপ, পার্কিং)
     - Guest ratings & review count
     - Check-in / Check-out policy
     - Direct hotline call button
4. **Google Search Travel Advisory Integration**:
   - Within `HotelDetailScreen`, an advisory card titled "ভ্রমণ-পরামর্শ ও নির্দেশিকা (Travel Advice)" displays curated tips for booking in Cox's Bazar (peak season vs. off-season discounts, Friday-Saturday weekend surges) with verifiable source links and Google attribution.

### Visual Identity & Theme
- **Color Tokens**:
  - Primary: Ocean Blue (`#0288D1`)
  - Accent / Price: Sunset Amber (`#F57C00`)
  - Verified Green: Forest Green (`#2E7D32` container `#E8F5E9`)
  - Estimate Warning: Amber Container (`#FFF3E0`, text `#E65100`)
  - Surface & Cards: M3 Elevated cards with subtle 1.dp border (`#E0E0E0`)
- **Typography & Accessibility**:
  - Bengali system fonts with distinct display hierarchy (`headlineSmall` 20.sp for hotel names, `labelMedium` 12.sp for badges).
  - Minimum 48.dp touch targets on all interactive chips, hotline call buttons, and comparison selectors.
  - Edge-to-edge layout with `WindowInsets.safeDrawing` handling.

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Chosen Approach | Rationale & Trade-Offs |
| :--- | :--- | :--- |
| **Map System Integration** | Retain OpenStreetMap/Leaflet for app routing; use rich cards & deep-links for Google Places | Google Maps Platform TOS §3.2.3 prohibits displaying Google Places content on non-Google maps. Displaying Places details in native cards with a "গুগল ম্যাপে দেখুন" button maintains 100% legal compliance while preserving our fast, crash-free Leaflet navigation. |
| **Backend & Cloudflare Worker** | Client-configurable Cloudflare Worker proxy (`CloudflareHotelProxy`) with local repository fallback | Keeps the Google Maps API key protected on the edge server. If the user hasn't deployed the worker yet, the app gracefully falls back to the bundled verified Cox's Bazar hotel database without failing or crashing. |
| **Data Durability & Caching** | Transient in-memory caching with 30-day refresh limits | Google Places Terms strictly forbid caching Place details, photos, or reviews beyond 30 days (only place_id and lat/lng can be stored permanently). In-memory + ephemeral cache guarantees compliance. |
| **Comparison UX** | Bottom Sheet Modal Matrix (2 to 3 hotels) | A modal bottom sheet allows tourists to compare selected hotels without losing their place or scroll position in the hotel catalog. |

---

## 4. Technical Architecture & Data Strategy *(Technical Reference)*

### System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI                              │
│  ┌───────────────────────┐  ┌──────────────────────┐  ┌─────────────┐  │
│  │     HotelsScreen      │  │  HotelDetailScreen   │  │ Comparison  │  │
│  │ (Categories & Search) │  │ (Details & Advisory) │  │ Sheet Modal │  │
│  └───────────┬───────────┘  └──────────┬───────────┘  └──────┬──────┘  │
└──────────────┼─────────────────────────┼─────────────────────┼─────────┘
               │                         │                     │
               ▼                         ▼                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                   CoxViewModel & HotelRepository                       │
│  - comparisonList: StateFlow<List<Hotel>> (max 3 items)                │
│  - hotelCategories: Budget, SeaView, Family, Premium, Boutique         │
│  - selectedHotelSort / filter state                                    │
└──────────────┬─────────────────────────────────────────────────────────┘
               │
               ▼
┌────────────────────────────────────────────────────────────────────────┐
│                     Hotel Information Service Layer                    │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │                   CloudflareHotelProxyService                    │  │
│  │   - Endpoint: /api/hotels/search?q=CoxsBazar                     │  │
│  │   - Endpoint: /api/hotels/details?placeId=...                    │  │
│  │   - Endpoint: /api/travel-tips?query=CoxsBazar                   │  │
│  └──────────────────┬───────────────────────────────────────────────┘  │
│                     │ (Fallback if proxy not configured)               │
│                     ▼                                                  │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │               Verified Local Cox's Bazar Repository              │  │
│  │   - Sayeman, Long Beach, Seagull, Ocean Paradise, Prime Park,    │  │
│  │     Kolatholi budget inns, Inani eco resorts                     │  │
│  │   - Verified pricing, phone hotlines, ocean view status          │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Data Models & Additions

1. **Enhanced `Hotel` Attributes**:
   - `priceVerified`: Boolean (true if verified from hotel counter/official source, false if estimated)
   - `priceSourceBn`: String (e.g., "হোটেল কাউন্টার ও ভেরিফাইড রিসেপশন")
   - `lastVerifiedDate`: String (e.g., "১৫ অক্টোবর ২০২৪")
   - `isSeaView`: Boolean (true if beachfront or ocean-view rooms available)
   - `isFamilyFriendly`: Boolean
   - `isBudget`: Boolean (tariff <= ৳২,৫০০)
   - `googlePlaceId`: String? (for authorized place attribution)
   - `googleMapsUrl`: String? (for official Google Maps link)

2. **`TravelAdvisoryItem` Model**:
   - `id`: String
   - `titleBn`: String
   - `tipBn`: String
   - `sourceName`: String (e.g., "বাংলাদেশ পর্যটন করপোরেশন / গুগল ট্রাভেল")
   - `sourceUrl`: String

3. **Comparison State in `CoxViewModel`**:
   - `val comparisonHotels = MutableStateFlow<List<Hotel>>(emptyList())`
   - `fun toggleHotelComparison(hotel: Hotel)`
   - `fun clearComparison()`

4. **Cloudflare Worker Script (`workers/hotel-places-proxy.js`)**:
   - Ready-to-deploy Cloudflare Worker script handling Google Places API (New) `/v1/places:searchText` and `/v1/places/{placeId}` with CORS, rate limiting, and server-side secret API key injection.

---

## 5. Verification & Testing Strategy

1. **Compilation Check**: Run `compile_applet` after all file creations and edits to ensure zero Kotlin or build failures.
2. **Category & Filter Verification**: Test that switching between "বাজেট", "সমুদ্র দৃশ্য", "পরিবারবান্ধব", and "প্রিমিয়াম" accurately filters the hotel list.
3. **Comparison Engine Verification**: Test selecting 1, 2, and 3 hotels, verifying that exceeding 3 displays a polite toast/snackbar, and inspecting the side-by-side attribute alignment.
4. **Policy & Attribution Check**: Ensure all Google search advice and Places details display legitimate attribution badges and links without violating non-Google map rendering rules.
