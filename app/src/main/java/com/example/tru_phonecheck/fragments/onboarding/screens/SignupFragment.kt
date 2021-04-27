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
import com.example.tru_phonecheck.api.data.PhoneCheck
import com.example.tru_phonecheck.api.data.PhoneCheckPost
import com.example.tru_phonecheck.api.data.PhoneCheckResponse
import com.example.tru_phonecheck.api.retrofit.RetrofitService
import com.example.tru_phonecheck.utlis.isPhoneNumberFormatValid
import com.google.android.material.snackbar.Snackbar
import id.tru.sdk.TruSDK
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupFragment : Fragment() {

    private val truSDK = TruSDK.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false);

        view.submitHandler.setOnClickListener {
            // get phone number
            val phoneNumber = phoneNumberInput.text.toString()
            Log.d("phone number is", phoneNumber)

            // close virtual keyboard
            phoneNumberInput.onEditorAction(EditorInfo.IME_ACTION_DONE)

            // if it's a valid phone number begin createPhoneCheck
            if(!isPhoneNumberFormatValid(phoneNumber)) {
                Snackbar.make(container as View, "Invalid Phone Number", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                println("valid number")

                // disable UI
                setUIStatus(submitHandler, phoneNumberInput, false)

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = rf().createPhoneCheck(PhoneCheckPost(phoneNumber))

                        if(response.isSuccessful && response.body() != null){
                            val phoneCheck = response.body() as PhoneCheck
                            // open checkURL
                            truSDK.openCheckUrl(phoneCheck.check_url)

                            // get PhoneCheck result
                            val response = rf().getPhoneCheck(phoneCheck.check_id)

                            if(response.isSuccessful && response.body() != null){
                                val phoneCheckResponse = response.body() as PhoneCheckResponse

                                // update UI with phoneCheckResponse
                                if(phoneCheckResponse.match){
                                    findNavController().navigate(R.id.action_signupFragment_to_signedUpFragment)
                                } else {
                                    Snackbar.make(container as View, "Registration Failed", Snackbar.LENGTH_LONG).show()
                                }
                            }
                            else {
                                Snackbar.make(container as View, "An unexpected problem occurred", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    } catch(e: Throwable){
                        Snackbar.make(container as View, e.message!!, Snackbar.LENGTH_SHORT).show()
                    }

                    // enable button
                    setUIStatus(submitHandler, phoneNumberInput, true)
                }
            }
        }

        return view;
    }


    //retrofit setup
    private fun rf(): RetrofitService {
        return  Retrofit.Builder().baseUrl(RetrofitService.base_url).addConverterFactory(
            GsonConverterFactory.create()).build().create(RetrofitService::class.java)
    }

    private fun setUIStatus (button: Button?, input: EditText, enabled: Boolean){
        activity?.runOnUiThread {
                button?.isClickable = enabled
                button?.isEnabled = enabled
                input.isEnabled = enabled
        }
    }
}
