package com.github.manideeplanka.firebasefun.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.manideeplanka.firebasefun.R
import com.github.manideeplanka.firebasefun.models.Person
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_profile.*

class NewProfileActivity : AppCompatActivity() {
    private lateinit var firestore :FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_profile)

        submit_btn.setOnClickListener {
            val person = Person(
                name_et.text.toString(),
                age_et.text.toString().toInt(),
                address_et.text.toString()
            )
            Log.d("NewProfile","person : $person")
            firestore.collection("users").add(person)

            finish()
        }
        initFirestore()
    }

    private fun initFirestore() {
        firestore = FirebaseFirestore.getInstance()
    }
}
