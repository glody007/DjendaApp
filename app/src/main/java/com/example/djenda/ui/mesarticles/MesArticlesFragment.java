package com.example.djenda.ui.mesarticles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djenda.ArticlesAdapter;
import com.example.djenda.databinding.FragmentMesArticlesBinding;
import com.example.djenda.reseau.Article;
import com.example.djenda.R;
import com.example.djenda.ui.main.ArticlesFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;



public class MesArticlesFragment extends Fragment implements
        ArticlesAdapter.ArticlesAdapterOnClickHandler {

    private RecyclerView reclyclerView;
    private ArticlesAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MesArticlesViewModel mesArticlesViewModel;
    private FragmentMesArticlesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mes_articles, container, false);

        reclyclerView = (RecyclerView) binding.recyclerviewMesArticles;
        reclyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        reclyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesAdapter(this);
        reclyclerView.setAdapter(mAdapter);

        mesArticlesViewModel = new ViewModelProvider(this).get(MesArticlesViewModel.class);

        binding.setViewModel(mesArticlesViewModel);

        mesArticlesViewModel.getUserArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
                mesArticlesViewModel.showArticles();
            }
        });

        mesArticlesViewModel.eventErrorDownloadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if(error) {
                    Snackbar.make(binding.getRoot(), R.string.connection_problem_message, Snackbar.LENGTH_LONG).show();
                    mesArticlesViewModel.showErrorDownload();
                    mesArticlesViewModel.onErrorDownloadArticlesFinished();
                }
            }
        });

        mesArticlesViewModel.getNavigateToArticleDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if(navigate) {
                    Navigation.findNavController(binding.getRoot()).navigate(ArticlesFragmentDirections
                            .actionArticlesFragmentToArticleDetailsFragment());
                    mesArticlesViewModel.onArticleDetailsNavigated();
                }

            }
        });

        mesArticlesViewModel.eventLoadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean load) {
                if(load) {
                    mesArticlesViewModel.showLoading();
                    mesArticlesViewModel.onLoadArticlesFinished();
                }
            }
        });

        return binding.getRoot();
    }



    @Override
    public void onClick(Article article) {
        Toast.makeText(getContext(), article.getNom(), Toast.LENGTH_LONG).show();
    }

}

