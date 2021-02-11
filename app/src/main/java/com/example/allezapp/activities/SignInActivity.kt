package com.example.allezapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R
import com.example.allezapp.R.layout.activity_sign_in
import com.example.allezapp.firebase.FirestoreClass
import com.example.allezapp.models.User
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class SignInActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_sign_in)
        setupActionBar()





        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        var signUpBtn = findViewById<Button>(R.id.btn_sign_in)

            signUpBtn.setOnClickListener{
                signInRegisteredUser()
            }

    }
    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_sign_in_activity)
        setSupportActionBar(findViewById(R.id.toolbar_sign_in_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = findViewById<TextView>(R.id.et_email_sign_in).text.toString().trim { it <= ' ' }
        val password: String = findViewById<TextView>(R.id.et_password_sign_in).text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // TODO (Step 2: Remove the toast message and call the FirestoreClass signInUser function to get the data of user from database. And also move the code of hiding Progress Dialog and Launching MainActivity to Success function.)
                            // Calling the FirestoreClass signInUser function to get the data of user from database.
                            FirestoreClass().loadUserData(this@SignInActivity)
                            // END
                        } else {
                            Toast.makeText(
                                    this@SignInActivity,
                                    task.exception!!.message,
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }
    }


    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }
    fun signInSuccess(user: User?) {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }
}



