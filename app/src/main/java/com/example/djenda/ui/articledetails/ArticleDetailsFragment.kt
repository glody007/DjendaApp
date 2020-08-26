package com.example.djenda.ui.articledetails

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.djenda.R
import com.example.djenda.databinding.FragmentArticleDetailsBinding
import com.example.djenda.ui.SharedArticleViewModel


class ArticleDetailsFragment : Fragment() {

    private lateinit var articleDetailsViewModel: ArticleDetailsViewModel
    private lateinit var sharedArticleViewModel: SharedArticleViewModel
    private lateinit var binding: FragmentArticleDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_details, container, false)
        articleDetailsViewModel = ViewModelProviders.of(this).get(ArticleDetailsViewModel::class.java)
        sharedArticleViewModel  = ViewModelProvider(requireActivity()).get(SharedArticleViewModel::class.java)

        binding.viewModel = articleDetailsViewModel

        sharedArticleViewModel.selectedArticle.observe(viewLifecycleOwner, Observer {
            binding.article = it
        })

        articleDetailsViewModel.eventCall.observe(viewLifecycleOwner, Observer {
            if(it) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:0123456789")
                startActivity(intent)
                articleDetailsViewModel.onCallFinished()
            }
        })

        articleDetailsViewModel.eventSendMessage.observe(viewLifecycleOwner, Observer {
            if(it) {
               val  url = "https://api.whatsapp.com/send?phone=+243997028901"
               val intent = Intent(Intent.ACTION_VIEW)
               intent.data = Uri.parse(url)
               startActivity(intent)
               Log.d("Intent", "com.whatsapp")

                //message()
                articleDetailsViewModel.onSendMessageFinished()
            }
        })

        return binding.root
    }

    private fun message() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("smsto:0123456789")  // This ensures only SMS apps respond
            val text: String = "Bonjour"
            putExtra(Intent.EXTRA_TEXT, text)
            setType("text/plain")
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    fun appInstalledOrNot(url : String) : Boolean {
        val packageManager : PackageManager = requireActivity().packageManager
        var appInstalled : Boolean

        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            appInstalled = true
        } catch (e : PackageManager.NameNotFoundException) {
            appInstalled = false
        }
        return appInstalled
    }
}