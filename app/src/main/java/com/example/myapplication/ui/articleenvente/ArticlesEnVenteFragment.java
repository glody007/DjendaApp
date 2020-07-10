package com.example.myapplication.ui.articleenvente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ArticlesEnVenteAdapter;
import com.example.myapplication.reseau.Article;
import com.example.myapplication.reseau.NetworkUtils;

import com.example.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticlesEnVenteFragment extends Fragment implements
        ArticlesEnVenteAdapter.ArticlesEnVenteAdapterOnClickHandler {

    private RecyclerView reclyclerView;
    private ArticlesEnVenteAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private NetworkUtils api;

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
        api = new NetworkUtils();
        setArticles();

        return root;
    }

    public void setArticles() {
        api.getArticles(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                int statusCode = response.code();
                List<Article> articles = response.body();
                mAdapter.setArticles(articles);
            }

            @Override
            public void onFailure(Call<List<Article>>call, Throwable t) {
                // Log error here since request failed
            }
        });
    }

    @Override
    public void onClick() {

    }

}

