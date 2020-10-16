package com.jjenda.ui.payment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.DialogPaymentBinding
import com.jjenda.ui.avertissementpostrestant.PostRestantViewModel
import com.segment.analytics.Analytics

class PaymentDialog : DialogFragment() {

    internal lateinit var listener: PaymentDialogListener

    interface PaymentDialogListener {
        fun onDialogAirtelMoneyClick()
        fun onDialogMPesaClick()
        fun onDialogOrangeMoneyClick()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = childFragment as PaymentDialogListener

        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((childFragment.toString() +
                    " must implement PaymentDialogListener"))
        }
    }

    lateinit var binding: DialogPaymentBinding
    lateinit var viewModel: PaymentViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Analytics.with(requireContext()).screen("Payment")
        return activity?.let {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.dialog_payment, null, false)
            viewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
            viewModel.listener = listener
            binding.viewModel = viewModel

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}