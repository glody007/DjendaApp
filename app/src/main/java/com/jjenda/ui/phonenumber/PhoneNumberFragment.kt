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
import com.jjenda.R
import com.jjenda.databinding.FragmentPhoneNumberBinding
import com.jjenda.reseau.Repository
import com.jjenda.ui.main.MainFragmentDirections
import com.jjenda.utils.signOut

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
                viewModel.onErrorWhenSendingMessageFinished()
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