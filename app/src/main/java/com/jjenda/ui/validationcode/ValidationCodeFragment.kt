package com.jjenda.ui.validationcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.jjenda.R
import com.jjenda.databinding.FragmentValidationCodeBinding
import com.jjenda.ui.ajouterdetailsarticle.AjouterDetailsArticleFragmentDirections

class ValidationCodeFragment : Fragment() {

    lateinit var binding : FragmentValidationCodeBinding
    lateinit var viewModel : ValidationCodeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_validation_code, container, false)
        viewModel = ViewModelProviders.of(this).get(ValidationCodeViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.eventSendCode.observe(viewLifecycleOwner, Observer {
            if(it) {
                disableForm()
                viewModel.checkVerificationCode()
                viewModel.onSendMessageFinished()
            }
        })

        viewModel.eventErrorWhenCheckingCode.observe(viewLifecycleOwner, Observer {
            if(it) {
                enableForm()
                viewModel.onErrorWhenSendingCodeFinished()
            }
        })

        viewModel.eventFalseValidationCode.observe(viewLifecycleOwner, Observer {
            if(it) {
                enableForm()
                viewModel.onFalseValidationCodeFinished()
            }
        })

        viewModel.eventNavigateToArticles.observe(viewLifecycleOwner, Observer {
            Navigation.findNavController(binding.root)
                    .navigate(ValidationCodeFragmentDirections
                            .actionValidationCodeFragmentToArticlesFragment())
            viewModel.onNavigateToArticlesFinished()
        })

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