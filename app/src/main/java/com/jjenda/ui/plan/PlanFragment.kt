package com.jjenda.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.FragmentPlanBinding

class PlanFragment : Fragment() {

    lateinit var binding: FragmentPlanBinding
    lateinit var viewModel: PlanViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false)
        viewModel = ViewModelProviders.of(this).get(PlanViewModel::class.java)

        return binding.root
    }

}