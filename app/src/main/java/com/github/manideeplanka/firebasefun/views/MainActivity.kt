package com.github.manideeplanka.firebasefun.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.github.manideeplanka.firebasefun.R
import com.github.manideeplanka.firebasefun.adapters.ProfileAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mFirestore : FirebaseFirestore
    lateinit var mQuery: Query
    lateinit var mAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        new_profile_btn.setOnClickListener {
            val intent = Intent(this, NewProfileActivity::class.java)
            startActivity(intent)
        }

        initFireStore()

        user_profile_list.layoutManager = LinearLayoutManager(this)
        mAdapter = ProfileAdapter(mQuery)
        user_profile_list.adapter = mAdapter

        val itemDecor = DividerItemDecoration(this, VERTICAL)
        user_profile_list.addItemDecoration(itemDecor)
    }

    fun initFireStore() {
        mFirestore = FirebaseFirestore.getInstance()
        mQuery = mFirestore.collection("users").orderBy("name", Query.Direction.DESCENDING).limit(10L)
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

}
