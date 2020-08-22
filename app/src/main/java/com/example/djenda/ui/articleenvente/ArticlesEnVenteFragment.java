package com.example.djenda.ui.articleenvente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djenda.ArticlesAdapter;
import com.example.djenda.reseau.Article;

import com.example.djenda.R;
import com.example.djenda.ui.SharedArticleViewModel;
import com.example.djenda.ui.main.ArticlesFragmentDirections;

import java.util.List;


public class ArticlesEnVenteFragment extends Fragment implements
        ArticlesAdapter.ArticlesAdapterOnClickHandler {

    private ArticlesAdapter mAdapter;
    private ArticlesEnVenteViewModel articlesEnVenteViewModel;
    private SharedArticleViewModel sharedArticleViewModel;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_articles_en_vente, container, false);

        RecyclerView reclyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_articles_en_vente);
        reclyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        reclyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesAdapter(this);
        reclyclerView.setAdapter(mAdapter);

        articlesEnVenteViewModel = new ViewModelProvider(this).get(ArticlesEnVenteViewModel.class);
        sharedArticleViewModel = new ViewModelProvider(requireActivity()).get(SharedArticleViewModel.class);

        articlesEnVenteViewModel.getArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
            }
        });

        articlesEnVenteViewModel.getNavigateToArticleDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if(navigate) {
                    Navigation.findNavController(root).navigate(ArticlesFragmentDirections
                            .actionArticlesFragmentToArticleDetailsFragment());
                    articlesEnVenteViewModel.onArticleDetailsNavigated();
                }

            }
        });

        return root;
    }


    @Override
    public void onClick(Article article) {
        sharedArticleViewModel.selectArticle(article);
        articlesEnVenteViewModel.onArticleClicked();
    }


}

