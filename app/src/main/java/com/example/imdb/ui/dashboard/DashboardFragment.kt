package com.example.imdb.ui.dashboard

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imdb.API.Detail
import com.example.imdb.LoginActivity
import com.example.imdb.R

import com.example.imdb.databinding.FragmentDashboardBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class DashboardFragment : Fragment(),DatePickerDialog.OnDateSetListener {

    private lateinit var mAuth : FirebaseAuth
   /* private lateinit var database : DatabaseReference*/
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    val db = Firebase.firestore

    private lateinit var name: EditText
    private lateinit var birtDate: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var emailId : EditText

    private var nameChecker = false
    private var emailChecker = false
    private var phoneChecker = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)
        name = binding.name
        birtDate = binding.birthdate
        phoneNumber = binding.phoneNumber
        emailId = binding.emailId

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser


        if (currentUser == null){

            binding.name.isInvisible = true
            binding.emailId.isInvisible = true
            binding.phoneNumber.isInvisible = true
            binding.birthdate.isInvisible = true
            binding.updateDetails.isInvisible = true

            binding.newActFragmentHome.setOnClickListener(){

                val loginIntent = Intent(context, LoginActivity::class.java)
                startActivity(loginIntent)

            }

        }else{
            binding.newActFragmentHome.isInvisible = true

            val docRef = db.collection("users").document(currentUser.uid)

            docRef.get().addOnCompleteListener {

                if (it.isSuccessful) {

                    val document = it.result
                    if (document.exists()) {
                        Log.d("document ", "exists")
                        name.setText("")
                        name.append(document.data?.get("name") as CharSequence?)
                        name.isFocusable = false

                        birtDate.setText("")
                        birtDate.append(document.data?.get("birthDate") as CharSequence?)
                        birtDate.isFocusable = false

                        emailId.setText("")
                        emailId.append(document.data?.get("email") as CharSequence?)
                        emailId.isFocusable = false

                        phoneNumber.setText("")
                        phoneNumber.append(document.data?.get("phone number") as CharSequence?)
                        phoneNumber.isFocusable = false

                        binding.updateDetails.isVisible = false
                    } else {

                        Log.d("reached", "here")
                        birtDate.setOnClickListener() {
                            showDatePickerDialog()
                        }

                        if (!currentUser?.displayName.isNullOrEmpty()) {
                            name.append(currentUser?.displayName.toString())
                            name.isFocusable = false
                        }
                        if (!currentUser?.phoneNumber.isNullOrEmpty()) {
                            phoneNumber.append(currentUser?.phoneNumber)
                            phoneNumber.isFocusable = false
                        }
                        if (!currentUser?.email.isNullOrEmpty()) {
                            emailId.append(currentUser?.email)
                            emailId.isFocusable = false
                        }

                        phoneNumber.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (!validateMobile(phoneNumber.text.toString())) {
                                    phoneNumber.error = "Invalid Mobile Number"
                                }
                            }

                            override fun afterTextChanged(s: Editable?) {
                            }
                        })

                        binding.updateDetails.setOnClickListener() {

                            if (name.text.isNullOrEmpty() || birtDate.text.isNullOrEmpty() || emailId.text.isNullOrEmpty() || phoneNumber.text.isNullOrEmpty()) {

                                Toast.makeText(context, "Add all details", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                val user = hashMapOf(
                                    "name" to name.text.toString(),
                                    "birthDate" to birtDate.text.toString(),
                                    "email" to emailId.text.toString(),
                                    "phone number" to phoneNumber.text.toString(),
                                    "favourite" to ArrayList<Detail>()
                                )

                                db.collection("users")
                                    .document(currentUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {

                                        Toast.makeText(context, "Details Updated", Toast.LENGTH_SHORT).show()

                                        phoneNumber.isFocusable = false
                                        emailId.isFocusable = false
                                        birtDate.isFocusable = false
                                        name.isFocusable = false
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                    }
                                /*db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener {

                                        Toast.makeText(context, "Details Updated", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                    }*/


                            }

                        }

                    }
                }


            }
        }








           /* if (firtTime){
                val user = hashMapOf(
                    "name" to name.text.toString(),
                    "birthDate" to birtDate.text.toString(),
                    "email" to emailId.text.toString(),
                    "phone number" to phoneNumber.text.toString()
                )
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener {

                        Toast.makeText(context, "Details Updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                firtTime = false
            }
            else {

                db.collection("users")
                    .get()
                    .addOnCompleteListener {

                        if (it.isSuccessful && !it.result.isEmpty){



                        }

                    }
            }*/


       /* database = FirebaseDatabase.getInstance().getReference("Users")
        binding.updateDetails.setOnClickListener(){

            val user = User(name.text.toString() ,birtDate.text.toString() , phoneNumber.text.toString() , emailId.text.toString() )

            database.child(name.text.toString()).setValue(user).addOnSuccessListener {

                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
            }
        }*/







        return root
    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val date = "$dayOfMonth/${month + 1}/$year"
        birtDate.setText(date)
    }
    private fun validateMobile(input: String): Boolean {
        val p: Pattern = Pattern.compile("[7-9][0-9]{9}")

        val m: Matcher = p.matcher(input)
        return m.matches()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.profile_menu,menu)

        val logout = menu.findItem(R.id.log_out)

       logout.setOnMenuItemClickListener {

           logOut()
           Toast.makeText(context, "LogOut successful", Toast.LENGTH_SHORT).show()
           view?.let { Snackbar.make(it, "log Out" , LENGTH_SHORT).show() }
           true
       }


        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun logOut() {

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser

        Firebase.auth.signOut()
        Toast.makeText(context, "LogOut successful", Toast.LENGTH_SHORT).show()
        view?.let { Snackbar.make(it, "log Out" , LENGTH_SHORT).show() }
        Log.d("user","signed out")
        binding.name.isInvisible = true
        binding.emailId.isInvisible = true
        binding.phoneNumber.isInvisible = true
        binding.birthdate.isInvisible = true
        binding.updateDetails.isInvisible = true
        binding.newActFragmentHome.isVisible = true
    }
}