package com.jjenda.ui.avertissementpostrestant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.FragmentAvertissementPostRestantBinding

class AvertissementPostRestantFragment : Fragment() {

    lateinit var binding: FragmentAvertissementPostRestantBinding
    lateinit var viewModel: AvertissementPostRestantViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_avertissement_post_restant,
                                          container, false)
        viewModel = ViewModelProviders.of(this).get(AvertissementPostRestantViewModel::class.java)

        return binding.root
    }
}