package com.example.imdb

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imdb.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private lateinit var mAuth : FirebaseAuth
@SuppressLint("StaticFieldLeak")
private lateinit var binding : ActivityLoginBinding
@SuppressLint("StaticFieldLeak")
private lateinit var googleSignInClient  : GoogleSignInClient
private const val RC_SIGN_IN = 120
private lateinit var firebaseAuth: FirebaseAuth
class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        firebaseAuth  = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_clientt_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.toSignupActLoginActivity.setOnClickListener(){

            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)

        }

        binding.loginbuttonLoginActivity.setOnClickListener(){

            val email = binding.emailLoginActivity.text.toString().trim()
            val password = binding.passwordLoginActivity.text.toString().trim()

            if (email.isEmpty()){
                binding.emailLoginActivity.error = "Email Required"
                binding.emailLoginActivity.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailLoginActivity.error = "Valid Email Required"
                binding.emailLoginActivity.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                binding.passwordLoginActivity.error = "Minimum 6 character password required"
                binding.passwordLoginActivity.requestFocus()
                return@setOnClickListener
            }
            
            loginUser(email,password)

        }
        binding.toPhoneAct.setOnClickListener(){
            val intent = Intent(this, SignInPhone::class.java).apply {

            }

            startActivity(intent)

        }
        binding.signInGoogle.setOnClickListener(){

            val currentUser = firebaseAuth.currentUser
            updateUI(currentUser)
            /*resultLauncher
                .launch(Intent(googleSignInClient.signInIntent))*/
            signIn()
        }
    }
    private fun loginUser(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val dashboardIntent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    Toast.makeText(this, "LogIn Successful", Toast.LENGTH_SHORT).show()
                    startActivity(dashboardIntent)
                }
                else{
                    Toast.makeText(this,it.exception?.message , Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
      /*  mAuth.currentUser?.let {

        }*/
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.d("SIGN IN","started")
    }

   /* private val resultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {

            if (it.resultCode == Activity.RESULT_OK) {

                val intent: Intent? = it.data

                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                Log.d("task", "started")

                if (task.isSuccessful) {
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d("google", "firebaseAuthWithGoogle:" + account.id)
                        firebaseAuthWithGoogle(account.idToken!!)
                    } catch (e: ApiException) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("google", "Google sign in failed", e)
                    }


                }
            }
        })*/

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("onActivityResult","started")
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("task","started")

            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("google", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                     Log.w("google", "Google sign in failed", e)
                }
            }else{

                Log.w("google" ,task.exception.toString())
            }


        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ON ACTIVITY RESULT 1","REACHED")
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("ON ACTIVITY RESULT 2","REACHED")
            val task: Task<GoogleSignInAccount>  = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){

        try {
            val user = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(user.idToken)
            Toast.makeText(this, user.displayName, Toast.LENGTH_SHORT).show()
        }catch (e:ApiException){
            Log.d("Message",e.toString())
            Toast.makeText(this, "this", Toast.LENGTH_SHORT).show()
        }

    }





   private fun firebaseAuthWithGoogle(idToken: String) {
       Log.d("firebase google", "signInWithCredential:success")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d("firebase google", "signInWithCredential:success")

                    updateUI(firebaseAuth.currentUser)

                    Toast.makeText(this, "LogIn Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    signOut()

                } else {
                    Log.w("firebase google", "signInWithCredential:failure", task.exception)

                    updateUI(null)
                }
            }
    }

    private fun signOut() {
        googleSignInClient.signOut()
    }

    private fun updateUI (user : FirebaseUser?){}
}