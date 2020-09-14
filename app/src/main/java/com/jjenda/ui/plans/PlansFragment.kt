package com.jjenda.ui.plans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jjenda.PlanAdapter
import com.jjenda.PlanListener
import com.jjenda.R
import com.jjenda.databinding.FragmentPlansBinding
import com.jjenda.ui.payment.PaymentDialog

class PlansFragment : Fragment(), PaymentDialog.PaymentDialogListener {

    lateinit var binding: FragmentPlansBinding
    lateinit var viewModel: PlansViewModel
    lateinit var dialog: PaymentDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plans, container, false)
        viewModel = ViewModelProviders.of(this).get(PlansViewModel::class.java)

        val adapter = PlanAdapter(PlanListener {
            type -> fragmentManager?.let {
                    dialog = PaymentDialog()
                    dialog.onAttachFragment(this)
                    dialog.show(it, "moyen de paiment")
                }
        })
        binding.planList.adapter = adapter
        binding.viewModel = viewModel

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.plans = it
            }
        })

        return binding.root
    }

    override fun onDialogAirtelMoneyClick() {
        dialog.dismiss()
        Toast.makeText(context, "Airtel Money", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogMPesaClick() {
        dialog.dismiss()
        Toast.makeText(context, "M-Pesa", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogOrangeMoneyClick() {
        dialog.dismiss()
        Toast.makeText(context, "Orange Money", Toast.LENGTH_SHORT).show()
    }

}