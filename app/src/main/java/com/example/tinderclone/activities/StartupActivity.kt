package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tinderclone.R

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

    }


    //when this view is press start the activity that is LoginActivity with the intent
    //provided here
    fun onLogin(v: View) {
        startActivity(LoginActivity.newIntent(this))

    }

    fun onSignup(v: View) {
        startActivity(SignupActivity.newIntent(this))

    }

    companion object {

        fun newIntent(context: Context?) = Intent(context, StartupActivity::class.java)
    }

}