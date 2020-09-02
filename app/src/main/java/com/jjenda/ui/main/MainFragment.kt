package com.jjenda.ui.main

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jjenda.R
import com.jjenda.databinding.FragmentMainBinding
import com.jjenda.ui.articleenvente.ArticlesEnVenteFragment
import com.jjenda.ui.mesarticles.MesArticlesFragment


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
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val googleSignInClient : GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), OnCompleteListener<Void?> {
                    PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
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