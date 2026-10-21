package com.example.data.datasource

import com.example.data.model.ContentStatus
import com.example.data.model.RouteDestination
import com.example.data.model.StartingPoint

object RouteData {

    /**
     * Standard verified starting points in Cox's Bazar
     * Used when GPS is unavailable or user chooses manual origin.
     */
    val verifiedStartingPoints: List<StartingPoint> = listOf(
        StartingPoint(
            nameBn = "লাবণী পয়েন্ট / মোড় (শহরের কেন্দ্র)",
            latitude = 21.4339,
            longitude = 91.9682
        ),
        StartingPoint(
            nameBn = "সুগন্ধা পয়েন্ট (হোটেল জোন)",
            latitude = 21.4278,
            longitude = 91.9723
        ),
        StartingPoint(
            nameBn = "কলাতলী মোড় (মেরিন ড্রাইভ প্রবেশদ্বার)",
            latitude = 21.4172,
            longitude = 91.9806
        ),
        StartingPoint(
            nameBn = "কক্সবাজার আন্তর্জাতিক বিমানবন্দর",
            latitude = 21.4520,
            longitude = 91.9635
        ),
        StartingPoint(
            nameBn = "কক্সবাজার সেন্ট্রাল বাস টার্মিনাল (ঝিলংজা)",
            latitude = 21.4402,
            longitude = 91.9960
        ),
        StartingPoint(
            nameBn = "কক্সবাজার আইকনিক রেলওয়ে স্টেশন",
            latitude = 21.4530,
            longitude = 91.9840
        ),
        StartingPoint(
            nameBn = "নানিয়ারছড়া মোড় ও বাঁকখালী সংযোগ",
            latitude = 21.4580,
            longitude = 91.9680
        ),
        StartingPoint(
            nameBn = "ঝাউতলা / ডিসি অফিস চত্বর",
            latitude = 21.4420,
            longitude = 91.9790
        ),
        StartingPoint(
            nameBn = "হিমছড়ি পয়েন্ট (মেরিন ড্রাইভ)",
            latitude = 21.3508,
            longitude = 92.0232
        ),
        StartingPoint(
            nameBn = "ইনানী বিচ পয়েন্ট",
            latitude = 21.2185,
            longitude = 92.0526
        )
    )

    /**
     * Verified primary destinations as explicitly requested by user:
     * - মহেশখালী
     * - নানিয়ারছড়া
     * - কক্সবাজার সমুদ্রসৈকত
     * - হিমছড়ি
     * - ইনানী বিচ
     * - হোটেল, রেস্টুরেন্ট, হাসপাতাল ও অন্যান্য সার্ভিস
     */
    val initialDestinations: List<RouteDestination> = listOf(
        // ১. মহেশখালী (Island - requires Ferry)
        RouteDestination(
            id = "dest_maheshkhali",
            nameBn = "মহেশখালী দ্বীপ ও আদিনাথ মন্দির",
            nameEn = "Maheshkhali Island & Adinath Temple",
            category = "দ্বীপ ও নৌপথ",
            address = "মৈনাক পাহাড়ের চূড়া, মহেশখালী উপজেলা, কক্সবাজার",
            latitude = 21.5540,
            longitude = 91.9525,
            requiresFerry = true,
            ferryGhatNameBn = "কস্তুরী ঘাট / ৬ নং জেটি ঘাট (কক্সবাজার শহর)",
            ferryGhatLat = 21.4485,
            ferryGhatLng = 91.9720,
            ferryDurationBn = "স্পিডবোটে ১৫ - ২০ মিনিট (৳১০০ - ৳১২০), কাঠের ট্রলারে ৫০ - ৬০ মিনিট (৳৪০ - ৳৫০)",
            ferryTipsBn = "মহেশখালী একটি দ্বীপ। শহর থেকে প্রথমে কস্তুরী ঘাট বা নানিয়ারছড়া ঘাটে সড়কপথে গিয়ে সেখান থেকে স্পিডবোট বা ট্রলারে মহেশখালী গোরকঘাটা জেটিতে পৌঁছাতে হয়। জেটি থেকে টমটমে ১০ মিনিটে আদিনাথ মন্দির। আবহাওয়া ও জোয়ার-ভাটা দেখে যাত্রা করুন।",
            source = "OpenStreetMap / বিআইডব্লিউটিএ ও স্থানীয় প্রশাসন",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=600&q=80"
        ),

        // ২. নানিয়ারছড়া
        RouteDestination(
            id = "dest_naniarchara",
            nameBn = "নানিয়ারছড়া ঘাট ও সংযোগ পয়েন্ট",
            nameEn = "Naniarchara Ghat & Bridge Area",
            category = "নৌঘাট ও যাতায়াত",
            address = "নানিয়ারছড়া, বাঁকখালী নদী তীর, কক্সবাজার পৌরসভা",
            latitude = 21.4580,
            longitude = 91.9680,
            requiresFerry = false,
            source = "OpenStreetMap / কক্সবাজার পৌরসভা গেজেট",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80"
        ),

        // ৩. কক্সবাজার সমুদ্রসৈকত (লাবণী বিচ)
        RouteDestination(
            id = "dest_laboni_beach",
            nameBn = "কক্সবাজার সমুদ্রসৈকত (লাবণী বিচ)",
            nameEn = "Cox's Bazar Sea Beach (Laboni Point)",
            category = "পর্যটন স্পট",
            address = "লাবণী পয়েন্ট রোড, কক্সবাজার সদর",
            latitude = 21.4339,
            longitude = 91.9682,
            requiresFerry = false,
            source = "OpenStreetMap / বাংলাদেশ পর্যটন কর্পোরেশন",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
        ),

        // ৪. সুগন্ধা বিচ
        RouteDestination(
            id = "dest_sugandha_beach",
            nameBn = "সুগন্ধা বিচ ও সি-ফুড মার্কেট",
            nameEn = "Sugandha Beach & Street Food Point",
            category = "পর্যটন স্পট",
            address = "সুগন্ধা মোড়, হোটেল-মোটেল জোন, কক্সবাজার",
            latitude = 21.4278,
            longitude = 91.9723,
            requiresFerry = false,
            source = "OpenStreetMap / কক্সবাজার পৌরসভা",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=600&q=80"
        ),

        // ৫. কলাতলী বিচ
        RouteDestination(
            id = "dest_kolatoli_beach",
            nameBn = "কলাতলী বিচ ও ডলফিন চত্বর",
            nameEn = "Kolatoli Beach & Dolphin Square",
            category = "পর্যটন স্পট",
            address = "কলাতলী মোড়, মেরিন ড্রাইভ সূচনা, কক্সবাজার",
            latitude = 21.4172,
            longitude = 91.9806,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80"
        ),

        // ৬. হিমছড়ি
        RouteDestination(
            id = "dest_himchari",
            nameBn = "হিমছড়ি জাতীয় উদ্যান ও ঝর্ণা",
            nameEn = "Himchari National Park & Waterfall",
            category = "পর্যটন স্পট",
            address = "মেরিন ড্রাইভ রোড, হিমছড়ি, কক্সবাজার",
            latitude = 21.3508,
            longitude = 92.0232,
            requiresFerry = false,
            source = "OpenStreetMap / বন অধিদপ্তর",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=600&q=80"
        ),

        // ৭. ইনানী বিচ
        RouteDestination(
            id = "dest_inani",
            nameBn = "ইনানী প্রবাল পাথুরে বিচ",
            nameEn = "Inani Coral Stone Beach",
            category = "পর্যটন স্পট",
            address = "মেরিন ড্রাইভ, উখিয়া, কক্সবাজার",
            latitude = 21.2185,
            longitude = 92.0526,
            requiresFerry = false,
            source = "OpenStreetMap / পর্যটন বোর্ড",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
        ),

        // ৮. পাটুয়ারটেক বিচ
        RouteDestination(
            id = "dest_patuartek",
            nameBn = "পাটুয়ারটেক পাথুরে সৈকত",
            nameEn = "Patuartek Beach",
            category = "পর্যটন স্পট",
            address = "মেরিন ড্রাইভ, ইনানীর দক্ষিণে, কক্সবাজার",
            latitude = 21.1450,
            longitude = 92.0830,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=600&q=80"
        ),

        // ৯. দরিয়ানগর গুহা ও ইকোপার্ক
        RouteDestination(
            id = "dest_dariyanagar",
            nameBn = "দরিয়ানগর গুহা ও ইকোপার্ক",
            nameEn = "Dariyanagar Caves & Eco Park",
            category = "পর্যটন স্পট",
            address = "মেরিন ড্রাইভ রোড, দরিয়ানগর",
            latitude = 21.3850,
            longitude = 92.0010,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=600&q=80"
        ),

        // ১০. রামু বৌদ্ধ বিহার
        RouteDestination(
            id = "dest_ramu",
            nameBn = "রামু বৌদ্ধ বিহার ও ১০০ ফুট বুদ্ধ মূর্তি",
            nameEn = "Ramu Buddhist Temples & Reclining Buddha",
            category = "পর্যটন স্পট",
            address = "চৌমুহনী, রামু উপজেলা, কক্সবাজার",
            latitude = 21.4390,
            longitude = 92.0980,
            requiresFerry = false,
            source = "OpenStreetMap / প্রত্নতত্ত্ব অধিদপ্তর",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=600&q=80"
        ),

        // ১১. সোনাদিয়া দ্বীপ (Island - requires Ferry)
        RouteDestination(
            id = "dest_sonadia",
            nameBn = "সোনাদিয়া দ্বীপ (পরিযায়ী পাখি ও লাল কাঁকড়া)",
            nameEn = "Sonadia Offshore Island",
            category = "দ্বীপ ও নৌপথ",
            address = "মহেশখালী উপকূল, কক্সবাজার",
            latitude = 21.4880,
            longitude = 91.8820,
            requiresFerry = true,
            ferryGhatNameBn = "কস্তুরী ঘাট বা গোরকঘাটা জেটি",
            ferryGhatLat = 21.4485,
            ferryGhatLng = 91.9720,
            ferryDurationBn = "নৌকায় ৪০ - ৫০ মিনিট",
            ferryTipsBn = "মহেশখালী ঘাট থেকে ছোট ইঞ্জিনচালিত বোট বা ট্রলারে সোনাদিয়া যেতে হয়। জোয়ারের সময় যাতায়াত সুবিধাজনক।",
            source = "OpenStreetMap / স্থানীয় মাঝি ও বন বিভাগ",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=600&q=80"
        ),

        // ১২. কস্তুরী ঘাট / ৬ নং জেটি ঘাট (মহেশখালী যাওয়ার প্রধান ঘাট)
        RouteDestination(
            id = "dest_kasturi_ghat",
            nameBn = "কস্তুরী ঘাট / ৬ নং স্পিডবোট জেটি",
            nameEn = "Kasturi Ghat (Speedboat Jetty to Maheshkhali)",
            category = "নৌঘাট ও যাতায়াত",
            address = "৬ নং ঘাট, বাঁকখালী নদী সংলগ্ন, কক্সবাজার পৌরসভা",
            latitude = 21.4485,
            longitude = 91.9720,
            requiresFerry = false,
            source = "OpenStreetMap / বিআইডব্লিউটিএ",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৩. হাসপাতাল: কক্সবাজার জেলা সদর হাসপাতাল
        RouteDestination(
            id = "dest_hosp_sadar",
            nameBn = "কক্সবাজার জেলা সদর হাসপাতাল (২৫০ শয্যা)",
            nameEn = "Cox's Bazar District Sadar Hospital",
            category = "হাসপাতাল ও জরুরি",
            address = "হাসপাতাল সড়ক, ঝাউতলা, কক্সবাজার পৌরসভা",
            latitude = 21.4420,
            longitude = 91.9790,
            requiresFerry = false,
            source = "স্বাস্থ্য অধিদপ্তর / OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1586773860418-d37222d8fce3?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৪. হাসপাতাল: ফুয়াদ আল খতীব হাসপাতাল
        RouteDestination(
            id = "dest_hosp_fuad",
            nameBn = "ফুয়াদ আল খতীব বিশেষায়িত হাসপাতাল",
            nameEn = "Fuad Al-Khatib Hospital",
            category = "হাসপাতাল ও জরুরি",
            address = "আলীর জাহান, কলাতলী বাইপাস সড়ক, কক্সবাজার",
            latitude = 21.4385,
            longitude = 91.9835,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1586773860418-d37222d8fce3?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৫. জরুরি: ট্যুরিস্ট পুলিশ ফাঁড়ি
        RouteDestination(
            id = "dest_tourist_police",
            nameBn = "ট্যুরিস্ট পুলিশ হেডকোয়ার্টার্স ও লাবণী ক্যাম্প",
            nameEn = "Tourist Police HQ & Laboni Camp",
            category = "হাসপাতাল ও জরুরি",
            address = "লাবণী পয়েন্ট পুলিশ কন্ট্রোল বক্স, সৈকত সড়ক",
            latitude = 21.4345,
            longitude = 91.9690,
            requiresFerry = false,
            source = "বাংলাদেশ পুলিশ / ট্যুরিস্ট পুলিশ বিভাগ",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৬. হোটেল: সায়মন বিচ রিসোর্ট
        RouteDestination(
            id = "dest_hotel_sayeman",
            nameBn = "সায়মন বিচ রিসোর্ট (Sayeman Beach Resort)",
            nameEn = "Sayeman Beach Resort",
            category = "হোটেল ও রিসোর্ট",
            address = "মেরিন ড্রাইভ রোড, কলাতলী, কক্সবাজার",
            latitude = 21.4165,
            longitude = 91.9815,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৭. হোটেল: সীগাল হোটেল
        RouteDestination(
            id = "dest_hotel_seagull",
            nameBn = "সীগাল হোটেল (Hotel Seagull)",
            nameEn = "Seagull Hotel",
            category = "হোটেল ও রিসোর্ট",
            address = "হোটেল মোটেল জোন, লাবণী পয়েন্ট রোড",
            latitude = 21.4320,
            longitude = 91.9710,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৮. হোটেল: রয়্যাল টিউলিপ সী পার্ল ইনানী
        RouteDestination(
            id = "dest_hotel_royaltulip",
            nameBn = "রয়্যাল টিউলিপ সী পার্ল বিচ রিসোর্ট",
            nameEn = "Royal Tulip Sea Pearl Beach Resort & Spa",
            category = "হোটেল ও রিসোর্ট",
            address = "জালিয়াপালং, ইনানী, উখিয়া, কক্সবাজার",
            latitude = 21.2190,
            longitude = 92.0535,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=600&q=80"
        ),

        // ১৯. রেস্তোরাঁ: ঝাউবন রেস্তোরাঁ
        RouteDestination(
            id = "dest_rest_jhaubon",
            nameBn = "ঝাউবন রেস্তোরাঁ (প্রসিদ্ধ বাংলা ও সি-ফুড)",
            nameEn = "Jhaubon Restaurant",
            category = "রেস্তোরাঁ",
            address = "হোটেল সীগালের সামনে, লাবণী পয়েন্ট রোড",
            latitude = 21.4335,
            longitude = 91.9705,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=600&q=80"
        ),

        // ২০. রেস্তোরাঁ: পৌষী রেস্তোরাঁ
        RouteDestination(
            id = "dest_rest_poushee",
            nameBn = "পৌষী রেস্তোরাঁ (হরেক রকমের ভর্তা ও দেশি খাবার)",
            nameEn = "Poushee Restaurant",
            category = "রেস্তোরাঁ",
            address = "ঝাউতলা মেইন রোড, কক্সবাজার শহর",
            latitude = 21.4380,
            longitude = 91.9765,
            requiresFerry = false,
            source = "OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=600&q=80"
        ),

        // ২১. শপিং: বার্মিজ মার্কেট
        RouteDestination(
            id = "dest_burmese_market",
            nameBn = "ঐতিহাসিক প্রধান বার্মিজ মার্কেট",
            nameEn = "Old Burmese Market",
            category = "শপিং ও মার্কেট",
            address = "টেকপাড়া, বৌদ্ধ মন্দির রোড, কক্সবাজার পৌরসভা",
            latitude = 21.4450,
            longitude = 91.9770,
            requiresFerry = false,
            source = "OpenStreetMap / কক্সবাজার চেম্বার অফ কমার্স",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?auto=format&fit=crop&w=600&q=80"
        ),

        // ২২. যাতায়াত: কক্সবাজার কেন্দ্রীয় বাস টার্মিনাল
        RouteDestination(
            id = "dest_bus_terminal",
            nameBn = "কক্সবাজার কেন্দ্রীয় আন্তঃজেলা বাস টার্মিনাল",
            nameEn = "Cox's Bazar Central Bus Terminal",
            category = "নৌঘাট ও যাতায়াত",
            address = "চট্টগ্রাম-কক্সবাজার মহাসড়ক, ঝিলংজা, কক্সবাজার",
            latitude = 21.4402,
            longitude = 91.9960,
            requiresFerry = false,
            source = "সড়ক ও জনপথ বিভাগ / OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=600&q=80"
        ),

        // ২৩. যাতায়াত: কক্সবাজার আইকনিক রেলওয়ে স্টেশন
        RouteDestination(
            id = "dest_railway_station",
            nameBn = "ঝিনুক আকৃতির আইকনিক রেলওয়ে স্টেশন",
            nameEn = "Cox's Bazar Iconic Railway Station",
            category = "নৌঘাট ও যাতায়াত",
            address = "ঝিলংজা, কক্সবাজার সদর",
            latitude = 21.4530,
            longitude = 91.9840,
            requiresFerry = false,
            source = "বাংলাদেশ রেলওয়ে / OpenStreetMap",
            verificationStatus = "যাচাইকৃত (Verified)",
            imageUrl = "https://images.unsplash.com/photo-1474487548417-781cb71495f3?auto=format&fit=crop&w=600&q=80"
        )
    )
}
