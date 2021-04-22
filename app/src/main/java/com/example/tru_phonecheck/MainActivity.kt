package com.example.tru_phonecheck

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.tru.sdk.TruSDK

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // initialize Tru SDK
        TruSDK.initializeSdk(applicationContext)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
    }
}
