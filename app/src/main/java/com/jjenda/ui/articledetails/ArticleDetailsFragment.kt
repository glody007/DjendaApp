package com.jjenda.ui.articledetails

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
import com.jjenda.R
import com.jjenda.databinding.FragmentArticleDetailsBinding
import com.jjenda.reseau.Repository
import com.jjenda.ui.SharedArticleViewModel


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

        sharedArticleViewModel.myLocation.observe(viewLifecycleOwner, Observer {
            val myLocation = it
            sharedArticleViewModel.selectedArticle.observe(viewLifecycleOwner, Observer {
                binding.article = it
                binding.myLocation = myLocation
                articleDetailsViewModel.articleId = it.id
                articleDetailsViewModel.vendeurId = it.vendeurId
                articleDetailsViewModel.getUserInfo()
            })
        })

        articleDetailsViewModel.eventCall.observe(viewLifecycleOwner, Observer {
            if(it) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${articleDetailsViewModel.number}")
                startActivity(intent)
                articleDetailsViewModel.onCallFinished()
            }
        })

        articleDetailsViewModel.eventSendWhatsappMessage.observe(viewLifecycleOwner, Observer {
            if(it) {
               val  url = "https://api.whatsapp.com/send?phone=${articleDetailsViewModel.number}"
               val intent = Intent(Intent.ACTION_VIEW)
               intent.data = Uri.parse(url)
               startActivity(intent)
               Log.d("Intent", "com.whatsapp")

                //message()
                articleDetailsViewModel.onSendWhatsappMessageFinished()
            }
        })

        articleDetailsViewModel.eventSendMessage.observe(viewLifecycleOwner, Observer {
            if(it) {
                sendMessage("")
                articleDetailsViewModel.onSendMessageFinished()
            }
        })

        articleDetailsViewModel.eventShare.observe(viewLifecycleOwner, Observer {
            if(it) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND

                    putExtra(Intent.EXTRA_TEXT, "${Repository.URL_JJENDA}/article/${articleDetailsViewModel.articleId}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "Partager le lien de l'article")
                startActivity(shareIntent)

                articleDetailsViewModel.onShareFinished()
            }
        })

        articleDetailsViewModel.eventErrorWhenGetUserInfo.observe(viewLifecycleOwner, Observer {
            if(it) {
                articleDetailsViewModel.onGetUserInfoFinished()
            }
        })

        articleDetailsViewModel

        return binding.root
    }

    private fun sendMessage(extraText : String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("smsto:${articleDetailsViewModel.number}")  // This ensures only SMS apps respond
            putExtra(Intent.EXTRA_TEXT, extraText)
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