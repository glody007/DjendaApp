package com.jjenda.ui.validationcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.FragmentValidationCodeBinding

class ValidationCodeFragment : Fragment() {

    lateinit var binding : FragmentValidationCodeBinding
    lateinit var viewModel : ValidationCodeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_validation_code, container, false)
        viewModel = ViewModelProviders.of(this).get(ValidationCodeViewModel::class.java)

        return binding.root
    }

}