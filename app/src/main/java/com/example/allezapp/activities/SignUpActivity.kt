package com.example.allezapp.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.allezapp.R
import com.example.allezapp.R.layout.activity_sign_up
import com.example.allezapp.firebase.FirestoreClass
import com.example.allezapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_sign_up)
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
        var signUpBtn = findViewById<Button>(R.id.btn_sign_up)

            signUpBtn.setOnClickListener {
            registerUser()
        }
    }
    private fun setupActionBar() {

        var toolBar = findViewById<Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(findViewById(R.id.toolbar_sign_up_activity))

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        // Here we get the text from editText and trim the space
        val name: String = findViewById<TextView>(R.id.et_name).text.toString().trim { it <= ' ' }
        val email: String = findViewById<TextView>(R.id.et_email).text.toString().trim { it <= ' ' }
        val password: String = findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                // If the registration is successfully done
                                if (task.isSuccessful) {

                                    // Firebase registered user
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    // Registered Email
                                    val registeredEmail = firebaseUser.email!!

                                    val user = User(
                                            firebaseUser.uid, name, registeredEmail
                                    )

                                    // call the registerUser function of FirestoreClass to make an entry in the database.
                                    FirestoreClass().registerUser(this@SignUpActivity, user)
                                } else {
                                    Toast.makeText(
                                            this@SignUpActivity,
                                            task.exception!!.message,
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
        }
    }

    private fun validateForm (name: String, email: String, password: String): Boolean{

        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("please enter a name ")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("please enter an email ")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("please enter a password ")
                false
            }else->{
                true
            }

        }
    }
    fun userRegisteredSuccess() {

        Toast.makeText(
                this@SignUpActivity,
                "You have successfully registered.",
                Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        finish()
    }
 }

