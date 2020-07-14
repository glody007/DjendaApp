package com.example.djenda.ui.mesarticles;

import android.os.Bundle;
import android.util.Log;
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
import com.example.djenda.ui.articleenvente.ArticlesEnVenteViewModel;

import java.util.List;


public class MesArticlesFragment extends Fragment implements
        ArticlesEnVenteAdapter.ArticlesEnVenteAdapterOnClickHandler {

    private RecyclerView reclyclerView;
    private ArticlesEnVenteAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MesArticlesViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mes_articles, container, false);

        reclyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_mes_articles);
        reclyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        reclyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesEnVenteAdapter(getActivity(), this);
        reclyclerView.setAdapter(mAdapter);

        model = new ViewModelProvider(this).get(MesArticlesViewModel.class);

        model.getArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
            }
        });
        Log.d("ldjkf", "ldfj");
        return root;
    }


    @Override
    public void onClick() {

    }

}

