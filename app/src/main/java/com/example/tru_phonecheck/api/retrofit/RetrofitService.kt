package com.example.tru_phonecheck.api.retrofit

import com.example.tru_phonecheck.api.data.PhoneCheck
import com.example.tru_phonecheck.api.data.PhoneCheckPost
import com.example.tru_phonecheck.api.data.PhoneCheckResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @Headers("Content-Type: application/json")
    @POST("/phone-check")
    suspend fun createPhoneCheck(@Body user: PhoneCheckPost): Response<PhoneCheck>

    @GET("/phone-check")
    suspend fun getPhoneCheck(@Query("check_id") checkId: String): Response<PhoneCheckResponse>

    companion object {
        // set up base_url in the format https://{subdomain}.loca.lt gotten from localTunnel URL
        const val base_url = "https://tru-id.loca.lt"
    }
}