package com.jjenda.ui.plans

import android.content.Intent
import android.net.Uri
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
            plan -> fragmentManager?.let {
                    viewModel.forfait = plan.type
                    dialog = PaymentDialog()
                    dialog.onAttachFragment(this)
                    dialog.show(it, "Moyen de paiment")
                }
        })
        binding.planList.adapter = adapter
        binding.viewModel = viewModel

        viewModel.plans.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.plans = it
            }
        })

        viewModel.getPlans()

        return binding.root
    }

    override fun onDialogAirtelMoneyClick() {
        dialog.dismiss()
        payementMessage("Airtel Money")
    }

    override fun onDialogMPesaClick() {
        dialog.dismiss()
        payementMessage("M-Pesa")
    }

    override fun onDialogOrangeMoneyClick() {
        dialog.dismiss()
        payementMessage("Orange Money")
    }

    fun payementMessage(provider : String) {
        val urlEncodedText = "Demande%20de%20payement%20du%20forfait%20${viewModel.forfait}%20via%20${provider}"
        val  url = "https://api.whatsapp.com/send?phone=+243997028901&text=${urlEncodedText}"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}