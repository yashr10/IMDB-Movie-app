package com.example.imdb.Loginfeatures

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.imdb.MainActivity
import com.example.imdb.databinding.ActivitySignUpBinding
import com.example.imdb.ui.dashboard.DashboardFragment
import com.google.firebase.auth.FirebaseAuth

private lateinit var binding : ActivitySignUpBinding
private lateinit var mAuth: FirebaseAuth
class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.signupSignupActivity.setOnClickListener(){

            val email = binding.emailSignUpActivity.text.toString().trim()
            val password = binding.passwordSignUpActivity.text.toString().trim()

            if (email.isEmpty()){
                binding.emailSignUpActivity.error = "Email Required"
                binding.emailSignUpActivity.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailSignUpActivity.error = "Valid Email Required"
                binding.emailSignUpActivity.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                binding.passwordSignUpActivity.error = "Minimum 6 character password required"
                binding.passwordSignUpActivity.requestFocus()
                return@setOnClickListener
            }
            registerUser(email, password)

        }


    }

    private fun registerUser(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {

                if (it.isSuccessful){
                    val dashboardIntent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(dashboardIntent)
                }
                else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }



    }
}