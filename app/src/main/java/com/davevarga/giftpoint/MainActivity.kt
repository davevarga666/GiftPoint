package com.davevarga.giftpoint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davevarga.giftpoint.ui.HomeScreenFragment
import com.davevarga.giftpoint.ui.SignInFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        mAuth = FirebaseAuth.getInstance()
//        val user = mAuth.currentUser
//
//        if (user != null) {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.homeScreenFragment, HomeScreenFragment())
//
//        } else {
//            supportFragmentManager.beginTransaction().add(R.id.signInFragment, SignInFragment())
////                finish()
//        }
    }
}