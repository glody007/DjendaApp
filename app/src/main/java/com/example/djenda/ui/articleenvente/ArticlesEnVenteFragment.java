package com.example.djenda.ui.articleenvente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djenda.ArticlesEnVenteAdapter;
import com.example.djenda.reseau.Article;

import com.example.djenda.R;

import java.util.List;


public class ArticlesEnVenteFragment extends Fragment implements
        ArticlesEnVenteAdapter.ArticlesEnVenteAdapterOnClickHandler {

    private RecyclerView reclyclerView;
    private ArticlesEnVenteAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArticlesEnVenteViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_articles_en_vente, container, false);

        reclyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_articles_en_vente);
        reclyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        reclyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesEnVenteAdapter(getActivity(), this);
        reclyclerView.setAdapter(mAdapter);

        model = new ViewModelProvider(this).get(ArticlesEnVenteViewModel.class);

        model.getArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
            }
        });
        return root;
    }


    @Override
    public void onClick() {

    }

}

