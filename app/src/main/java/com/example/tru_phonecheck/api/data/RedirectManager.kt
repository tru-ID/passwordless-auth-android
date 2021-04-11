package com.example.tru_phonecheck.api.data

import android.util.Log
import id.tru.sdk.TruSDK

class RedirectManager {
    private   val truSDK = TruSDK.getInstance();
    fun openCheckUrl(checkUrl: String) {
        Log.d("RedirectManager", "Triggering open check url $checkUrl")
        return truSDK.openCheckUrl(checkUrl)
    }
}