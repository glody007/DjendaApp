package com.example.djenda.ui.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.djenda.R
import com.example.djenda.databinding.FragmentMainBinding
import com.example.djenda.reseau.Repository
import com.example.djenda.ui.ajouterdetailsarticle.AjouterDetailsArticleFragmentArgs
import com.example.djenda.ui.articleenvente.ArticlesEnVenteFragment
import com.example.djenda.ui.mesarticles.MesArticlesFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ArticlesFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var viewPager: ViewPager2
    lateinit var articlesPagerAdapter: ArticlesPagerAdapter
    lateinit var viewModel : ArticlesViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.btnAjouterArticle.setOnClickListener {
            Navigation.findNavController(it).navigate(ArticlesFragmentDirections.actionArticlesFragmentToPrendrePhotoFragment())
        }

        viewModel = ViewModelProviders.of(this).get(ArticlesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        articlesPagerAdapter = ArticlesPagerAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = articlesPagerAdapter

        val tabLayout : TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Articles"
                else -> "Mes articles"
            }
        }.attach()

        arguments?.let {
            val args = ArticlesFragmentArgs.fromBundle(it)
            if(args.articlePosted) {
                Snackbar.make(binding.root, R.string.article_posted_message, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    class ArticlesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            // Return a NEW fragment instance in createFragment(int)
            return when(position) {
                0 -> ArticlesEnVenteFragment()
                else -> MesArticlesFragment()
            }

        }
    }

}