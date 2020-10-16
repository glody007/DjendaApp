package com.jjenda.ui.phonenumber

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.jjenda.R
import com.jjenda.databinding.FragmentPhoneNumberBinding
import com.jjenda.reseau.Repository
import com.jjenda.ui.main.MainFragmentDirections
import com.jjenda.utils.signOut
import com.segment.analytics.Analytics
import com.segment.analytics.Traits

class PhoneNumberFragment: Fragment() {

    lateinit var binding : FragmentPhoneNumberBinding
    lateinit var viewModel : PhoneNumberViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false)
        viewModel = ViewModelProviders.of(this).get(PhoneNumberViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.eventSendMessage.observe(viewLifecycleOwner, Observer {
            if(it) {
                disableForm()
                viewModel.sendVerificationMessage(requireContext())
                viewModel.onSendMessageFinished()
            }
        })

        viewModel.eventRegisterNumber.observe(viewLifecycleOwner, Observer {
            if(it) {
                disableForm()
                viewModel.registerNumber()
                viewModel.onRegisterPhoneNumberFinished()
            }
        })

        viewModel.eventNavigateToArticles.observe(viewLifecycleOwner, Observer {
            if(it) {
                Analytics.with(requireContext()).identify(Traits().putPhone(viewModel.phoneNumber))
                Analytics.with(requireContext()).track("Signup finished")

                Repository.getInstance().navigatedToPhoneNumber = false
                Navigation.findNavController(binding.root)
                        .navigate(PhoneNumberFragmentDirections
                                .actionPhoneNumberFragmentToArticlesFragment())
                viewModel.onNavigateToArticlesFinished()
            }
        })

        viewModel.eventErrorWhenSendingMessage.observe(viewLifecycleOwner, Observer {
            if(it) {
                enableForm()
                Snackbar.make(binding.root, R.string.connection_problem_message, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorWhenSendingMessageFinished()
            }
        })

        viewModel.eventNumberInvalide.observe(viewLifecycleOwner, Observer {
            if(it) {
                binding.tvNumberInvalide.visibility = View.VISIBLE
                enableForm()
                viewModel.onNumberInvalideFinished()
            }
        })

        viewModel.eventNavigateToCodeValidation.observe(viewLifecycleOwner, Observer {
            if(it) {
                Navigation.findNavController(binding.root)
                        .navigate(PhoneNumberFragmentDirections
                                .actionPhoneNumberFragmentToValidationCodeFragment())
                viewModel.onNavigateToCodeValidationFinished()
            }
        })

        viewModel.eventCodeNotSended.observe(viewLifecycleOwner, Observer {
            if(it) {
                enableForm()
                viewModel.onCodeNotSendedFinished()
            }
        })

        Repository.getInstance().navigatedToPhoneNumber = true
        binding.tvNumberInvalide.visibility = View.GONE

        Analytics.with(requireContext()).screen("Phone number")

        return binding.root
    }

    fun disableForm() {
        binding.btnSubmit.isEnabled = false
        binding.ilNumber.isEnabled = false
    }

    fun enableForm() {
        binding.btnSubmit.isEnabled = true
        binding.ilNumber.isEnabled = true
    }
}