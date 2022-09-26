package com.example.tru_phonecheck.fragments.onboarding.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.tru_phonecheck.R
import com.example.tru_phonecheck.api.retrofit.RetrofitService
import com.example.tru_phonecheck.utlis.isPhoneNumberFormatValid
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.tru_phonecheck.api.data.PhoneCheckPost
import com.example.tru_phonecheck.api.data.PhoneCheck
import com.example.tru_phonecheck.api.data.PhoneCheckComplete
import com.example.tru_phonecheck.api.data.PhoneCheckResponse
import id.tru.sdk.TruSDK
import org.json.JSONObject
import java.net.URL

class SignupFragment : Fragment() {
    private val truSDK = TruSDK.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_signup, container, false);

        view.submitHandler.setOnClickListener {
            // get phone number
            val phoneNumber = phoneNumberInput.text.toString()
            Log.d("phone number is", phoneNumber)

            // close virtual keyboard
            phoneNumberInput.onEditorAction(EditorInfo.IME_ACTION_DONE)

            // if it's a valid phone number begin createPhoneCheck
            if (!isPhoneNumberFormatValid(phoneNumber)) {
                Snackbar.make(container as View, "Invalid Phone Number", Snackbar.LENGTH_LONG).show()
            } else {
                println("valid number")

                // disable the UI
                setUIStatus(submitHandler, phoneNumberInput, false)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = rf().createPhoneCheck(PhoneCheckPost(phoneNumber))

                        if (response.isSuccessful && response.body() != null) {
                            val phoneCheck = response.body() as PhoneCheck

                            val resp: JSONObject? = TruSDK.getInstance().openWithDataCellular(URL(phoneCheck.check_url), false)
                            if (resp != null) {
                                if (resp.optString("error") != "") {
                                    Snackbar.make(container as View, "Authentication Failed, Unexpected Error", Snackbar.LENGTH_LONG).show()
                                } else {
                                    val status = resp.optInt("http_status")
                                    if (status == 200) {
                                        val body = resp.optJSONObject("response_body")
                                        if (body != null) {
                                            val code = body.optString("code")
                                            if (code != null) {
                                                val checkId = body.optString("check_id")
                                                val ref = body.optString("check_id")

                                                // TODO: Make a POST request to backend to validate check_id and code
                                                val response = rf().completePhoneCheck(PhoneCheckComplete(checkId, code))

                                                if (response.isSuccessful && response.body() != null) {
                                                    val phoneCheckResponse = response.body() as PhoneCheckResponse

                                                    // update UI with phoneCheckResponse
                                                    if (phoneCheckResponse.match) {
                                                        findNavController().navigate(R.id.action_signupFragment_to_signedUpFragment)
                                                    } else {
                                                        Snackbar.make(container as View, "Registration Failed", Snackbar.LENGTH_LONG).show()
                                                    }
                                                }
                                                else {
                                                    Snackbar.make(container as View, "An unexpected problem occurred", Snackbar.LENGTH_LONG).show()
                                                }
                                            } else {
                                                val error = body.optString("error")
                                                val desc = body.optString("error_description")
                                                Snackbar.make(container as View,
                                                    "Authentication Failed, $desc", Snackbar.LENGTH_LONG).show()
                                            }
                                        } else {
                                            // invalid response format
                                            Snackbar.make(container as View, "Authentication Failed", Snackbar.LENGTH_LONG).show()
                                        }
                                    } else if (status == 400) {
                                        // MNO not supported
                                        Snackbar.make(container as View, "Authentication Failed, MNO not supported", Snackbar.LENGTH_LONG).show()
                                    } else if (status == 412) {
                                        // MNO a mobile IP
                                        Snackbar.make(container as View, "Authentication Failed, Not a Mobile IP", Snackbar.LENGTH_LONG).show()
                                    } else {
                                        Snackbar.make(container as View, "Authentication Failed", Snackbar.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    } catch(e: Throwable){
                        Snackbar.make(container as View, e.message!!, Snackbar.LENGTH_SHORT).show()
                    }

                    // enable the UI
                    setUIStatus(submitHandler, phoneNumberInput, true)
                }
            }
        }

        return view;
    }

    private fun setUIStatus (button: Button?, input: EditText, enabled: Boolean){
        activity?.runOnUiThread {
            button?.isClickable = enabled
            button?.isEnabled = enabled
            input.isEnabled = enabled
        }
    }

    //retrofit setup
    private fun rf(): RetrofitService {
        return  Retrofit.Builder().baseUrl(RetrofitService.base_url).addConverterFactory(
            GsonConverterFactory.create()).build().create(RetrofitService::class.java)
    }
}
