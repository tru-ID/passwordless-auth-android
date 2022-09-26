package com.example.tru_phonecheck.api.retrofit

import com.example.tru_phonecheck.api.data.PhoneCheck
import com.example.tru_phonecheck.api.data.PhoneCheckComplete
import com.example.tru_phonecheck.api.data.PhoneCheckPost
import com.example.tru_phonecheck.api.data.PhoneCheckResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @Headers("Content-Type: application/json")
    @POST("/v0.2/phone-check")
    suspend fun createPhoneCheck(@Body user: PhoneCheckPost): Response<PhoneCheck>

    @POST("/v0.2/phone-check/exchange-code")
    suspend fun completePhoneCheck(@Body check: PhoneCheckComplete): Response<PhoneCheckResponse>

    companion object {
        // set up base_url in the format https://{subdomain}.{region}.ngrok.io gotten from ngrok URL
        const val base_url = "https://{subdomain}.{region}.ngrok.io"
    }
}
