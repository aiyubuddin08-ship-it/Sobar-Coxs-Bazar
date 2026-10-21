package com.example.data.datasource

import com.example.data.model.*

object DemoData {

    val spots: List<TouristSpot> = listOf(
        TouristSpot(
            id = "spot_1",
            nameBn = "লাবণী বিচ",
            nameEn = "Laboni Beach",
            category = "Beach",
            rating = 4.8f,
            reviewCount = 1420,
            location = "কক্সবাজার শহর",
            distance = "০ কিমি (শহরের কেন্দ্র)",
            descriptionBn = "লাবণী বিচ কক্সবাজারের প্রধান ও সবচেয়ে জনপ্রিয় সমুদ্র সৈকত। এখানে উত্তাল ঢেউ, সোনালি বালুচর এবং মনকাড়া সূর্যাস্ত উপভোগ করা যায়। সৈকতের ধারে রয়েছে রকমারি ঝিনুক ও বার্মিজ পণ্যের দোকান।",
            descriptionEn = "Laboni Beach is the primary and most iconic beach in Cox's Bazar, famed for its golden sands, roaring waves, and lively atmosphere.",
            openingHours = "সার্বক্ষণিক উন্মুক্ত (সাঁতার: সকাল ৬টা - সন্ধ্যা ৬টা)",
            visitingDuration = "২ - ৪ ঘণ্টা",
            thingsToDo = listOf(
                "উত্তাল সাগরের ঢেউয়ে গা ভাসানো",
                "মনোরম সূর্যাস্ত অবলোকন",
                "সৈকত ধরে পদব্রজে ভ্রমণ",
                "ঝিনুক ও স্থানীয় হস্তশিল্প কেনাকাটা",
                "বিচ চেয়ারে বসে আরাম ও ডাব পান"
            ),
            safetyInfoBn = "লাইফগার্ডের নির্দেশনা ও লাল/সবুজ পতাকা দেখে সাগরে নামুন। ভাটার সময় এবং সন্ধ্যার পর সাগরে নামা সম্পূর্ণ নিষিদ্ধ।",
            travelTipsBn = "বিকালে সূর্যাস্তের অন্তত ১ ঘণ্টা আগে উপস্থিত হলে চমৎকার আলো ও দৃশ্য উপভোগ করতে পারবেন।",
            nearbyHotels = listOf("Seagull Hotel", "Sayeman Beach Resort", "Long Beach Hotel"),
            nearbyRestaurants = listOf("ঝাউবন রেস্তোরাঁ", "পৌঁশী রেস্তোরাঁ", "মারমেইড ক্যাফে"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.4339,
            longitude = 91.9682
        ),
        TouristSpot(
            id = "spot_2",
            nameBn = "সুগন্ধা বিচ",
            nameEn = "Sugandha Beach",
            category = "Beach",
            rating = 4.7f,
            reviewCount = 1180,
            location = "সুগন্ধা পয়েন্ট, কক্সবাজার",
            distance = "১.২ কিমি",
            descriptionBn = "সুগন্ধা পয়েন্ট সুস্বাদু তাজা সি-ফুড এবং স্ট্রিট ফুডের জন্য সবচেয়ে বেশি সুপরিচিত। এখানে সন্ধ্যায় পর্যটকদের মিলনমেলা বসে। লাইভ ফ্রাই রূপচাঁদা, কাঁকড়া ও স্কুইড খাওয়ার অন্যতম সেরা জায়গা।",
            descriptionEn = "Sugandha Beach is known for vibrant street food stalls, live fried seafood, and bustling evening tourist crowds.",
            openingHours = "সার্বক্ষণিক উন্মুক্ত",
            visitingDuration = "২ - ৩ ঘণ্টা",
            thingsToDo = listOf(
                "লাইভ ফ্রাই রূপচাঁদা ও সামুদ্রিক মাছের স্বাদ গ্রহণ",
                "বিচ ফটোগ্রাফি ও ঘোড়ায় চড়া",
                "স্পিড বোট ও ওয়াটার বাইক রাইড",
                "বার্মিজ কসমেটিকস ও স্যুভনির সংগ্রহ"
            ),
            safetyInfoBn = "জোয়ারের উচ্চতা লক্ষ্য রাখুন। শিশুদের সবসময় চোখের সীমানার মধ্যে রাখুন।",
            travelTipsBn = "রাতে সি-ফুড খাওয়ার সময় ফ্রেশ মাছ যাচাই করে দরদাম করে নিন।",
            nearbyHotels = listOf("Ocean Paradise", "Hotel The Cox Today"),
            nearbyRestaurants = listOf("লাইভ ফিশ রেস্টুরেন্ট", "ডিভাইন সি-ফুড"),
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1471922694855-fa59acdb3608?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.4278,
            longitude = 91.9723
        ),
        TouristSpot(
            id = "spot_3",
            nameBn = "কলাতলী বিচ",
            nameEn = "Kolatoli Beach",
            category = "Beach",
            rating = 4.6f,
            reviewCount = 950,
            location = "কলাতলী মোড়, কক্সবাজার",
            distance = "২.৫ কিমি",
            descriptionBn = "হোটেল মোটেল জোনের প্রবেশদ্বারে অবস্থিত কলাতলী বিচ। এখান থেকেই পর্যটকরা সহজেই সাগরে নেমে যেতে পারেন এবং মেরিন ড্রাইভের সূচনা হয় এখান থেকেই।",
            descriptionEn = "Gateway to the Hotel-Motel zone and the beginning of the picturesque Marine Drive.",
            openingHours = "সার্বক্ষণিক উন্মুক্ত",
            visitingDuration = "১ - ২ ঘণ্টা",
            thingsToDo = listOf(
                "ভোরের নিরিবিলি বিচ ওয়াক",
                "মেরিন ড্রাইভ রোড ট্রিপ শুরু করা",
                "সৈকতে বসে মুক্ত বাতাস উপভোগ"
            ),
            safetyInfoBn = "সাঁতারের আগে সি-সেফ লাইফগার্ড পোস্ট দেখে নামুন।",
            travelTipsBn = "কলাতলী মোড়ে সবসময় সিএনজি ও ইজি বাইক পাওয়া যায়।",
            nearbyHotels = listOf("Sayeman Beach Resort", "Long Beach Hotel"),
            nearbyRestaurants = listOf("হান্ডি রেস্তোরাঁ", "কয়লা রেস্টুরেন্ট"),
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.4172,
            longitude = 91.9806
        ),
        TouristSpot(
            id = "spot_4",
            nameBn = "ইনানী বিচ",
            nameEn = "Inani Beach",
            category = "Beach",
            rating = 4.9f,
            reviewCount = 1890,
            location = "উখিয়া, কক্সবাজার",
            distance = "২৬ কিমি (মেরিন ড্রাইভ রোড)",
            descriptionBn = "প্রবাল পাথরে ঘেরা অবিশ্বাস্য সুন্দর এক সৈকত ইনানী বিচ। এখানে নীল জলরাশি প্রবালের গায়ে আছড়ে পড়ে। শহরের কোলাহলমুক্ত শান্ত পরিবেশ ও স্বচ্ছ সাগরের পানি পর্যটকদের মুগ্ধ করে।",
            descriptionEn = "Inani Beach is celebrated for its natural coral stones, crystal-clear blue waters, and pristine, tranquil ambience.",
            openingHours = "সকাল ৬:০০ - সন্ধ্যা ৬:৩০",
            visitingDuration = "৩ - ৫ ঘণ্টা",
            thingsToDo = listOf(
                "প্রবাল পাথরের উপর দাঁড়িয়ে ছবি তোলা",
                "স্বচ্ছ পানিতে পা ভিজিয়ে বসে থাকা",
                "ঝাউবনের ছায়ায় প্রাকৃতিক সৌন্দর্য উপভোগ",
                "লাল কাঁকড়া ও সামুদ্রিক জীব অবলোকন"
            ),
            safetyInfoBn = "প্রবাল পাথরে পা পিছলে যাওয়ার ঝুঁকি থাকে এবং পাথরে ধার থাকে। সাবধানে জুতো পরে হাঁটুন।",
            travelTipsBn = "মেরিন ড্রাইভ ধরে খোলা জিপ বা সিএনজিতে করে যাওয়া এক দারুণ রোমাঞ্চকর অভিজ্ঞতা।",
            nearbyHotels = listOf("Royal Tulip Sea Pearl Beach Resort"),
            nearbyRestaurants = listOf("পাথুরে সি-ফুড ইনানী", "ইনানী ব্রিজ ক্যাফে"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.2185,
            longitude = 92.0526
        ),
        TouristSpot(
            id = "spot_5",
            nameBn = "হিমছড়ি জাতীয় উদ্যান ও ঝর্ণা",
            nameEn = "Himchari National Park",
            category = "Nature",
            rating = 4.7f,
            reviewCount = 1120,
            location = "হিমছড়ি, মেরিন ড্রাইভ",
            distance = "১২ কিমি",
            descriptionBn = "একপাশে সবুজ পাহাড় এবং অন্যপাশে উত্তাল সমুদ্রের অপূর্ব মিতালী হিমছড়িতে। পাহাড়ি ঝর্ণা, সবুজ অরণ্য ও পাহাড়ের চূড়া থেকে দিগন্ত বিস্তৃত সমুদ্রের প্যানোরামিক ভিউ অত্যন্ত আকর্ষণীয়।",
            descriptionEn = "Himchari National Park features lush green hills meeting the turquoise sea, waterfalls, and panoramic view decks.",
            openingHours = "সকাল ৮:০০ - বিকাল ৫:৩০",
            visitingDuration = "২ - ৩ ঘণ্টা",
            thingsToDo = listOf(
                "পাহাড়ের সিঁড়ি বেয়ে ভিউপয়েন্টে ওঠা",
                "হিমছড়ি শীতল পাহাড়ি ঝর্ণা দর্শন",
                "জাতীয় উদ্যানের বন ও বন্যপাখি দেখা",
                "মেরিন ড্রাইভের স্মৃতিচিহ্ন সংগ্রহ"
            ),
            safetyInfoBn = "বৃষ্টির দিনে পাহাড়ের সিঁড়িতে শেওলা জমে পিচ্ছিল হতে পারে। ভালো গ্রিপের জুতা ব্যবহার করুন।",
            travelTipsBn = "ভিউ পয়েন্টে ওঠার আগে পানির বোতল সাথে রাখুন। ৩০০+ সিঁড়ি উঠতে হবে।",
            nearbyHotels = listOf("Mermaid Eco Resort", "Praasad Paradise"),
            nearbyRestaurants = listOf("হিমছড়ি পাহাড়ি ধাবা", "মেরিন ভিউ রেস্টুরেন্ট"),
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.3508,
            longitude = 92.0232
        ),
        TouristSpot(
            id = "spot_6",
            nameBn = "দরিয়ানগর গুহা ও ইকোপার্ক",
            nameEn = "Dariyanagar Caves & Eco Park",
            category = "Adventure",
            rating = 4.5f,
            reviewCount = 680,
            location = "দরিয়ানগর, মেরিন ড্রাইভ",
            distance = "৮ কিমি",
            descriptionBn = "প্রাকৃতিক গুহা ও পাহাড় কেটে তৈরি আকর্ষণীয় অ্যাডভেঞ্চার স্পট। এখানে রয়েছে প্রাচীন সরু গুহা পথ ও পাহাড়ের উপর সুন্দর ওয়াচ টাওয়ার। অ্যাডভেঞ্চারপ্রেমী পর্যটকদের প্রিয় জায়গা।",
            descriptionEn = "Known for thrilling natural caves, trekking trails, and hilltop watchtowers with coastal views.",
            openingHours = "সকাল ৯:০০ - বিকাল ৫:০০",
            visitingDuration = "১.৫ - ২ ঘণ্টা",
            thingsToDo = listOf(
                "অন্ধকার সরু প্রাকৃতিক গুহা এক্সপ্লোর করা",
                "পাহাড়ের চূড়ায় বসে সমুদ্র বাতাস উপভোগ",
                "ইকো ট্রেইল দিয়ে হাইকিং"
            ),
            safetyInfoBn = "গুহায় ঢোকার সময় মোবাইল টর্চ সাথে রাখুন। অতিরিক্ত ভিড়ের সময় সতর্কতা অবলম্বন করুন।",
            travelTipsBn = "শিশুদের নিয়ে গুহায় প্রবেশের সময় হাত শক্ত করে ধরে রাখুন।",
            nearbyHotels = listOf("Sayeman Beach Resort"),
            nearbyRestaurants = listOf("স্টোন ফরেস্ট রেস্টুরেন্ট"),
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.3850,
            longitude = 92.0010
        ),
        TouristSpot(
            id = "spot_7",
            nameBn = "মহেশখালী দ্বীপ ও আদিনাথ মন্দির",
            nameEn = "Maheshkhali Island & Adinath Temple",
            category = "Island",
            rating = 4.8f,
            reviewCount = 1350,
            location = "মহেশখালী, কক্সবাজার",
            distance = "১২ কিমি নৌপথ (স্পিডবোট)",
            descriptionBn = "বাংলাদেশের একমাত্র পাহাড়ি দ্বীপ মহেশখালী। মৈনাক পাহাড়ের চূড়ায় অবস্থিত শতাব্দী প্রাচীন ঐতিহাসিক আদিনাথ শিব মন্দির। এছাড়াও রয়েছে রাখাইন বৌদ্ধ প্যাগোডা ও পান বরজ।",
            descriptionEn = "Bangladesh's only hilly island, home to the ancient Adinath Shiva temple atop Mainak Hill, Buddhist pagodas, and betel leaf plantations.",
            openingHours = "সকাল ৬:০০ - সন্ধ্যা ৬:০০",
            visitingDuration = "৪ - ৬ ঘণ্টা",
            thingsToDo = listOf(
                "কস্তুরী ঘাট থেকে স্পিডবোটে বঙ্গোপসাগরের মোহনা পাড়ি",
                "আদিনাথ মন্দিরে পুজো ও ঐতিহাসিক দর্শন",
                "রাখাইন পাড়া ও বৌদ্ধ মন্দির পরিদর্শন",
                "মহেশখালীর বিখ্যাত মিষ্টি পান ও শুঁটকি সংগ্রহ"
            ),
            safetyInfoBn = "স্পিডবোটে ওঠার সময় অবশ্যই লাইফজ্যাকেট পরবেন। আবহাওয়া খারাপ থাকলে ট্রলার পরিহার করুন।",
            travelTipsBn = "সকাল ১০টার মধ্যে কস্তুরী ঘাট থেকে রওনা দিলে দুপুরে ফিরে আসা সহজ হয়।",
            nearbyHotels = listOf("কক্সবাজার শহরের হোটেলসমূহ"),
            nearbyRestaurants = listOf("দ্বীপ রেস্তোরাঁ মহেশখালী", "স্থানীয় রাখাইন খাবার স্টল"),
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.5540,
            longitude = 91.9525
        ),
        TouristSpot(
            id = "spot_8",
            nameBn = "রামু বৌদ্ধ মন্দির ও বিহার",
            nameEn = "Ramu Buddhist Temples & Monasteries",
            category = "Cultural",
            rating = 4.7f,
            reviewCount = 890,
            location = "রামু উপজেলা",
            distance = "১৬ কিমি",
            descriptionBn = "ঐতিহাসিক রামুর সীমা বিহার ও ১০০ ফুট দীর্ঘ সিংহশয্যা বুদ্ধ মূর্তি পর্যটকদের জন্য এক বিশেষ সাংস্কৃতিক আকর্ষণ। কাঠের তৈরি অপূর্ব কারুকার্যময় শতাব্দী প্রাচীন মন্দির এখানে অবস্থিত।",
            descriptionEn = "Historical Buddhist heritage hub featuring the monumental 100-foot reclining Buddha and exquisite centuries-old monasteries.",
            openingHours = "সকাল ৭:০০ - বিকাল ৫:৩০",
            visitingDuration = "২ - ৩ ঘণ্টা",
            thingsToDo = listOf(
                "বিশাল ১০০ ফুট সিংহশয্যা গৌতম বুদ্ধের মূর্তি দর্শন",
                "রংখ্যং সীমা বিহারের প্রাচীন কাঠের কারুশিল্প দেখা",
                "রামু রাবার বাগান ভ্রমণ"
            ),
            safetyInfoBn = "ধর্মীয় পবিত্রতা রক্ষার্থে মন্দিরে প্রবেশের সময় জুতা খুলে শালীন পোশাক পরিধান করুন।",
            travelTipsBn = "রামু যাওয়ার পথে রাবার বাগান দেখে আসতে ভুলবেন না।",
            nearbyHotels = listOf("কক্সবাজার হোটেল জোন"),
            nearbyRestaurants = listOf("রামু হাইওয়ে রেস্তোরাঁ", "বনশ্রী ধাবা"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.4390,
            longitude = 92.0980
        ),
        TouristSpot(
            id = "spot_9",
            nameBn = "সোনাদিয়া দ্বীপ",
            nameEn = "Sonadia Island",
            category = "Island",
            rating = 4.9f,
            reviewCount = 620,
            location = "মহেশখালী উপকূল",
            distance = "১৮ কিমি নৌপথ",
            descriptionBn = "সোনালি বালুকা এবং পরিযায়ী পাখির স্বর্গরাজ্য সোনাদিয়া। ম্যানগ্রোভ অরণ্য, লাল কাঁকড়ার বিচরণ এবং সম্পূর্ণ নির্জন দ্বীপের অপার্থিব রূপ পরিবেশপ্রেমীদের টানে।",
            descriptionEn = "A pristine offshore island known for golden sands, red crabs, migratory birds, and untouched mangrove forests.",
            openingHours = "দিনের বেলা",
            visitingDuration = "৪ - ৬ ঘণ্টা",
            thingsToDo = listOf(
                "লাখো লাল কাঁকড়ার দল দেখা",
                "শীতকালে অতিথি পাখির কলকাকলি শোনা",
                "ক্যাম্পিং ও নির্জন সমুদ্র সৈকত উপভোগ"
            ),
            safetyInfoBn = "দ্বীপে স্থায়ী হোটেল নেই, দিনের আলো থাকতেই ফিরে আসা নিরাপদ।",
            travelTipsBn = "স্থানীয় মাঝির সাথে কথা বলে ট্রলার ঠিক করুন এবং শুকনো খাবার ও পানি সঙ্গে রাখুন।",
            nearbyHotels = listOf("মহেশখালী গেস্ট হাউস"),
            nearbyRestaurants = listOf("দ্বীপের স্থানীয় জেলে পরিবারের খাবার"),
            imageUrl = "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.5033,
            longitude = 91.8845
        ),
        TouristSpot(
            id = "spot_10",
            nameBn = "কক্সবাজার মেরিন ড্রাইভ",
            nameEn = "Cox's Bazar Marine Drive",
            category = "Adventure",
            rating = 5.0f,
            reviewCount = 2850,
            location = "কলাতলী থেকে টেকনাফ",
            distance = "৮০ কিমি দীর্ঘ পথ",
            descriptionBn = "বিশ্বের অন্যতম দীর্ঘতম মেরিন ড্রাইভ সড়ক। একদিকে সুবিশাল নীল বঙ্গোপসাগরের গর্জন, অন্যদিকে সবুজে ঘেরা সুউচ্চ পাহাড়। এই সড়কে ড্রাইভ করা জীবনের এক অবিস্মরণীয় স্মৃতি।",
            descriptionEn = "One of the world's longest marine driveways, running 80 km between the turquoise Bay of Bengal and lush green coastal hills.",
            openingHours = "সার্বক্ষণিক",
            visitingDuration = "৩ - ৫ ঘণ্টা",
            thingsToDo = listOf(
                "খোলা জিপ (চাঁদের গাড়ি) বা স্কুটারে ড্রাইভ",
                "পথিমধ্যে ঝাউবনের পাশে ছবি তোলা",
                "বিকালে সমুদ্র ও পাহাড়ের পটভূমিতে সূর্যাস্ত দেখা",
                "পথে স্থানীয় ডাবের মিষ্টি পানি পান"
            ),
            safetyInfoBn = "গতিসীমা মেনে চলুন। রাতে নির্জন অংশে সাবধানে ড্রাইভ করুন।",
            travelTipsBn = "বিকালে কলাতলী থেকে টেকনাফের দিকে যাত্রা শুরু করলে বাঁয়ে পাহাড় ডানে সাগর চমৎকার দেখায়।",
            nearbyHotels = listOf("Royal Tulip Sea Pearl", "Mermaid Beach Resort"),
            nearbyRestaurants = listOf("মেরিন ক্যাফে", "পাথুরে ইনানী"),
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80"
            ),
            latitude = 21.3200,
            longitude = 92.0300
        )
    )

    val hotels: List<Hotel> = listOf(
        Hotel(
            id = "hotel_1",
            nameBn = "সায়মন বিচ রিসোর্ট",
            nameEn = "Sayeman Beach Resort",
            rating = 4.8f,
            reviewCount = 890,
            location = "মেরিন ড্রাইভ রোড, কলাতলী",
            address = "Marine Drive Road, Kolatoli, Cox's Bazar",
            priceFrom = 8500,
            hotelType = "Luxury Resort",
            amenities = listOf("সরাসরি সি-ভিউ", "ইনফিনিটি সুইমিং পুল", "ফ্রি ব্রেকফাস্ট", "জিম ও স্পা", "ফ্রি ওয়াইফাই", "এয়ার কন্ডিশন"),
            descriptionBn = "১৯৬৪ সালে প্রতিষ্ঠিত ঐতিহাসিক সায়মন আধুনিক স্থাপত্যে কলাতলী বিচে সমুদ্রের কোল ঘেঁষে অবস্থিত। সৈকতমুখী ইনফিনিটি পুল এবং বিশ্বমানের আতিথেয়তার জন্য এটি শীর্ষস্থানীয়।",
            roomTypes = listOf(
                RoomType("Infinity Sea View Deluxe", 12500, "২ জন প্রাপ্তবয়স্ক", "১টি কিং সাইজ বেড", true),
                RoomType("Super Deluxe Hill View", 8500, "২ জন প্রাপ্তবয়স্ক", "২টি কুইন বেড", false),
                RoomType("Ocean Panorama Suite", 19500, "৪ জন পরিবার", "২টি কিং বেড + লিভিং", true)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801755699901",
            latitude = 21.4165,
            longitude = 91.9810,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80"
            ),
            priceVerified = true,
            priceSourceBn = "সায়মন অফিশিয়াল ফ্রন্টডেস্ক ও বুকিং কাউন্টার",
            lastVerifiedDate = "১৫ অক্টোবর ২০২৪",
            isSeaView = true,
            isFamilyFriendly = true,
            isBudget = false,
            googlePlaceId = "ChIJ-wYd0sL4-TARK92R17qQ4t0",
            googleMapsUrl = "https://maps.google.com/?cid=12599298374928"
        ),
        Hotel(
            id = "hotel_2",
            nameBn = "ওশান প্যারাডাইস হোটেল অ্যান্ড রিসোর্ট",
            nameEn = "Ocean Paradise Hotel & Resort",
            rating = 4.7f,
            reviewCount = 1150,
            location = "কলাতলী রোড, কক্সবাজার",
            address = "290/291 Kolatoli Road, Cox's Bazar",
            priceFrom = 7200,
            hotelType = "Luxury Resort",
            amenities = listOf("সুইমিং পুল", "রুফটপ রেস্তোরাঁ", "কিডস জোন", "ফ্রি বুফে ব্রেকফাস্ট", "স্পা সেন্টার"),
            descriptionBn = "বিশাল জায়গা জুড়ে নির্মিত পাঁচ তারকা সমমানের রিসোর্ট। রয়েছে জাকুজি, সুইমিং পুল এবং ছাদের ওপর চমৎকার ডাইনিং সুবিধা।",
            roomTypes = listOf(
                RoomType("Deluxe Sea View", 9500, "২ জন", "১ কিং বেড", true),
                RoomType("Superior Twin", 7200, "২ জন", "২ টুইন বেড", false)
            ),
            checkInTime = "১৩:০০",
            checkOutTime = "১১:৩০",
            phone = "+8801938846761",
            latitude = 21.4215,
            longitude = 91.9790,
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_3",
            nameBn = "রয়্যাল টিউলিপ সি পার্ল বিচ রিসোর্ট",
            nameEn = "Royal Tulip Sea Pearl Beach Resort",
            rating = 4.9f,
            reviewCount = 1620,
            location = "ইনানী বিচ, মেরিন ড্রাইভ",
            address = "Jaliapalong, Inani, Ukhia, Cox's Bazar",
            priceFrom = 11000,
            hotelType = "Beachfront Luxury Resort",
            amenities = listOf("প্রাইভেট বিচ", "ওয়াটার পার্ক", "২টি সুইমিং পুল", "আন্তর্জাতিক ডাইনিং", "টেনিস কোর্ট"),
            descriptionBn = "ইনানীর শান্ত প্রবাল সৈকতে অবস্থিত বাংলাদেশের অন্যতম সেরা আন্তর্জাতিক পাঁচ তারকা মেগা রিসোর্ট। এতে রয়েছে নিজস্ব প্রাইভেট বিচ ও বিশাল ওয়াটার পার্ক।",
            roomTypes = listOf(
                RoomType("Superior Ocean View", 14000, "২ জন", "১ কিং বেড", true),
                RoomType("Executive Suite", 22000, "৪ জন", "২ মাস্টার বেড", true)
            ),
            checkInTime = "১৫:০০",
            checkOutTime = "১২:০০",
            phone = "+8801844016001",
            latitude = 21.2190,
            longitude = 92.0530,
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_4",
            nameBn = "লং বিচ হোটেল",
            nameEn = "Long Beach Hotel",
            rating = 4.6f,
            reviewCount = 760,
            location = "কলাতলী রোড",
            address = "14 Kalatoli Road, Cox's Bazar",
            priceFrom = 6500,
            hotelType = "Business & Leisure",
            amenities = listOf("সুইমিং পুল", "বিলিয়ার্ড ও গেমস রুম", "রেস্টুরেন্ট", "ফ্রি ওয়াইফাই", "কনফারেন্স হল"),
            descriptionBn = "কলাতলী মোড়ের সন্নিকটে অবস্থিত আধুনিক সুবিশাল হোটেল। পরিবার ও বন্ধুদের সাথে আরামদায়ক অবকাশ যাপনের জন্য অত্যন্ত জনপ্রিয়।",
            roomTypes = listOf(
                RoomType("Premier Deluxe", 7500, "২ জন", "১ কিং বেড", false),
                RoomType("Junior Suite", 11000, "৩ জন", "১ কিং + ১ সোফা", true)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801730338905",
            latitude = 21.4230,
            longitude = 91.9775,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_5",
            nameBn = "সিগাল হোটেল",
            nameEn = "Seagull Hotel",
            rating = 4.6f,
            reviewCount = 980,
            location = "লাবণী পয়েন্ট",
            address = "Hotel Motel Zone, Laboni, Cox's Bazar",
            priceFrom = 5800,
            hotelType = "Beachfront",
            amenities = listOf("সরাসরি সৈকত অ্যাক্সেস", "বাগান সংলগ্ন পুল", "বারবিকিউ কর্নার", "জিম"),
            descriptionBn = "লাবণী পয়েন্টের সবচেয়ে সুপরিচিত পাঁচ তারকা হোটেল। এর সুদৃশ্য সবুজ বাগান ও খোলা পুল সাগরের বাতাসের সাথে দারুণ মিশে যায়।",
            roomTypes = listOf(
                RoomType("Deluxe Sea Facing", 7200, "২ জন", "১ কিং বেড", true),
                RoomType("Regular Hill View", 5800, "২ জন", "টুইন বেড", false)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801766666530",
            latitude = 21.4330,
            longitude = 91.9705,
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_6",
            nameBn = "হোটেল দ্য কক্স টুডে",
            nameEn = "Hotel The Cox Today",
            rating = 4.5f,
            reviewCount = 820,
            location = "প্লট-৭, রোড-২, কলাতলী",
            address = "Plot 7, Road 2, Kolatoli, Cox's Bazar",
            priceFrom = 6200,
            hotelType = "Luxury Hotel",
            amenities = listOf("ইনডোর পুল", "ক্যাফে ও রেস্টুরেন্ট", "স্পা", "ফ্রি পিকআপ"),
            descriptionBn = "অভিজাত ফার্নিচার এবং আধুনিক আরামদায়ক সুযোগ-সুবিধা সম্বলিত চমৎকার হোটেল। কলাতলী বিচ থেকে হাঁটা দূরত্বে অবস্থিত।",
            roomTypes = listOf(
                RoomType("Crown Deluxe", 7000, "২ জন", "১ কিং বেড", false),
                RoomType("Executive Sea View", 9800, "২ জন", "১ কিং বেড", true)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801755598446",
            latitude = 21.4220,
            longitude = 91.9760,
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_7",
            nameBn = "মারমেইড বিচ রিসোর্ট",
            nameEn = "Mermaid Beach Resort",
            rating = 4.9f,
            reviewCount = 1430,
            location = "প্যাঁচার দ্বীপ, মেরিন ড্রাইভ",
            address = "Pechar Dwip, Marine Drive Road, Cox's Bazar",
            priceFrom = 14000,
            hotelType = "Eco Boutique Resort",
            amenities = listOf("প্রাইভেট কটেজ", "অর্গানিক ফুড", "প্রাইভেট বিচ", "ইয়োগা ডেক", "কায়াকিং"),
            descriptionBn = "প্রকৃতি ও নান্দনিকতার মেলবন্ধনে তৈরি ইকো বিচ রিসোর্ট। বাঁশ ও মাটির ছোঁয়ায় তৈরি বিলাসবহুল কটেজগুলো আপনাকে দেবে নির্মল প্রশান্তি।",
            roomTypes = listOf(
                RoomType("Beach Front Villa", 18500, "২ জন", "১ কিং ক্যানোপি বেড", true),
                RoomType("Pool Villa Suite", 26000, "২ জন", "প্রাইভেট প্লাঞ্জ পুল সহ", true)
            ),
            checkInTime = "১৫:০০",
            checkOutTime = "১২:০০",
            phone = "+8801841416468",
            latitude = 21.3100,
            longitude = 92.0320,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_8",
            nameBn = "প্রাসাদ প্যারাডাইস",
            nameEn = "Praasad Paradise",
            rating = 4.4f,
            reviewCount = 540,
            location = "প্লট-৯, নিউ বিচ রোড",
            address = "Plot 9, New Beach Road, Cox's Bazar",
            priceFrom = 4500,
            hotelType = "Family Resort",
            amenities = listOf("সুইমিং পুল", "গার্ডেন লন", "ফ্রি ব্রেকফাস্ট", "বাচ্চাদের খেলার মাঠ"),
            descriptionBn = "সবুজ গাছপালা ঘেরা শান্ত পরিবেশে মনোরম রিসোর্ট। পরিবারের জন্য সুলভ ও নিরাপদ আবাসন।",
            roomTypes = listOf(
                RoomType("Deluxe Double", 5200, "২ জন", "১ ডাবল বেড", false),
                RoomType("Family Suite", 8000, "৪ জন", "২ ডাবল বেড", false)
            ),
            checkInTime = "১৩:৩০",
            checkOutTime = "১১:০০",
            phone = "+8801556347711",
            latitude = 21.4280,
            longitude = 91.9740,
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_9",
            nameBn = "উইন্ডি টেরেস বুটিক হোটেল",
            nameEn = "Windy Terrace Boutique Hotel",
            rating = 4.5f,
            reviewCount = 610,
            location = "কলাতলী মোড়",
            address = "Plot 40, Block C, Kolatoli, Cox's Bazar",
            priceFrom = 4800,
            hotelType = "Boutique Hotel",
            amenities = listOf("রুফটপ ক্যাফে", "ফ্রি ওয়াইফাই", "এয়ার কন্ডিশন", "২৪ ঘণ্টা রুম সার্ভিস"),
            descriptionBn = "রুচিশীল ইন্টেরিয়র এবং চমৎকার রুফটপ ডাইনিংয়ের জন্য পরিচিত সাশ্রয়ী বুটিক হোটেল।",
            roomTypes = listOf(
                RoomType("Deluxe Room", 4800, "২ জন", "১ কুইন বেড", false),
                RoomType("Executive Room", 6000, "২ জন", "১ কিং বেড", true)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801777755866",
            latitude = 21.4200,
            longitude = 91.9780,
            imageUrl = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_10",
            nameBn = "গ্রেস কক্স স্মার্ট হোটেল",
            nameEn = "Grace Cox Smart Hotel",
            rating = 4.6f,
            reviewCount = 490,
            location = "কলাতলী রোড",
            address = "Plot 34, Kolatoli Main Road, Cox's Bazar",
            priceFrom = 5200,
            hotelType = "Smart Boutique",
            amenities = listOf("স্মার্ট রুম অটোমেশন", "ফ্রি ব্রেকফাস্ট", "রুফটপ ইনফিনিটি ভিউ", "ফ্রি হাই স্পিড ওয়াইফাই"),
            descriptionBn = "আধুনিক প্রযুক্তি ও কী-লেস স্মার্ট রুম সমৃদ্ধ নতুন প্রজন্মের ট্রেন্ডি হোটেল।",
            roomTypes = listOf(
                RoomType("Smart Deluxe", 5200, "২ জন", "১ কিং বেড", false),
                RoomType("Ocean Panorama", 7800, "২ জন", "১ কিং বেড", true)
            ),
            checkInTime = "১৪:০০",
            checkOutTime = "১২:০০",
            phone = "+8801700707788",
            latitude = 21.4210,
            longitude = 91.9770,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Hotel(
            id = "hotel_11",
            nameBn = "হোটেল বিচ পার্ক",
            nameEn = "Hotel Beach Park",
            rating = 4.3f,
            reviewCount = 380,
            location = "কলাতলী রোড, কক্সবাজার",
            address = "Main Kolatoli Road, Cox's Bazar",
            priceFrom = 1800,
            hotelType = "Budget Hotel",
            amenities = listOf("ফ্রি ওয়াইফাই", "জেনারেটর ব্যাকআপ", "এসি/নন-এসি রুম", "২৪ ঘণ্টা সিকিউরিটি"),
            descriptionBn = "কলাতলী বিচের কাছে স্বল্প খরচে পরিবার ও বন্ধুদের নিয়ে থাকার মতো নির্ভরযোগ্য বাজেট হোটেল। পরিষ্কার-পরিচ্ছন্ন পরিবেশ ও সাশ্রয়ী প্যাকেজ।",
            roomTypes = listOf(
                RoomType("স্ট্যান্ডার্ড নন-এসি ডাবল", 1800, "২ জন", "১ ডাবল বেড", false),
                RoomType("ডিলাক্স এসি কাপল রুম", 2400, "২ জন", "১ কুইন বেড", false)
            ),
            checkInTime = "১২:৩০",
            checkOutTime = "১১:০০",
            phone = "+8801819654321",
            latitude = 21.4230,
            longitude = 91.9785,
            imageUrl = "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=800&q=80"
            ),
            priceVerified = true,
            priceSourceBn = "হোটেল ফ্রন্টডেস্ক ক্যাশ কাউন্টার",
            lastVerifiedDate = "১৫ অক্টোবর ২০২৪",
            isSeaView = false,
            isFamilyFriendly = true,
            isBudget = true,
            googlePlaceId = "ChIJ_beach_park_cox",
            googleMapsUrl = "https://maps.google.com/?q=Hotel+Beach+Park+Coxs+Bazar"
        ),
        Hotel(
            id = "hotel_12",
            nameBn = "আল-হেরা গেস্ট হাউস",
            nameEn = "Al-Hera Guest House",
            rating = 4.1f,
            reviewCount = 210,
            location = "সুগন্ধা পয়েন্ট মোড়",
            address = "Sugandha Point, Cox's Bazar",
            priceFrom = 1500,
            hotelType = "Budget Guest House",
            amenities = listOf("ওয়াইফাই", "২৪ ঘণ্টা পানির ব্যবস্থা", "রুম সার্ভিস"),
            descriptionBn = "সুগন্ধা সৈকতের কাছে সাশ্রয়ী থাকার সেরা ঠিকানা। শিক্ষার্থী ও বাজেট ট্রাভেলারদের জন্য বিশেষ উপযোগী।",
            roomTypes = listOf(
                RoomType("ইকোনমি ডাবল রুম", 1500, "২ জন", "১ ডাবল বেড", false),
                RoomType("ফ্যামিলি ফোর-বেড", 2500, "৪ জন", "২ ডাবল বেড", false)
            ),
            checkInTime = "১২:০০",
            checkOutTime = "১১:০০",
            phone = "+8801815123456",
            latitude = 21.4260,
            longitude = 91.9765,
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80"
            ),
            priceVerified = false,
            priceSourceBn = "অনুমিত ট্যারিফ (সিজনভেদে পরিবর্তনশীল)",
            lastVerifiedDate = "১০ অক্টোবর ২০২৪",
            isSeaView = false,
            isFamilyFriendly = true,
            isBudget = true,
            googlePlaceId = "ChIJ_al_hera_cox",
            googleMapsUrl = "https://maps.google.com/?q=Al-Hera+Guest+House+Coxs+Bazar"
        )
    )

    val restaurants: List<Restaurant> = listOf(
        Restaurant(
            id = "rest_1",
            nameBn = "ঝাউবন রেস্তোরাঁ",
            nameEn = "Jhaubon Restaurant",
            category = "Bangla",
            rating = 4.7f,
            reviewCount = 1450,
            priceRange = "৳৳",
            location = "মেইন রোড, কক্সবাজার",
            address = "Main Road, Near Laboni Point, Cox's Bazar",
            openingHours = "সকাল ৭:০০ - রাত ১১:৩০",
            phone = "+8801819612345",
            descriptionBn = "কক্সবাজারের খাঁটি বাংলা খাবার ও ঐতিহ্যবাহী নানা পদের সামুদ্রিক মাছের ভর্তার জন্য বহু বছর ধরে সবার সেরা পছন্দ। রূপচাঁদা ফ্রাই এবং লইট্টা ফ্রাই দারুণ জনপ্রিয়।",
            popularDishes = listOf(
                DishItem("রূপচাঁদা ফ্রাই (তাজা)", "Fresh Rupchanda Fry", 550, true),
                DishItem("লইট্টা শুঁটকি ভুনা", "Loitta Dry Fish Bhuna", 180, true),
                DishItem("সামুদ্রিক কোরাল মাছ দোপেঁয়াজা", "Coral Fish Dopeyaza", 420),
                DishItem("চিংড়ি ভর্তা", "Spicy Shrimp Mash", 120)
            ),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_2",
            nameBn = "পৌঁশী রেস্তোরাঁ",
            nameEn = "Poushee Restaurant",
            category = "Bangla",
            rating = 4.8f,
            reviewCount = 1920,
            priceRange = "৳৳",
            location = "ঝাউতলা রোড",
            address = "Hotel Sea Palace Lane, Jhawtala, Cox's Bazar",
            openingHours = "সকাল ৬:৩০ - রাত ১২:০০",
            phone = "+8801817024567",
            descriptionBn = "ভর্তা প্রেমীদের স্বর্গ! এখানে ৩০টিরও বেশি পদের মাছ ও সবজির দেশীয় ভর্তা এবং গরম ভাতের সাথে রূপচাঁদা ভাজা পরিবেশন করা হয়।",
            popularDishes = listOf(
                DishItem("স্পেশাল রূপচাঁদা ফ্রাই", "Special Fried Pomfret", 500, true),
                DishItem("কালিবাউশ মাছের পেটি", "Kalibaush Fish Curry", 280),
                DishItem("মিক্সড ভর্তা থালি (৫ পদ)", "Traditional Bharta Thali", 250),
                DishItem("চিংড়ি কারি", "Prawn Coconut Curry", 380)
            ),
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_3",
            nameBn = "মারমেইড ক্যাফে",
            nameEn = "Mermaid Cafe",
            category = "Cafe",
            rating = 4.8f,
            reviewCount = 1380,
            priceRange = "৳৳৳",
            location = "সুগন্ধা বিচ সংলগ্ন",
            address = "Sugandha Beach Point, Cox's Bazar",
            openingHours = "সকাল ৮:০০ - রাত ১২:০০",
            phone = "+8801841416464",
            descriptionBn = "খোলামেলা সমুদ্র সৈকতের পাশে কাঠের নান্দনিক সাজসজ্জায় কফি, পিৎজা, ফ্রেশ জুস ও সি-ফুড পরিবেশন করে এই আইকনিক ক্যাফে।",
            popularDishes = listOf(
                DishItem("উড ফায়ারড সি-ফুড পিৎজা", "Woodfired Seafood Pizza", 850, true),
                DishItem("গ্রিলড লবস্টার", "Grilled Garlic Butter Lobster", 1600, true),
                DishItem("মারমেইড স্পেশাল কোল্ড কফি", "Mermaid Signature Cold Coffee", 280),
                DishItem("ক্রিস্পি ক্যালামারি (স্কুইড)", "Crispy Fried Calamari", 480)
            ),
            imageUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_4",
            nameBn = "ডিভাইন সি-ফুড রেস্তোরাঁ",
            nameEn = "Devine Seafood Restaurant",
            category = "Seafood",
            rating = 4.6f,
            reviewCount = 820,
            priceRange = "৳৳",
            location = "কলাতলী সী-ইন পয়েন্ট",
            address = "Sea In Point, Kolatoli, Cox's Bazar",
            openingHours = "বেলা ১১:০০ - রাত ১১:০০",
            phone = "+8801711234890",
            descriptionBn = "সরাসরি জেলেদের কাছ থেকে সংগৃহীত রূপচাঁদা, লাল কোরাল, কাঁকড়া ও গলদা চিংড়ির লাইভ গ্রিল এবং সি-ফুড প্লাটারের জন্য বিখ্যাত।",
            popularDishes = listOf(
                DishItem("লাইভ বারবিকিউ কোরাল (কেজি)", "BBQ Live Coral Fish", 900, true),
                DishItem("জাফলং ক্র্যাব মাসালা", "Spicy Crab Masala", 450),
                DishItem("জাম্পো প্রন গ্রিল", "Jumbo Prawn Grill", 750)
            ),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_5",
            nameBn = "স্টোন ফরেস্ট রেস্টুরেন্ট",
            nameEn = "Stone Forest Restaurant",
            category = "Chinese",
            rating = 4.5f,
            reviewCount = 590,
            priceRange = "৳৳",
            location = "মেরিন ড্রাইভ রোড",
            address = "Marine Drive Road, Cox's Bazar",
            openingHours = "বেলা ১২:০০ - রাত ১০:৩০",
            phone = "+8801822334455",
            descriptionBn = "পাহাড় ও ঝাউবনের ছায়ায় অবস্থিত থাই, চাইনিজ ও সামুদ্রিক খাবারের জনপ্রিয় প্রতিষ্ঠান।",
            popularDishes = listOf(
                DishItem("থাই সিজলিং প্রন", "Thai Sizzling Prawn", 520),
                DishItem("সি-ফুড কর্ন স্যুপ", "Seafood Corn Soup", 260)
            ),
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_6",
            nameBn = "হান্ডি রেস্তোরাঁ",
            nameEn = "Handi Restaurant",
            category = "Indian",
            rating = 4.6f,
            reviewCount = 910,
            priceRange = "৳৳",
            location = "হোটেল মোটেল জোন, কলাতলী",
            address = "Hotel Motel Zone, Kolatoli, Cox's Bazar",
            openingHours = "দুপুর ১২:০০ - রাত ১১:৩০",
            phone = "+8801819129876",
            descriptionBn = "হায়দ্রাবাদি দম বিরিয়ানি, হান্ডি মাটন এবং সুস্বাদু বাটার চিকেন ও নানা পদের তন্দুর রুটির জন্য বিশ্বস্ত নাম।",
            popularDishes = listOf(
                DishItem("হান্ডি মাটন দম বিরিয়ানি", "Handi Mutton Biryani", 480, true),
                DishItem("বাটার চিকেন উইথ গার্লিক নান", "Butter Chicken & Garlic Naan", 390)
            ),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_7",
            nameBn = "তরঙ্গ রেস্তোরাঁ",
            nameEn = "Taranga Restaurant",
            category = "Bangla",
            rating = 4.4f,
            reviewCount = 470,
            priceRange = "৳",
            location = "লাবণী বিচ রোড",
            address = "Laboni Beach, Cox's Bazar",
            openingHours = "সকাল ৭:০০ - রাত ১১:০০",
            phone = "+8801712998811",
            descriptionBn = "সুলভ মূল্যে পরিবারের জন্য দুপুরের তৃপ্তিদায়ক ভাত, ডাল ও সাগরের তাজা মাছের লোভনীয় প্যাকেজ মিল।",
            popularDishes = listOf(
                DishItem("বাজেট লাঞ্চ থালি", "Standard Fish Thali", 180),
                DishItem("লইট্টা মাছের ঝোল", "Loitta Fish Curry", 150)
            ),
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_8",
            nameBn = "লাইভ ফিশ রেস্টুরেন্ট",
            nameEn = "Live Fish Restaurant",
            category = "Seafood",
            rating = 4.7f,
            reviewCount = 1100,
            priceRange = "৳৳",
            location = "সুগন্ধা বিচ মার্কেট",
            address = "Sugandha Point, Cox's Bazar",
            openingHours = "বিকাল ৪:০০ - রাত ১:০০",
            phone = "+8801833556677",
            descriptionBn = "পর্যটকদের পছন্দের মাছ বেছে নিয়ে চোখের সামনে তাজা ভেজে গরম গরম পরিবেশন করা হয়।",
            popularDishes = listOf(
                DishItem("লাইভ ফ্রাইড রুপচাঁদা", "Live Fried Pomfret", 500, true),
                DishItem("স্কুইড ফ্রাই স্পাইসি", "Spicy Squid Fry", 350)
            ),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_9",
            nameBn = "বীচ প্ল্যানেট ক্যাফে",
            nameEn = "Beach Planet Cafe",
            category = "Fast Food",
            rating = 4.5f,
            reviewCount = 380,
            priceRange = "৳৳",
            location = "কলাতলী রোড",
            address = "Kolatoli Beach Road, Cox's Bazar",
            openingHours = "বেলা ১২:০০ - রাত ১২:০০",
            phone = "+8801911442233",
            descriptionBn = "বার্গার, ক্রিস্পি ফ্রাইড চিকেন, মিল্কশেক এবং ওয়াফেলের জন্য তরুণ পর্যটকদের অন্যতম আড্ডাখানা।",
            popularDishes = listOf(
                DishItem("স্মোকি বিফ বার্গার", "Smoky Beef Burger", 320),
                DishItem("চকলেট ওরিও শেক", "Oreo Thickshake", 220)
            ),
            imageUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1554118811-1e0d58224f24?auto=format&fit=crop&w=800&q=80"
            )
        ),
        Restaurant(
            id = "rest_10",
            nameBn = "কয়লা রেস্টুরেন্ট",
            nameEn = "Koyla Restaurant",
            category = "Local Food",
            rating = 4.6f,
            reviewCount = 650,
            priceRange = "৳৳",
            location = "সায়মন মোড়, কলাতলী",
            address = "Sayeman Circle, Kolatoli, Cox's Bazar",
            openingHours = "বিকাল ৫:০০ - রাত ১২:৩০",
            phone = "+8801824778899",
            descriptionBn = "কয়লার আগুনে পোড়ানো বিভিন্ন কাবাব, বটি কাবার ও মাটন শিক কাবাবের চমৎকার সুবাস ও স্বাদ।",
            popularDishes = listOf(
                DishItem("চিকেন রেশমি কাবাব", "Chicken Reshmi Kebab", 340),
                DishItem("স্পেশাল রুমালি রুটি", "Rumali Roti (2 pcs)", 60)
            ),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80",
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
            )
        )
    )

    val shops: List<Shop> = listOf(
        Shop(
            id = "shop_1",
            nameBn = "বার্মিজ মার্কেট",
            nameEn = "Burmese Market",
            category = "Burmese Market",
            location = "টেকপাড়া, কক্সবাজার শহর",
            descriptionBn = "কক্সবাজারের সবচেয়ে ঐতিহ্যবাহী শপিং মার্কেট। বার্মিজ আচার, চন্দন সাবান, লুঙ্গি, শামুক-ঝিনুকের গহনা ও হস্তশিল্পের বিশাল সমাহার।",
            openingHours = "সকাল ৯:০০ - রাত ১০:০০",
            phone = "+8801818112233",
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_2",
            nameBn = "কক্সবাজার শুঁটকি মহাল",
            nameEn = "Cox's Bazar Dry Fish Market",
            category = "Dry Fish",
            location = "নাজিরারটেক ও সুগন্ধা মোড়",
            descriptionBn = "এশিয়া মহাদেশের অন্যতম বৃহৎ শুঁটকি প্রক্রিয়াজাতকরণ এলাকা থেকে সরাসরি আসা খাঁটি রূপচাঁদা, লইট্টা, ছুরি ও চিংড়ি শুঁটকি।",
            openingHours = "সকাল ৮:০০ - রাত ১১:০০",
            phone = "+8801823998877",
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_3",
            nameBn = "শুকতারা মুক্তা ও পার্ল জুয়েলার্স",
            nameEn = "Shuktara Pearl & Jewelry",
            category = "Local Products",
            location = "লাবণী বিচ মার্কেট",
            descriptionBn = "প্রাকৃতিক সাগরের আসল মুক্তার মালা, আংটি, কানের দুল ও ঝিনুকের তৈরি ঐতিহ্যবাহী স্যুভনির।",
            openingHours = "সকাল ১০:০০ - রাত ১০:০০",
            phone = "+8801711883344",
            imageUrl = "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_4",
            nameBn = "গ্রীন লিফ হস্তশিল্প ও তাঁত",
            nameEn = "Green Leaf Handicrafts",
            category = "Souvenirs",
            location = "ঝাউতলা রোড",
            descriptionBn = "নারিকেলের ছোবড়া, কাঠ ও ঝিনুকের তৈরি ঘর সাজানোর শোপিস ও স্থানীয় তাঁত সামগ্রী।",
            openingHours = "সকাল ১০:০০ - রাত ৯:৩০",
            phone = "+8801912334455",
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_5",
            nameBn = "হিমছড়ি স্পেশাল বার্মিজ আচার কর্নার",
            nameEn = "Himchari Achar & Souvenir",
            category = "Local Products",
            location = "হিমছড়ি বাজার",
            descriptionBn = "তেঁতুল, বরই, জলপাই, চালতা ও কাঁচা আমের শতাধিক পদের সুস্বাদু মুখরোচক আচার।",
            openingHours = "সকাল ৯:০০ - সন্ধ্যা ৭:০০",
            phone = "+8801831223344",
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_6",
            nameBn = "মেরিন ড্রাইভ শেল মার্কেট",
            nameEn = "Marine Drive Shell Market",
            category = "Souvenirs",
            location = "ইনানী রোড, মেরিন ড্রাইভ",
            descriptionBn = "সমুদ্রের বিরল ঝিনুক, শঙ্খ এবং হস্তনির্মিত ফটোফ্রেম ও দেয়ালঘড়ি।",
            openingHours = "সকাল ১০:০০ - রাত ৮:০০",
            phone = "+8801722556677",
            imageUrl = "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_7",
            nameBn = "সী বিচ ফ্যাশন বাজার",
            nameEn = "Sea Beach Fashion Bazaar",
            category = "Clothing",
            location = "কলাতলী রোড",
            descriptionBn = "রঙিন বিচ শর্টস, ফ্লোরাল শার্ট, সানগ্লাস, বিচ হ্যাট ও আরামদায়ক পোশাকের সমাহার।",
            openingHours = "সকাল ৯:০০ - রাত ১১:০০",
            phone = "+8801933445566",
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80"
        ),
        Shop(
            id = "shop_8",
            nameBn = "রাখাইন ঐতিহ্যবাহী তাঁত শিল্পালয়",
            nameEn = "Rakhine Traditional Handloom",
            category = "Clothing",
            location = "রাখাইন পাড়া, মহেশখালী",
            descriptionBn = "রাখাইন নারীদের নিজস্ব হাতে বোনা সূক্ষ্ম কারুকাজের থামি, চাদর ও সুতি মাফলার।",
            openingHours = "সকাল ৯:৩০ - বিকাল ৫:৩০",
            phone = "+8801844556611",
            imageUrl = "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?auto=format&fit=crop&w=800&q=80"
        )
    )

    val transports: List<TransportService> = listOf(
        TransportService(
            id = "trans_1",
            nameBn = "গ্রীন লাইন পরিবহন (স্লিপার কোচ)",
            serviceType = "Bus",
            phone = "+8801730060000",
            location = "কলাতলী বাস টার্মিনাল",
            descriptionBn = "ঢাকা - চট্টগ্রাম - কক্সবাজার রুটে প্রিমিয়াম বিলাসবহুল ডাবল ডেকার ও স্লিপার কোচ সেবা।",
            estimatedFare = "৳১৫০০ - ৳২৫০০",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_2",
            nameBn = "সোহাগ এলিট স্লিপার বাস",
            serviceType = "Bus",
            phone = "+8801711598765",
            location = "ঝাউতলা কাউন্টার",
            descriptionBn = "আরামদায়ক বিজনেস ক্লাস স্লিপার বার্থ এবং ওয়াইফাই সুবিধা সহ সরাসরি যাত্রা।",
            estimatedFare = "৳১৬০০ - ৳২৪০০",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_3",
            nameBn = "বীচ সিএনজি ও অটো সার্ভিস",
            serviceType = "CNG",
            phone = "+8801815001122",
            location = "কলাতলী / সুগন্ধা মোড়",
            descriptionBn = "শহরের মধ্যে যেকোনো বিচে স্বল্প সময়ে যাতায়াতের সবচেয়ে জনপ্রিয় বাহন।",
            estimatedFare = "৳৫০ - ৳১৫০",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_4",
            nameBn = "কক্সবাজার এয়ারপোর্ট শাটল ট্যাক্সি",
            serviceType = "Taxi",
            phone = "+8801720889900",
            location = "কক্সবাজার বিমানবন্দর",
            descriptionBn = "বিমানবন্দর থেকে সরাসরি হোটেল জোনে নিরাপদে পৌঁছানোর নির্ভরযোগ্য ট্যাক্সি সার্ভিস।",
            estimatedFare = "৳৩০০ - ৳৬০০",
            imageUrl = "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_5",
            nameBn = "ট্যুরিস্ট চাঁদের গাড়ি (খোলা জিপ)",
            serviceType = "Tourist vehicle",
            phone = "+8801822445566",
            location = "কলাতলী জিপ স্ট্যান্ড",
            descriptionBn = "মেরিন ড্রাইভ ধরে হিমছড়ি ও ইনানী বিচ ভ্রমণের জন্য রোমাঞ্চকর খোলা ছাদের জিপ গাড়ি।",
            estimatedFare = "৳২৫০০ - ৳৪৫০০ (পুরো দিন)",
            imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_6",
            nameBn = "মেরিন ড্রাইভ স্কুটার ও বাইক রেন্টাল",
            serviceType = "Rent-a-car",
            phone = "+8801755112233",
            location = "লাবণী মোড়",
            descriptionBn = "নিজস্ব ড্রাইভিং লাইসেন্সে দিনব্যাপী স্কুটার বা বাইক নিয়ে সাগর পাড়ে ঘুরে বেড়ান।",
            estimatedFare = "৳৮০০ - ৳১৫০০ / দিন",
            imageUrl = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_7",
            nameBn = "সেন্টমার্টিন বিলাসবহুল ক্রুজ / শিপ টিকিট",
            serviceType = "Tourist vehicle",
            phone = "+8801819887766",
            location = "কক্সবাজার বিআইডব্লিউটিএ ঘাট",
            descriptionBn = "কক্সবাজার থেকে সরাসরি প্রবাল দ্বীপ সেন্টমার্টিনের প্রমোদতরী বুকিং সেবা।",
            estimatedFare = "৳১৫০০ - ৳৪০০০ (আসা-যাওয়া)",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80"
        ),
        TransportService(
            id = "trans_8",
            nameBn = "কক্স রেন্ট-এ-কার (হাইয়েস / নোয়া)",
            serviceType = "Rent-a-car",
            phone = "+8801712334499",
            location = "সদর রোড, কক্সবাজার",
            descriptionBn = "পরিবার ও দলবদ্ধ পর্যটকদের জন্য শীতাতপ নিয়ন্ত্রিত মাইক্রোবাস ও কার ভাড়া।",
            estimatedFare = "৳৩৫০০ - ৳৬০০০ / দিন",
            imageUrl = "https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?auto=format&fit=crop&w=800&q=80"
        )
    )

    val emergencies: List<EmergencyService> = listOf(
        EmergencyService(
            id = "emg_1",
            nameBn = "কক্সবাজার ট্যুরিস্ট পুলিশ হেডকোয়ার্টার",
            nameEn = "Cox's Bazar Tourist Police HQ",
            category = "Tourist Police",
            phone = "+8801320169999",
            location = "লাবণী পয়েন্ট পুলিশ বক্স",
            address = "Laboni Beach Road, Tourist Police Station, Cox's Bazar",
            is24x7 = true,
            isHotline = true
        ),
        EmergencyService(
            id = "emg_2",
            nameBn = "কক্সবাজার সদর জেলা হাসপাতাল (জরুরি বিভাগ)",
            nameEn = "Sadar District Hospital Emergency",
            category = "Hospital",
            phone = "+88034162122",
            location = "হাসপাতাল রোড, সদর",
            address = "Hospital Road, Cox's Bazar Sadar",
            is24x7 = true,
            isHotline = false
        ),
        EmergencyService(
            id = "emg_3",
            nameBn = "ফায়ার সার্ভিস ও সিভিল ডিফেন্স কক্সবাজার",
            nameEn = "Fire Service & Civil Defence",
            category = "Fire Service",
            phone = "+88034163333",
            location = "ঝিলংজা, কক্সবাজার",
            address = "Jhilongja Fire Station, Cox's Bazar",
            is24x7 = true,
            isHotline = true
        ),
        EmergencyService(
            id = "emg_4",
            nameBn = "সি-সেফ লাইফগার্ড স্টেশন (সৈকত উদ্ধার দল)",
            nameEn = "SeaSafe Beach Lifeguard Station",
            category = "Beach Safety",
            phone = "+8801815123456",
            location = "সুগন্ধা ও লাবণী বিচ লাইফগার্ড টাওয়ার",
            address = "Lifeguard Watch Towers, Laboni & Sugandha",
            is24x7 = false,
            isHotline = true
        ),
        EmergencyService(
            id = "emg_5",
            nameBn = "কলাতলী পুলিশ ফাঁড়ি",
            nameEn = "Kolatoli Police Outpost",
            category = "Police",
            phone = "+8801320165500",
            location = "কলাতলী মোড়",
            address = "Kolatoli Circle Police Camp",
            is24x7 = true,
            isHotline = false
        ),
        EmergencyService(
            id = "emg_6",
            nameBn = "রেড ক্রিসেন্ট ২৪/৭ অ্যাম্বুলেন্স সেবা",
            nameEn = "Red Crescent 24/7 Ambulance",
            category = "Ambulance",
            phone = "+8801819998877",
            location = "সদর কক্সবাজার",
            address = "Red Crescent Society, Hospital Gate",
            is24x7 = true,
            isHotline = true
        ),
        EmergencyService(
            id = "emg_7",
            nameBn = "কক্সবাজার হাইওয়ে পুলিশ ফাঁড়ি",
            nameEn = "Cox's Bazar Highway Police",
            category = "Police",
            phone = "+8801320185544",
            location = "লিংক রোড, কক্সবাজার",
            address = "Link Road Highway Outpost",
            is24x7 = true,
            isHotline = false
        ),
        EmergencyService(
            id = "emg_8",
            nameBn = "বাংলাদেশ কোস্ট গার্ড কন্ট্রোল রুম",
            nameEn = "Bangladesh Coast Guard Station",
            category = "Beach Safety",
            phone = "+8801766690123",
            location = "কক্সবাজার মোহনা",
            address = "Coast Guard Base, Nuniyarchara, Cox's Bazar",
            is24x7 = true,
            isHotline = true
        )
    )

    val news: List<NewsArticle> = listOf(
        NewsArticle(
            id = "news_1",
            titleBn = "কক্সবাজার সৈকতে বিশেষ পরিচ্ছন্নতা অভিযান ও পরিবেশ সচেতনতা কর্মসূচি শুরু",
            titleEn = "Massive beach cleanliness drive inaugurated in Cox's Bazar",
            category = "Beach",
            date = "আজ, ২৫ সেপ্টেম্বর",
            shortDescriptionBn = "বিশ্ব পর্যটন দিবস উপলক্ষে লাবণী থেকে সুগন্ধা পর্যন্ত বিশাল পরিচ্ছন্নতা ক্যাম্পেইন পরিচালিত হচ্ছে। পর্যটকদের সৈকত পরিষ্কার রাখতে আহ্বান জানানো হয়েছে।",
            fullContentBn = "কক্সবাজার জেলা প্রশাসন ও ট্যুরিজম বোর্ডের উদ্যোগে আজ সকাল থেকে সৈকতে দিনব্যাপী বিশেষ পরিচ্ছন্নতা অভিযান পরিচালিত হচ্ছে। প্লাস্টিক বর্জ্য অপসারণে স্বেচ্ছাসেবী সংগঠন, স্থানীয় শিক্ষার্থী এবং সচেতন পর্যটকরা অংশ নিয়েছেন। সৈকতের সৌন্দর্য ও জীববৈচিত্র্য রক্ষায় সৈকতে পলিথিন ও বোতল নির্দিষ্ট ডাস্টবিনে ফেলার অনুরোধ জানানো হয়েছে।",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            author = "কক্সবাজার ট্যুরিজম ডেস্ক"
        ),
        NewsArticle(
            id = "news_2",
            titleBn = "পূর্ণিমার প্রভাবে সাগরে বাড়তি জোয়ার: গোসলে লাল নিশানা সতর্কতা",
            titleEn = "High tide warning during full moon: Red flag hoisted at beach",
            category = "Safety",
            date = "গতকাল",
            shortDescriptionBn = "উত্তাল সাগরে ৩-৪ ফুট উচ্চতার ঢেউ থাকায় পর্যটকদের গভীর পানিতে নামতে নিষেধাজ্ঞা ও লাল নিশানা টাঙানো হয়েছে।",
            fullContentBn = "বঙ্গোপসাগরে অমাবস্যা-পূর্ণিমার প্রভাবে সাগরের ঢেউয়ের উচ্চতা স্বাভাবিকের চেয়ে কয়েক ফুট বৃদ্ধি পেয়েছে। সি-সেফ লাইফগার্ড সূত্রে জানানো হয়েছে, সৈকতের ঝুঁকিপূর্ণ অংশে গুপ্ত গর্ত ও শক্তিশালী আন্ডার-কারেন্ট সৃষ্টি হওয়ায় লাল পতাকা ওড়ানো হয়েছে। পর্যটকদের হাঁটু পানির বেশি নামতে নিষেধ করা হয়েছে।",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
            author = "সি-সেফ লাইফগার্ড কন্ট্রোল"
        ),
        NewsArticle(
            id = "news_3",
            titleBn = "আসন্ন পর্যটন মৌসুমে বসছে আন্তর্জাতিক ঘুড়ি উৎসব ও বিচ কার্নিভাল",
            titleEn = "International Kite Festival & Beach Carnival announced",
            category = "Events",
            date = "২২ সেপ্টেম্বর",
            shortDescriptionBn = "আগামী মাসে শত শত দেশি-বিদেশি পর্যটকের উপস্থিতিতে রঙিন ঘুড়িতে আকাশ ঢাকবে কক্সবাজার সৈকত।",
            fullContentBn = "কক্সবাজার সৈকতে ঐতিহ্যবাহী ঘুড়ি উৎসবের আনুষ্ঠানিক তারিখ ঘোষণা করা হয়েছে। এতে দেশ-বিদেশের খ্যাতিমান কাইট ফ্লায়াররা অংশ নেবেন। রাতে সৈকতে ফায়ার স্পিনিং, লোকসংগীত ও ঐতিহ্যবাহী নাচের আয়োজন থাকবে।",
            imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80",
            author = "কালচারাল উইং"
        ),
        NewsArticle(
            id = "news_4",
            titleBn = "মেরিন ড্রাইভে পর্যটকদের জন্য নতুন গতিসীমা ও নিরাপত্তা নির্দেশিকা জারি",
            titleEn = "New speed limits & safety guidelines on Marine Drive",
            category = "Travel Notice",
            date = "২০ সেপ্টেম্বর",
            shortDescriptionBn = "দুর্ঘটনা রোধে মেরিন ড্রাইভে সর্বোচ্চ গতিসীমা ৫০ কিমি এবং রাতে পাহাড়ি বাঁকে বিশেষ নজরদারির নির্দেশ।",
            fullContentBn = "কক্সবাজার থেকে টেকনাফ পর্যন্ত ৮০ কিলোমিটার দীর্ঘ নয়নাভিরাম মেরিন ড্রাইভ সড়কে পর্যটকবাহী জিপ, প্রাইভেট কার ও মোটরসাইকেলের চলাচলে শৃঙ্খলা বজায় রাখতে হাইওয়ে পুলিশ নতুন ট্রাফিক নির্দেশিকা জারি করেছে। সকল যানবাহনকে গতিসীমা মেনে চলতে বলা হয়েছে।",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80",
            author = "হাইওয়ে পুলিশ কক্সবাজার"
        ),
        NewsArticle(
            id = "news_5",
            titleBn = "হিমছড়িতে নতুন ইকো-ট্যুরিজম ট্রেইল উদ্বোধন",
            titleEn = "New eco-tourism hiking trail opened in Himchari",
            category = "Tourism",
            date = "১৮ সেপ্টেম্বর",
            shortDescriptionBn = "প্রকৃতিপ্রেমীদের জন্য পাহাড় ও অরণ্যের মাঝে দেড় কিলোমিটার দীর্ঘ পরিবেশবান্ধব হাঁটার পথ উন্মুক্ত।",
            fullContentBn = "হিমছড়ি জাতীয় উদ্যানের জীববৈচিত্র্য উপভোগ করার লক্ষ্যে নতুন ইকো-ট্রেইল তৈরি করা হয়েছে। এতে গাইডসহ দর্শনার্থীরা সুরক্ষিতভাবে বিভিন্ন পাহাড়ি পাখি, বুনো ফুল ও অর্কিড পর্যবেক্ষণ করতে পারবেন।",
            imageUrl = "https://images.unsplash.com/photo-1544644181-1484b3fdfc62?auto=format&fit=crop&w=800&q=80",
            author = "বন বিভাগ কক্সবাজার"
        ),
        NewsArticle(
            id = "news_6",
            titleBn = "ঢাকা-কক্সবাজার রুটে অতিরিক্ত পর্যটক স্পেশাল ট্রেনের সময়সূচি প্রকাশ",
            titleEn = "Cox's Bazar special trains added for upcoming holiday season",
            category = "Local News",
            date = "১৫ সেপ্টেম্বর",
            shortDescriptionBn = "ছুটির দিনে পর্যটকদের উপচে পড়া ভিড় সামাল দিতে রেলওয়ে নতুন ট্রিপ ঘোষণা করেছে।",
            fullContentBn = "ঝিনুক আকৃতির অত্যাধুনিক কক্সবাজার রেলওয়ে স্টেশনে নিয়মিত ট্রেনের পাশাপাশি আরও দুইটি পর্যটক স্পেশাল ট্রেন যুক্ত হচ্ছে। এতে সহজে ও নিরাপদে ভ্রমণকারী পর্যটকরা অল্প সময়ে যাতায়াত করতে পারবেন।",
            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80",
            author = "বাংলাদেশ রেলওয়ে"
        ),
        NewsArticle(
            id = "news_7",
            titleBn = "সৈকতে লাইফ জ্যাকেট ব্যবহার বাধ্যতামূলক করার উদ্যোগ গ্রহণ",
            titleEn = "Mandatory life jacket initiative for deep-water sea bathing",
            category = "Safety",
            date = "১২ সেপ্টেম্বর",
            shortDescriptionBn = "সাঁতার না জানা পর্যটকদের সুরক্ষায় প্রতিটি পয়েন্টে সুলভে লাইফ জ্যাকেট বুথ স্থাপন।",
            fullContentBn = "ট্যুরিস্ট পুলিশের পক্ষ থেকে জানানো হয়েছে, সৈকতে অনাকাঙ্ক্ষিত দুর্ঘটনা এড়াতে যারা সাঁতার জানেন না তাদের জন্য লাইফ জ্যাকেট পরা উৎসাহিত করা হচ্ছে। প্রতিটি বিচ পয়েন্টে লাইফগার্ডের তদারকি বাড়ানো হয়েছে।",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
            author = "ট্যুরিস্ট পুলিশ"
        ),
        NewsArticle(
            id = "news_8",
            titleBn = "ইনানী সৈকতে সামুদ্রিক কাছিমের ডিম পাড়ার মৌসুম শুরু",
            titleEn = "Sea turtle nesting season begins along Inani Coast",
            category = "Tourism",
            date = "১০ সেপ্টেম্বর",
            shortDescriptionBn = "বিলুপ্তপ্রায় অলিভ রিডলে কাছিম রক্ষায় সৈকত সংলগ্ন এলাকায় রাতের বেলা উজ্জ্বল আলো নিয়ন্ত্রণে নির্দেশনা।",
            fullContentBn = "ইনানী ও সোনাদিয়া উপকূলে শত শত সামুদ্রিক কাছিম ডিম পাড়তে আসছে। পরিবেশ গবেষকরা সৈকত সুরক্ষায় বিশেষ নার্সারি স্থাপন করেছেন। পর্যটকদের রাতের বেলা টর্চের আলো না ফেলতে অনুরোধ করা হয়েছে।",
            imageUrl = "https://images.unsplash.com/photo-1473186578172-c141e6798cf4?auto=format&fit=crop&w=800&q=80",
            author = "পরিবেশ অধিদপ্তর"
        ),
        NewsArticle(
            id = "news_9",
            titleBn = "লাবণী পয়েন্টে আধুনিক সানসেট ডেক ও ওয়াকওয়ে নির্মাণ কাজ সমাপ্ত",
            titleEn = "Modern sunset viewing deck completed at Laboni Point",
            category = "Beach",
            date = "০৮ সেপ্টেম্বর",
            shortDescriptionBn = "বয়স্ক ও শিশুদের জন্য নির্বিঘ্নে সূর্যাস্ত দেখার সুদৃশ্য কাঠের ডেক চালু করা হয়েছে।",
            fullContentBn = "লাবণী বিচে সৈকত লাগোয়া অংশে আধুনিক কাঠের ডেক ও আলোকসজ্জা সমৃদ্ধ সুন্দর ফুটপাথ তৈরি করা হয়েছে। সন্ধ্যায় পর্যটকরা নিরাপদে বসে সাগরের বাতাস উপভোগ করতে পারছেন।",
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
            author = "পৌরসভা কক্সবাজার"
        ),
        NewsArticle(
            id = "news_10",
            titleBn = "হোটেল ও রেস্তোরাঁয় ন্যায্য মূল্য তালিকা প্রদর্শনের কঠোর নির্দেশনা",
            titleEn = "Strict directives issued on displaying fair hotel tariffs",
            category = "Travel Notice",
            date = "০৫ সেপ্টেম্বর",
            shortDescriptionBn = "পর্যটক হয়রানি রোধে জেলা প্রশাসনের ভ্রাম্যমাণ আদালত পরিচালনা ও সার্বক্ষণিক অভিযোগ কেন্দ্র চালু।",
            fullContentBn = "কক্সবাজারের প্রতিটি হোটেল ও খাবার দোকানে নির্ধারিত মূল্য তালিকা টাঙানো বাধ্যতামূলক করা হয়েছে। কোনো পর্যটক অতিরিক্ত মূল্যের শিকার হলে হটলাইন ৩৩৩ বা ট্যুরিস্ট পুলিশ বক্সে তৎক্ষণাৎ জানাতে পারবেন।",
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=800&q=80",
            author = "জেলা প্রশাসন কক্সবাজার"
        )
    )

    val events: List<TourismEvent> = listOf(
        TourismEvent(
            id = "evt_1",
            nameBn = "কক্সবাজার বিচ কার্নিভাল ২০২৬",
            nameEn = "Cox's Bazar Grand Beach Carnival",
            date = "১৫ - ১৮ অক্টোবর, ২০২৬",
            time = "বিকাল ৪:০০ - রাত ১০:০০",
            location = "লাবণী বিচ অ্যাম্পিথিয়েটার",
            descriptionBn = "চার দিনব্যাপী মহাসমারোহে বিচ কনসার্ট, লোকনৃত্য, সামুদ্রিক খাবার প্রদর্শনী এবং আতশবাজির মেলা।",
            category = "Carnival",
            imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_2",
            nameBn = "বার্ষিক আন্তর্জাতিক ঘুড়ি উৎসব",
            nameEn = "Annual International Kite Festival",
            date = "২৮ অক্টোবর, ২০২৬",
            time = "দুপুর ২:০০ - সন্ধ্যা ৬:৩০",
            location = "সুগন্ধা ও কলাতলী সৈকত",
            descriptionBn = "শত শত দৈত্যাকার ড্রাগন, পাখি ও সামুদ্রিক প্রাণীর আকৃতির রঙিন ঘুড়িতে নীল আকাশ সাজবে।",
            category = "Festival",
            imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_3",
            nameBn = "পূর্ণিমা বিচ মিউজিক ফেস্ট",
            nameEn = "Full Moon Coastal Music Night",
            date = "০৫ নভেম্বর, ২০২৬",
            time = "সন্ধ্যা ৭:০০ - রাত ১১:০০",
            location = "ইনানী সৈকত ভেন্যু",
            descriptionBn = "সাগরের মৃদু ঢেউ আর পূর্ণিমার চাঁদের আলোর নিচে অ্যাকোস্টিক বাংলা গান ও বাঁশির সুরের আসর।",
            category = "Concert",
            imageUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_4",
            nameBn = "মেরিন ড্রাইভ সাইক্লিং র‍্যালি",
            nameEn = "Marine Drive Coastal Cycling Rally",
            date = "১২ নভেম্বর, ২০২৬",
            time = "সকাল ৬:০০ - সকাল ১০:০০",
            location = "কলাতলী থেকে ইনানী বিচ (২৬ কিমি)",
            descriptionBn = "সকাল বেলার স্নিগ্ধ বাতাসে মেরিন ড্রাইভ সড়কে দুই শতাধিক সাইক্লিস্টের বর্ণাঢ্য উপকূলীয় শোভাযাত্রা।",
            category = "Sports",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_5",
            nameBn = "ঐতিহ্যবাহী রাখাইন জলকেলি উৎসব",
            nameEn = "Traditional Rakhine Water Festival",
            date = "২০ নভেম্বর, ২০২৬",
            time = "সকাল ১০:০০ - বিকাল ৫:০০",
            location = "রাখাইন পাড়া ও মহেশখালী",
            descriptionBn = "রাখাইন সম্প্রদায়ের নিজস্ব সংস্কৃতিতে একে অপরকে পবিত্র জল ছিটিয়ে আনন্দ ভাগ করে নেওয়ার বর্ণিল উৎসব।",
            category = "Cultural",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_6",
            nameBn = "কক্স সি-ফুড ফিয়েস্তা ও রন্ধন প্রদর্শনী",
            nameEn = "Cox's Bazar Seafood Fiesta",
            date = "০১ - ০৩ ডিসেম্বর, ২০২৬",
            time = "বেলা ১২:০০ - রাত ১১:০০",
            location = "সুগন্ধা ওপেন স্কয়ার",
            descriptionBn = "দেশের শীর্ষস্থানীয় শেফদের লাইভ রান্না এবং ৫০টিরও বেশি সামুদ্রিক মাছের বিশেষ পদের স্বাদ নেওয়ার সুযোগ।",
            category = "Food",
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_7",
            nameBn = "জাতীয় বিচ ভলিবল চ্যাম্পিয়নশিপ",
            nameEn = "National Beach Volleyball Championship",
            date = "১০ - ১৩ ডিসেম্বর, ২০২৬",
            time = "সকাল ৯:০০ - বিকাল ৫:৩০",
            location = "লাবণী বিচ স্পোর্টস কোর্ট",
            descriptionBn = "সৈকতের সোনালি বালুতে দেশের সেরা জেলা দলগুলোর রোমাঞ্চকর ভলিবল লড়াই।",
            category = "Sports",
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80"
        ),
        TourismEvent(
            id = "evt_8",
            nameBn = "কোস্টাল ইকো-ম্যারাথন ২০২৬",
            nameEn = "Coastal Eco-Marathon (10K & 21K)",
            date = "২৫ ডিসেম্বর, ২০২৬",
            time = "ভোর ৫:৩০ - সকাল ৯:০০",
            location = "মেরিন ড্রাইভ জিরো পয়েন্ট",
            descriptionBn = "পরিবেশ রক্ষার শপথ নিয়ে সাগরের তীর ঘেঁষে দৌড়বিদদের বার্ষিক স্বাস্থ্য ও ফিটনেস ম্যারাথন।",
            category = "Sports",
            imageUrl = "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=800&q=80"
        )
    )

    val beachSafetyInfo = BeachSafetyInfo(
        flag = BeachFlag.GREEN,
        flagTitleBn = "সবুজ পতাকা (সাঁতারের জন্য নিরাপদ)",
        flagDescriptionBn = "বর্তমানে সমুদ্র শান্ত রয়েছে এবং লাইফগার্ড পর্যবেক্ষণের আওতায় নির্দিষ্ট সীমানার মধ্যে নিরাপদে পানিতে নামা যাবে।",
        highTideTime = "দুপুর ০১:৪৫",
        lowTideTime = "সন্ধ্যা ০৭:৩০",
        tideWarningBn = "ভাটার সময় সাগরে পানির টান অত্যন্ত প্রবল থাকে। ভাটা চলাকালীন কোনো অবস্থাতেই কোমর পানির বেশি নামবেন না।",
        nightSwimmingWarningBn = "সন্ধ্যার পর (সূর্যাস্তের পর) সাগরে নামা সম্পূর্ণ বেআইনি ও অত্যন্ত ঝুঁকিপূর্ণ। অন্ধকারে চোরাবালির খাদ দেখা যায় না।",
        seaConditionBn = "স্বাভাবিক জোয়ার, মৃদু ঢেউ (উচ্চতা ১.৫ ফুট)",
        lifeguardHotline = "+8801815123456",
        safetyTips = listOf(
            "সর্বদা লাইফগার্ড টাওয়ারের দৃশ্যমান সীমানার মধ্যে গোসল করুন।",
            "সৈকতে ওড়ানো পতাকা খেয়াল করুন: সবুজ পতাকা = নিরাপদ, লাল পতাকা = বিপজ্জনক বা নামা নিষিদ্ধ।",
            "ভাটার সময় সাগরে নামবেন না, জোয়ারের শুরুতে নামাই সবচেয়ে নিরাপদ।",
            "সাঁতার না জানলে অবশ্যই রেন্টাল বুথ থেকে লাইফ জ্যাকেট বা টিউব পরিধান করুন।",
            "শিশুদের সবসময় হাতের নাগালের মধ্যে রাখুন, কখনও একা পানিতে ছেড়ে দেবেন না।",
            "চোরাবালির গর্ত বা হঠাৎ গভীর হওয়া এলাকা (গুপ্ত খাদ) এড়িয়ে চলুন।",
            "জরুরি প্রয়োজনে লাইফগার্ড অথবা ট্যুরিস্ট পুলিশের সাহায্য চান (হটলাইন: ৯৯৯)।"
        )
    )

    val weather = WeatherInfo(
        currentTemp = "২৯° সে.",
        conditionBn = "আংশিক মেঘলা ও মনোরম সমুদ্র বাতাস",
        conditionEn = "Pleasant Coastal Breeze",
        humidity = "৭৪%",
        wind = "১৪ কিমি/ঘণ্টা (দক্ষিণ-পশ্চিম)",
        rainChance = "২০%",
        sunrise = "সকাল ০৫:৪৬",
        sunset = "সন্ধ্যা ০৫:৫২",
        forecasts = listOf(
            WeatherForecastDay("আজ", "Today", "৩১°", "২৫°", "রৌদ্রোজ্জ্বল"),
            WeatherForecastDay("আগামীকাল", "Tomorrow", "৩০°", "২৪°", "মৃদু বাতাস"),
            WeatherForecastDay("রবিবার", "Sunday", "২৯°", "২৫°", "হালকা বৃষ্টি"),
            WeatherForecastDay("সোমবার", "Monday", "৩২°", "২৬°", "উজ্জ্বল রোদ"),
            WeatherForecastDay("মঙ্গলবার", "Tuesday", "৩১°", "২৫°", "পরিষ্কার আকাশ")
        )
    )

    val notifications: List<AppNotification> = listOf(
        AppNotification(
            id = "notif_1",
            titleBn = "জোয়ারের বিশেষ সতর্কতা",
            messageBn = "আজ দুপুর ০১:৪৫ মিনিটে পূর্ণ জোয়ার শুরু হবে। সাগরে গোসলের পূর্বে সৈকতের লাইফগার্ড নির্দেশনা অনুসরণ করুন।",
            category = "Safety",
            timeAgo = "১০ মিনিট আগে",
            isRead = false
        ),
        AppNotification(
            id = "notif_2",
            titleBn = "আসন্ন ঘুড়ি উৎসবের আমন্ত্রণ",
            messageBn = "কলাতলী বিচে আগামী ২৮ অক্টোবর অনুষ্ঠিত হতে যাচ্ছে বর্ণাঢ্য আন্তর্জাতিক ঘুড়ি উৎসব।",
            category = "Event",
            timeAgo = "২ ঘণ্টা আগে",
            isRead = false
        ),
        AppNotification(
            id = "notif_3",
            titleBn = "হিমছড়ি ইকো ট্রেইল উন্মুক্ত",
            messageBn = "প্রকৃতিপ্রেমীদের জন্য হিমছড়িতে নতুন দেড় কিলোমিটার ট্র্যাকিং ট্রেইল চালু করা হয়েছে।",
            category = "Tourism",
            timeAgo = "১ দিন আগে",
            isRead = true
        ),
        AppNotification(
            id = "notif_4",
            titleBn = "ট্যুরিস্ট পুলিশ হেল্পলাইন আপডেট",
            messageBn = "যেকোনো জরুরি সহায়তায় পর্যটকদের জন্য ট্যুরিস্ট পুলিশ হটলাইন +8801320-169999 চালু রয়েছে।",
            category = "Alert",
            timeAgo = "২ দিন আগে",
            isRead = true
        )
    )

    val sampleReviews: List<UserReview> = listOf(
        UserReview("rev_1", "spot_1", "তানভীর আহমেদ", 5, "লাবণী বিচের সূর্যাস্ত অসাধারণ! সন্ধ্যায় বসার চমৎকার পরিবেশ।", "২ দিন আগে"),
        UserReview("rev_2", "spot_4", "সাদিয়া ইসলাম", 5, "ইনানীর প্রবাল পাথর আর নীল জলরাশি মন জুড়িয়ে দেয়। মেরিন ড্রাইভের রাস্তা দারুণ।", "১ সপ্তাহ আগে"),
        UserReview("rev_3", "hotel_1", "মাহমুদুল হাসান", 5, "সায়মন বিচ রিসোর্টের ইনফিনিটি পুল থেকে সমুদ্রের দৃশ্য চোখে লেগে থাকার মতো। স্টাফদের ব্যবহার খুব ভালো।", "৩ দিন আগে"),
        UserReview("rev_4", "rest_1", "ফারহানা করিম", 5, "ঝাউবনের রূপচাঁদা ফ্রাই আর লইট্টা শুঁটকি ভুনা অতুলনীয়। সবসময় গরম গরম পরিবেশন করে।", "৫ দিন আগে"),
        UserReview("rev_5", "spot_5", "রাকিবুল আলম", 4, "হিমছড়ির পাহাড়ের চূড়া থেকে সাগর দেখার অনুভূতি অন্যরকম। সিঁড়ি ওঠার সময় একটু সাবধানে উঠবেন।", "৪ দিন আগে")
    )

    val travelAdvisories: List<TravelAdvisoryItem> = listOf(
        TravelAdvisoryItem(
            id = "adv_1",
            titleBn = "কক্সবাজার হোটেল ভাড়ার সিজনাল তারতম্য ও বুকিং পরামর্শ",
            tipBn = "নভেম্বর থেকে ফেব্রুয়ারি মাস হলো কক্সবাজারের পিক-সিজন। এই সময়ে হোটেল ভাড়া স্বাভাবিকের তুলনায় ৩০% থেকে ৫০% পর্যন্ত বৃদ্ধি পেতে পারে। সাশ্রয়ী ভ্রমণ করতে চাইলে রবি থেকে বুধবারের মধ্যে বুকিং করুন, শুক্র-শনিবার উইকেন্ডে সবসময় অতিরিক্ত চাপ থাকে।",
            categoryBn = "বুকিং পরামর্শ",
            sourceName = "গুগল ট্রাভেল ও বাংলাদেশ পর্যটন করপোরেশন",
            sourceUrl = "https://www.google.com/travel/hotels/Cox's+Bazar"
        ),
        TravelAdvisoryItem(
            id = "adv_2",
            titleBn = "সৈকতের কাছে থাকার সঠিক লোকেশন নির্বাচন",
            tipBn = "লাবণী পয়েন্ট ও সুগন্ধা বিচ এলাকা সাধারণত সবচেয়ে জনবহুল এবং খাবারের দোকানের কাছে। পরিবার নিয়ে নিরিবিলি সময় কাটাতে চাইলে কলাতলী মেরিন ড্রাইভ বা ইনানী বিচ সংলগ্ন রিসোর্টগুলো বেছে নেওয়া উত্তম।",
            categoryBn = "অবস্থান নির্বাচন",
            sourceName = "কক্সবাজার জেলা প্রশাসন ও ট্যুরিজম সেল",
            sourceUrl = "https://www.google.com/search?q=Coxs+Bazar+Hotel+Guide"
        ),
        TravelAdvisoryItem(
            id = "adv_3",
            titleBn = "অননুমোদিত দালাল ও মধ্যস্বত্বভোগী সতর্কতা",
            tipBn = "বাস টার্মিনাল বা মোড়ে দাঁড়িয়ে থাকা অননুমোদিত দালালদের মাধ্যমে হোটেল ভাড়া করবেন না। সরাসরি হোটেলের অফিশিয়াল ফ্রন্টডেস্কে কথা বলুন এবং রিসিপশন কাউন্টার থেকে মূল্যের ক্যাশ রিসিট সংগ্রহ করুন।",
            categoryBn = "নিরাপত্তা ও সতর্কতা",
            sourceName = "ট্যুরিস্ট পুলিশ বাংলাদেশ",
            sourceUrl = "https://tourist.police.gov.bd"
        ),
        TravelAdvisoryItem(
            id = "adv_4",
            titleBn = "সি-ভিউ রুম ও প্রকৃত দূরত্বের পার্থক্য",
            tipBn = "কিছু হোটেল 'সি-ফেসিং' বললেও সরাসরি সমুদ্র দৃশ্যমান নাও হতে পারে। বুকিংয়ের আগে রুমটি সরাসরি বিচমুখী কি না এবং বারান্দা থেকে সমুদ্র দৃশ্যমান কি না তা যাচাই করে নিন।",
            categoryBn = "রুম নির্বাচন",
            sourceName = "গুগল লোকাল গাইডস রিভিউ ফোরাম",
            sourceUrl = "https://maps.google.com"
        )
    )
}
