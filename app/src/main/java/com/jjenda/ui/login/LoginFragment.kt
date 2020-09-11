package com.jjenda.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jjenda.R
import com.jjenda.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.jjenda.reseau.Repository
import com.jjenda.utils.signOut

class LoginFragment : Fragment() {


    var mGoogleSignInClient: GoogleSignInClient? = null
    lateinit var binding : FragmentLoginBinding
    lateinit var viewModel: LoginViewModel

    companion object {
        const private val TAG = "Erreur connection"
        const val RC_SIGN_IN = 42
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.viewModel = viewModel

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                     .requestEmail()
                     .requestIdToken(getString(R.string.google_client_id))
                     .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.btnConnection.setOnClickListener { signIn(it) }

        viewModel.eventNavigateToArticlesFragment.observe(viewLifecycleOwner, Observer {
            if(it) {
                Navigation.findNavController(binding.root)
                        .navigate(LoginFragmentDirections.actionLoginFragmentToArticlesFragment())
                viewModel.onNavigateToArticlesFragmentFinished()
            }
        })

        viewModel.eventNavigateToPhoneNumberFragment.observe(viewLifecycleOwner, Observer {
            if(it) {
                Navigation.findNavController(binding.root)
                        .navigate(LoginFragmentDirections.actionLoginFragmentToPhoneNumberFragment())
                viewModel.onNavigateToPhoneNumberFragmentFinished()
            }
        })

        viewModel.eventErrorWhenVerifyIfUserHasPhoneNumber.observe(viewLifecycleOwner, Observer {
            if(it) {
                signOut()
                Snackbar.make(binding.root, R.string.connection_problem_message, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorWhenVerifyIfUserHasPhoneNumberFinished()
            }
        })

        viewModel.eventErrorWhenVerifyOAuthToken.observe(viewLifecycleOwner, Observer {
            if(it) {
                signOut()
                Snackbar.make(binding.root, R.string.connection_problem_message, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorWhenVerifyOAuthTokenFinished()
            }
        })

        viewModel.eventBadIdToken.observe(viewLifecycleOwner, Observer {
            if(it) {
                signOut()
                viewModel.onBadIdTokenFinished()
            }
        })

        return binding.root
    }

    private fun signOut() {
        signOut(requireActivity())
        viewModel.loading.set(false)
    }

    private fun signIn(view : View) {
        viewModel.loading.set(true)
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            viewModel.verifyIfUserHasPhoneNumber()
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
            viewModel.verify_oauth_token(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LoginFragment.TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    override fun onStart() {
        super.onStart()

        if(Repository.getInstance().navigatedToPhoneNumber) {
            signOut()
            Repository.getInstance().navigatedToPhoneNumber = false
        }
        else {
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            val account = GoogleSignIn.getLastSignedInAccount(requireContext())
            updateUI(account)
        }
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