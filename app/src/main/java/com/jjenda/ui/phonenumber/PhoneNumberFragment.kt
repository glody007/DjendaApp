package com.jjenda.ui.phonenumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.FragmentPhoneNumberBinding

class PhoneNumberFragment : Fragment() {

    lateinit var binding : FragmentPhoneNumberBinding
    lateinit var viewModel : PhoneNumberViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_phone_number, container, false)
        viewModel = ViewModelProviders.of(this).get(PhoneNumberViewModel::class.java)

        return binding.root
    }

}