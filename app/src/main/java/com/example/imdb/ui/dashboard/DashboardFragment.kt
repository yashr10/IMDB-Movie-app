package com.example.imdb.ui.dashboard

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imdb.LoginActivity
import com.example.imdb.R
import com.example.imdb.User

import com.example.imdb.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class DashboardFragment : Fragment(),DatePickerDialog.OnDateSetListener {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    private lateinit var name: EditText
    private lateinit var birtDate: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var emailId : EditText

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



        if (currentUser != null){
            binding.newActFragmentHome.isInvisible = true
        }


        binding.newActFragmentHome.setOnClickListener(){

            val loginIntent = Intent(context, LoginActivity::class.java)
            startActivity(loginIntent)

        }

        birtDate.setOnClickListener() {
            showDatePickerDialog()
        }

        if (!currentUser?.displayName.isNullOrEmpty()){
            name.append(currentUser?.displayName.toString())
            name.isFocusable = false
        }
        if (!currentUser?.phoneNumber.isNullOrEmpty()){
            phoneNumber.append(currentUser?.phoneNumber)
            phoneNumber.isFocusable = false
        }
        if (!currentUser?.email.isNullOrEmpty()){
            emailId.append(currentUser?.email)
            emailId.isFocusable = false
        }

        phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!validateMobile(phoneNumber.text.toString())) {
                    phoneNumber.error = "Invalid Mobile Number"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        database = FirebaseDatabase.getInstance().getReference("Users")
        binding.updateDetails.setOnClickListener(){

            val user = User(name.text.toString() ,birtDate.text.toString() , phoneNumber.text.toString() , emailId.text.toString() )

            database.child(name.text.toString()).setValue(user).addOnSuccessListener {

                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
            }
        }





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

        super.onCreateOptionsMenu(menu, inflater)
    }
}