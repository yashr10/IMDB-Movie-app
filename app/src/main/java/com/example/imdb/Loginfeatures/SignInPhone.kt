package com.example.imdb.Loginfeatures

import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imdb.MainActivity
import com.example.imdb.databinding.ActivitySignInPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


@SuppressLint("StaticFieldLeak")
private lateinit var binding :ActivitySignInPhoneBinding
private lateinit var mAuth: FirebaseAuth

class SignInPhone : AppCompatActivity() {

    private var verificationId : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()


        binding.progressBarPhone.visibility = View.INVISIBLE

        binding.verifyOtpPoneActivity.setOnClickListener(){
            binding.ccp.registerCarrierNumberEditText(binding.editTextPhone)
            val checkPhoneNumber = binding.editTextPhone.text.toString().trim()
            val phone = binding.ccp.fullNumberWithPlus


            if (checkPhoneNumber.isEmpty() || checkPhoneNumber.length != 11){
                binding.editTextPhone.error = "Enter a Valid Phone Number"
                binding.editTextPhone.requestFocus()
                Log.d("Number", checkPhoneNumber)
                return@setOnClickListener
            }

            val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(phoneAuthCallBack)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)

        }

        binding.codeverify.setOnClickListener(){
            val code = binding.codePhoneActivity.text.toString().trim()

            if (code.isEmpty()){
                binding.codePhoneActivity.error = "Code Required"
                binding.codePhoneActivity.requestFocus()
                return@setOnClickListener
            }

            verificationId?.let {

                val credential = PhoneAuthProvider.getCredential(it,code)
                addPhoneNumber(credential)
            }

        }

    }

   private val phoneAuthCallBack = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            credential.let {
                addPhoneNumber(it)
            }
            Toast.makeText(this@SignInPhone, "onVerification completed", Toast.LENGTH_SHORT).show()

        }

        override fun onVerificationFailed(exception: FirebaseException) {
            Toast.makeText(this@SignInPhone, exception.message, Toast.LENGTH_LONG).show()
            exception.message?.let { Log.d("aaaaaaaa", it) }
        }

       override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
         //  Toast.makeText(this, "code sent", Toast.LENGTH_SHORT).show()
            super.onCodeSent(verificationId, token)
           this@SignInPhone.verificationId = verificationId

       }


    }

    private fun addPhoneNumber(it: PhoneAuthCredential) {

        mAuth.signInWithCredential (it)
            ?.addOnCompleteListener {

                if (it.isSuccessful){
                    val intent  = Intent(this , MainActivity::class.java )
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    val a = it.exception?.message
                    if (a != null) {
                        Log.d("ss",a)
                    }
                }
            }

    }
}