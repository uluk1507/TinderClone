package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tinderclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_login.passwordET as passwordET1

class SignupActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()//ref to the firebaseAuth service
    private val firebaseAuthListener =
        FirebaseAuth.AuthStateListener {
            //check if the user exists
            val user = firebaseAuth.currentUser
            if (user != null) {
                startActivity(MainActivity.newIntent(this))
                finish()
            }
        }///this is basically called when the user created and authenticated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }


    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun onSignup(v: View) {
        if (!emailET.text.toString().isNullOrEmpty() && !passwordET.text.toString().isNullOrEmpty()) {
            //instruction to create the user
            //if this task completes successfully then startActivity takes you to the MainActivity
            firebaseAuth.createUserWithEmailAndPassword(
                emailET.text.toString(),
                passwordET.text.toString()
            ).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "Signup error ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {

        fun newIntent(context: Context?) = Intent(context, SignupActivity::class.java)
    }
}