package com.example.djenda.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.djenda.MainActivity
import com.example.djenda.R
import com.example.djenda.databinding.FragmentLoginBinding
import com.example.djenda.reseau.Repository
import com.example.djenda.reseau.Verification
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Boolean

class LoginFragment : Fragment() {


    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var binding : FragmentLoginBinding

    companion object {
        const private val TAG = "Erreur connection"
        const val RC_SIGN_IN = 42
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //      .requestEmail()
                //.requestIdToken(getString(R.string.google_client_id))
                //.build()

        // Build a GoogleSignInClient with the options specified by gso.

        // Build a GoogleSignInClient with the options specified by gso.
        //mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.btnConnection.setOnClickListener { signIn(it) }

        return binding.root
    }

    private fun signIn(view : View) {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_articlesFragment)
        //val signInIntent = mGoogleSignInClient?.signInIntent
        //startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Repository.getInstance().verify_oauth_token(account!!.idToken, object : Callback<Verification> {
                override fun onResponse(call: Call<Verification>, response: Response<Verification>) {
                    Log.d("Verification", Boolean.toString(response.body()!!.verify))
                }

                override fun onFailure(call: Call<Verification>, t: Throwable) {
                    // Log error here since request failed
                }
            })
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LoginFragment.TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        //updateUI(account)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}