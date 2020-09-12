package com.jjenda.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    lateinit var binding: FragmentPaymentBinding
    lateinit var viewModel: PaymentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)

        return binding.root
    }
}