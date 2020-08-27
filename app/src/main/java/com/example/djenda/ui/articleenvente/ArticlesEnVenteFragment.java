package com.example.djenda.ui.articleenvente;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djenda.ArticlesAdapter;
import com.example.djenda.databinding.FragmentArticlesEnVenteBinding;
import com.example.djenda.reseau.Article;

import com.example.djenda.R;
import com.example.djenda.ui.SharedArticleViewModel;
import com.example.djenda.ui.main.ArticlesFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class ArticlesEnVenteFragment extends Fragment implements
        ArticlesAdapter.ArticlesAdapterOnClickHandler {

    private ArticlesAdapter mAdapter;
    private ArticlesEnVenteViewModel articlesEnVenteViewModel;
    private SharedArticleViewModel sharedArticleViewModel;
    private FragmentArticlesEnVenteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles_en_vente, container, false);

        RecyclerView recyclerView = (RecyclerView) binding.recyclerviewArticlesEnVente;
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        articlesEnVenteViewModel = new ViewModelProvider(this).get(ArticlesEnVenteViewModel.class);
        sharedArticleViewModel = new ViewModelProvider(requireActivity()).get(SharedArticleViewModel.class);

        binding.setViewModel(articlesEnVenteViewModel);

        articlesEnVenteViewModel.getArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
                articlesEnVenteViewModel.showArticles();
            }
        });

        articlesEnVenteViewModel.eventLoadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean load) {
                if(load) {
                    articlesEnVenteViewModel.showLoading();
                    articlesEnVenteViewModel.onLoadArticlesFinished();
                }
            }
        });

        articlesEnVenteViewModel.getNavigateToArticleDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if(navigate) {
                    Navigation.findNavController(binding.getRoot()).navigate(ArticlesFragmentDirections
                            .actionArticlesFragmentToArticleDetailsFragment());
                    articlesEnVenteViewModel.onArticleDetailsNavigated();
                }

            }
        });

        articlesEnVenteViewModel.eventErrorDownloadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if(error) {
                    Snackbar.make(binding.getRoot(), R.string.connection_problem_message, Snackbar.LENGTH_LONG).show();
                    articlesEnVenteViewModel.showErrorDownload();
                    articlesEnVenteViewModel.onErrorDownloadArticlesFinished();
                }
            }
        });

        sharedArticleViewModel.getEventArticlePosted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean articlePosted) {
                if(articlePosted) {
                    Snackbar.make(binding.getRoot(), R.string.article_posted_message, Snackbar.LENGTH_SHORT).show();
                    sharedArticleViewModel.onArticlePostedFinished();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onClick(Article article) {
        sharedArticleViewModel.selectArticle(article);
        articlesEnVenteViewModel.onArticleClicked();
    }


}

