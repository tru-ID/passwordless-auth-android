package com.example.tru_phonecheck.api.data

import com.google.gson.annotations.SerializedName

data class PhoneCheck(  @SerializedName("check_url")
                        val check_url: String,
                        @SerializedName("check_id")
                        val check_id: String) {
}

data class PhoneCheckPost (
    @SerializedName("phone_number")
    val phone_number: String
)

data class PhoneCheckResponse(
    @SerializedName("check_id")
    val check_id: String,
    @SerializedName("match")
    val match: Boolean
)