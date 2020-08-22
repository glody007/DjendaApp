package com.example.djenda.ui.mesarticles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.djenda.ArticlesAdapter;
import com.example.djenda.reseau.Article;
import com.example.djenda.R;
import java.util.List;



public class MesArticlesFragment extends Fragment implements
        ArticlesAdapter.ArticlesAdapterOnClickHandler {

    private RecyclerView reclyclerView;
    private ArticlesAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MesArticlesViewModel mesArticlesViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mes_articles, container, false);

        reclyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_mes_articles);
        reclyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        reclyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesAdapter(this);
        reclyclerView.setAdapter(mAdapter);

        mesArticlesViewModel = new ViewModelProvider(this).get(MesArticlesViewModel.class);

        mesArticlesViewModel.getUserArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticles(newArticles);
            }
        });

        return root;
    }


    @Override
    public void onClick(Article article) {
        Toast.makeText(getContext(), article.getNom(), Toast.LENGTH_LONG).show();
    }

}

