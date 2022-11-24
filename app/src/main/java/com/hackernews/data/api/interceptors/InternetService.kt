package com.hackernews.data.api.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build

class InternetService {

    private lateinit var wifiManager: WifiManager

    private lateinit var connectivityManager: ConnectivityManager

    companion object {
        val instance = InternetService()
    }

    fun initializeWithApplicationContext(context: Context) {

        wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    fun isOnline(): Boolean {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

                if (capabilities != null) {

                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                    }

                }

            } else {

                val activeNetwork = connectivityManager.activeNetworkInfo

                return if (activeNetwork != null) {

                    activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                            activeNetwork.type == ConnectivityManager.TYPE_MOBILE

                } else false

            }

        } catch (e: Exception) {

            e.printStackTrace()

        }

        return false

    }

}