package com.example.tinderclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.tinderclone.R
import com.example.tinderclone.activities.TinderCallback
import com.example.tinderclone.util.*
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private var callback: TinderCallback? = null

    fun setCallback(callback: TinderCallback) {
        this.callback = callback

        userId = callback.ongGetUserId()
        userDatabase = callback.getUserDatabase().child(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileLayout.setOnTouchListener { view, event -> true }//this will stop click registration while loading - the progress bar loading

        populateInfo()

        applyButton.setOnClickListener { onApply() }
        signoutButton.setOnClickListener { callback?.onSignout() }
    }

    private fun onApply() {

        //-1 means no button is checked at all
        if (nameET.text.toString().isNullOrEmpty() ||
            emailET.text.toString().isNullOrEmpty() ||
            radioGroup1.checkedRadioButtonId == -1 ||
            radioGroup2.checkedRadioButtonId == -1
        ) {
            Toast.makeText(context, getString(R.string.error_profile_incomplete), Toast.LENGTH_SHORT).show()
        } else {
            val name = nameET.text.toString()
            val age = ageET.text.toString()
            val email = emailET.text.toString()
            val gender =
                if (radioMan1.isChecked) GENDER_MALE
                else GENDER_FEMALE
            val preferredGender =
                if (radioMan2.isChecked) GENDER_MALE
                else GENDER_FEMALE
            userDatabase.child(DATA_NAME).setValue(name)
            userDatabase.child(DATA_AGE).setValue(age)
            userDatabase.child(DATA_EMAIL).setValue(email)
            userDatabase.child(DATA_GENDER).setValue(gender)
            userDatabase.child(DATA_GENDER_PREFERRENCE).setValue(preferredGender)
            callback?.profileComplete()

        }


    }

    fun populateInfo() {
        progressLayout.visibility = View.VISIBLE

        userDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    val user = snapshot.getValue(User::class.java)
                    nameET.setText(user?.name, TextView.BufferType.EDITABLE)
                    emailET.setText(user?.email, TextView.BufferType.EDITABLE)
                    ageET.setText(user?.age, TextView.BufferType.EDITABLE)

                    if (user?.gender == GENDER_MALE) {
                        radioMan1.isChecked = true
                    }
                    if (user?.gender == GENDER_FEMALE) {
                        radioWoman1.isChecked = true
                    }
                    if (user?.preferredGender == GENDER_MALE) {
                        radioMan2.isChecked = true
                    }
                    if (user?.preferredGender == GENDER_FEMALE) {
                        radioWoman2.isChecked = true
                    }

                    progressLayout.visibility = View.GONE

                }
            }

        })


    }


}