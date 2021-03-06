package com.jjenda.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jjenda.R
import com.jjenda.databinding.FragmentMainBinding
import com.jjenda.reseau.Repository
import com.jjenda.ui.articleenvente.ArticlesEnVenteFragment
import com.jjenda.ui.mesarticles.MesArticlesFragment
import com.jjenda.utils.signOut

class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var viewPager: ViewPager2
    lateinit var articlesPagerAdapter: ArticlesPagerAdapter
    lateinit var viewModel : MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.btnAjouterArticle.setOnClickListener {
            Navigation.findNavController(it).navigate(MainFragmentDirections.actionArticlesFragmentToPrendrePhotoFragment())
        }

        binding.btnShareBoutique.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND

                putExtra(Intent.EXTRA_TEXT, "${Repository.URL_JJENDA}/vendeur/${Repository.getInstance().prefUserid}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Partager le lien de votre boutique")
            startActivity(shareIntent)
        }

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

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
    }

    private fun signOut() {
      signOut(requireActivity()).addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                    Repository.getInstance().clearCache()
                    Navigation.findNavController(binding.root)
                            .navigate(MainFragmentDirections.actionArticlesFragmentToLoginFragment())
                })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.loginFragment) {
            signOut()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
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