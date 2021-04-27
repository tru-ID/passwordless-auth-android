package com.example.tru_phonecheck.utlis



import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

fun isPhoneNumberFormatValid(phoneNumber: String): Boolean {
    // Test phone numbers
    if (phoneNumber == "+447700900000" /* pass in sandbox mode */ ||
        phoneNumber == "+447700900001" /* fail in sandbox mode */) {
        return true
    }

    val phoneNumberUtil = PhoneNumberUtil.getInstance()
    return try {
        phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(
            phoneNumber, Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name))
    } catch (e: NumberParseException) {
        false
    }
}