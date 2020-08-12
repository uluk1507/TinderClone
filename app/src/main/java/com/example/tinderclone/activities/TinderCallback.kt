package com.example.tinderclone.activities

import com.google.firebase.database.DatabaseReference

interface TinderCallback {

    fun onSignout()
    fun ongGetUserId():String
    fun getUserDatabase():DatabaseReference
    fun profileComplete()

}