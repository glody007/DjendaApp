package com.jjenda.ui.avertissementpostrestant

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jjenda.R
import com.jjenda.databinding.DialogPostRestantBinding

class PostRestantDialog(val nombrePostsRestant: Int) : DialogFragment() {

    internal lateinit var listener: PostRestantDialogListener

    interface PostRestantDialogListener {
        fun onDialogPostClick()
        fun onDialogTarifClick()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = childFragment as PostRestantDialogListener

        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((childFragment.toString() +
                    " must implement PostRestantDialogListener"))
        }
    }

    lateinit var binding: DialogPostRestantBinding
    lateinit var viewModel: PostRestantViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                            R.layout.dialog_post_restant, null, false)
            viewModel = ViewModelProviders.of(this).get(PostRestantViewModel::class.java)
            viewModel.nombrePostsRestant = nombrePostsRestant
            viewModel.listener = listener
            binding.viewModel = viewModel

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}