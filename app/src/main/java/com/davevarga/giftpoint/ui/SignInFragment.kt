package com.davevarga.giftpoint.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.davevarga.giftpoint.R
import com.davevarga.giftpoint.databinding.SignInScreenBinding
import com.davevarga.giftpoint.di.DaggerAppComponent
import com.davevarga.giftpoint.viewmodels.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import javax.inject.Inject

class SignInFragment : BaseFragment<SignInScreenBinding>() {

    companion object {
        private const val SIGN_IN = 100
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: SignInViewModel


    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configure Google Sign In
        DaggerAppComponent.create().inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(SignInViewModel::class.java)
        val user = viewModel.getUser()
        if (viewModel.getUser() != null) {
            Log.i("SignInFragment", user?.displayName.toString())
            findNavController().navigate(R.id.action_signInFragment_to_homeScreenFragment)
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        //mAuth = FirebaseAuth.getInstance()

        binding.signInBtn.setOnClickListener {
            signIn()
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                    // ...
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = getCredential(idToken, null)
        viewModel.getMAuth().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("SignInActivity", "signInWithCredential:success")
                    findNavController().navigate(R.id.action_signInFragment_to_homeScreenFragment)
                } else {
                    Log.w("SignInActivity", "signInWithCredential:failure", task.exception)

                }

            }
    }

    override fun getFragmentView() = R.layout.sign_in_screen
//
//    override fun getViewModel() = SignInViewModel::class.java
}