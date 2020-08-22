package com.example.dunzoassignment.presentation

import android.content.Context
import android.net.ConnectivityManager


fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val ConnectMgr = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val NetInfo = ConnectMgr.activeNetworkInfo ?: return false
    return NetInfo.isConnected
}

fun getEachImageWidth(context: Context): Int {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    return (dpWidth/3).toInt()
}