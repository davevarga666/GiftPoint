package com.davevarga.giftpoint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.davevarga.giftpoint.ui.HomeScreenFragment
import com.davevarga.giftpoint.ui.SignInFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}