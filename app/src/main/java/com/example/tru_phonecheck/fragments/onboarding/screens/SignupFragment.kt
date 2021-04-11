package com.example.tru_phonecheck.fragments.onboarding.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button

import com.example.tru_phonecheck.R
import com.example.tru_phonecheck.api.data.PhoneCheck
import com.example.tru_phonecheck.api.data.PhoneCheckPost
import com.example.tru_phonecheck.api.data.PhoneCheckResponse
import com.example.tru_phonecheck.api.data.RedirectManager
import com.example.tru_phonecheck.api.retrofit.RetrofitService
import com.example.tru_phonecheck.utlis.isPhoneNumberFormatValid
import com.google.android.material.snackbar.Snackbar
import id.tru.sdk.TruSDK
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SignupFragment : Fragment() {
    //private val redirectManager by lazy { RedirectManager() }
    private   val truSDK = TruSDK.getInstance();
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
            if(isPhoneNumberFormatValid(phoneNumber)){
                println("valid number")
                // disable button before async work
                toggleButtonStatus(submitHandler, true)

                CoroutineScope(Dispatchers.IO).launch {
                  try {
                      val response = rf().createPhoneCheck(PhoneCheckPost(phoneNumber))

                      if(response.isSuccessful && response.body() != null){
                    val phoneCheck = response.body() as PhoneCheck
                          // open checkURL
                          openCheckURL(phoneCheck)


                       // enable button
                          toggleButtonStatus(submitHandler, false)

                      }
                  } catch(e: Throwable){
                      Snackbar.make(container as View, e.message!!, Snackbar.LENGTH_SHORT).show()
                  }
                }
            } else {
                Snackbar.make(container as View, "Invalid Phone Number", Snackbar.LENGTH_LONG).show()
            }
        }
        return view;
    }

    //retrofit setup
    private fun rf(): RetrofitService {
        return  Retrofit.Builder().baseUrl(RetrofitService.base_url).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService::class.java)
    }
    // function for disabling and enabling a button
    private fun toggleButtonStatus (button: Button?, isDisabled: Boolean){
       if(isDisabled){
           button?.isClickable = false;
           button?.isEnabled = false;
       } else {
           button?.isClickable = true;
           button?.isEnabled = true;
       }
    }
        // open check URL
    private fun openCheckURL(phoneCheck: PhoneCheck){
            CoroutineScope(Dispatchers.IO).launch{
                truSDK.openCheckUrl(phoneCheck.check_url)
                //redirectManager.openCheckUrl(phoneCheck.check_url)
                    // get phoneCheckResult
                getPhoneCheckResult(phoneCheck.check_id)

            }

        }
    // get PhoneCheck
   private fun getPhoneCheckResult(checkId: String){
        CoroutineScope(Dispatchers.IO).launch {
            val response = rf().getPhoneCheck(checkId)

            if(response.isSuccessful && response.body() != null){
                val phoneCheckResponse = response.body() as PhoneCheckResponse

                // update UI with phoneCheckResponse
                if(phoneCheckResponse.match){
                Snackbar.make(container, "Registration Successful", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(container, "Registration Failed", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


}
