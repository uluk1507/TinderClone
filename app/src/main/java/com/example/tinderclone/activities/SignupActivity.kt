package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tinderclone.R
import com.example.tinderclone.util.DATA_USERS
import com.example.tinderclone.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference

    private val firebaseAuth = FirebaseAuth.getInstance()//ref to the firebaseAuth service
    private val firebaseAuthListener =
        FirebaseAuth.AuthStateListener {
            //check if the user exists
            val user = firebaseAuth.currentUser
            if (user != null) {
                startActivity(TinderActivity.newIntent(this))
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
            firebaseAuth.createUserWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this, "Signup error ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } else {
                        val email = emailET.text.toString()
                        val userId = firebaseAuth.currentUser?.uid ?: ""//gets unique identifier or empty string
                        val user = User(userId, "", "", email, "", "")
                        firebaseDatabase.child(DATA_USERS).child(userId).setValue(user)
                    }
                }
        }
    }

    companion object {

        fun newIntent(context: Context?) = Intent(context, SignupActivity::class.java)
    }
}