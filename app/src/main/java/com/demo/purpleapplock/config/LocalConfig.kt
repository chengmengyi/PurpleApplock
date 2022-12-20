package com.demo.purpleapplock.config

import com.demo.purpleapplock.bean.ServerBean

object LocalConfig {
    const val URL=""
    const val EMAIL=""

    val localServerList= arrayListOf(
        ServerBean(
            ip = "100.223.52.0",
            salasana = "123456",
            kaupunki = "Japan",
            maa = "Tokyo",
            satama = 100,
            tilinumero = "chacha20-ietf-poly1305"
        ),
        ServerBean(
            ip = "100.223.52.78",
            salasana = "123456",
            kaupunki = "UnitedStates",
            maa = "Tokyo",
            satama = 100,
            tilinumero = "chacha20-ietf-poly1305"
        ),
    )


    const val localAd="""{
    "max_click":15,
    "max_show":50,
    "purple_open": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/3419835294",
            "purple_type": "o",
            "purple_sort": 1
        },
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/3419835294A",
            "purple_type": "o",
            "purple_sort": 2
        }
    ],
    "purple_home": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/2247696110",
            "purple_type": "y",
            "purple_sort": 2
        },
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/2247696110A",
            "purple_type": "y",
            "purple_sort": 3
        }
    ],
    "purple_lock_home": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/2247696110",
            "purple_type": "y",
            "purple_sort": 2
        }
    ],
     "pa_vpnhome_n": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/2247696110",
            "purple_type": "y",
            "purple_sort": 2
        }
    ],
      "pa_vpnresult_n": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/2247696110",
            "purple_type": "y",
            "purple_sort": 2
        }
    ],
     "pa_vpnlink_i": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433",
            "purple_type": "c",
            "purple_sort": 2
        }
    ],
     "pa_server_i": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433",
            "purple_type": "c",
            "purple_sort": 2
        }
    ],
     "pa_homeclick_i": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433",
            "purple_type": "c",
            "purple_sort": 2
        }
    ],
      "purple_lock": [
        {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433",
            "purple_type": "c",
            "purple_sort": 2
        },
         {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433",
            "purple_type": "c",
            "purple_sort": 1
        },
         {
            "purple_source": "admob",
            "purple_id": "ca-app-pub-3940256099942544/8691691433A",
            "purple_type": "c",
            "purple_sort": 3
        }
    ]
}"""
}