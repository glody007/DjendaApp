package com.jjenda;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjenda.databinding.ListeArticlesBinding;
import com.jjenda.databinding.MyArticlesBinding;
import com.jjenda.reseau.Article;
import com.jjenda.reseau.Location;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyArticlesAdapter extends RecyclerView.Adapter<MyArticlesAdapter.MyArticlesEnVenteViewHolder> {
    private List<Article> articles;

    final private MyArticlesAdapterOnClickHandler mClickHandler;

    public interface MyArticlesAdapterOnClickHandler {
        void onClick(Article article);
    }

    public MyArticlesAdapter(MyArticlesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.articles = new ArrayList<>();
        //articles.add(new  Article("nom", "categorie", "description", "urlPhoto", "urlThumbnailPhoto", "latitude", "longitude", 200));
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyArticlesEnVenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyArticlesEnVenteViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MyArticlesEnVenteViewHolder articlesEnVenteViewHolderholder, int position) {
        Article article = articles.get(position);
        articlesEnVenteViewHolderholder.bind(article, mClickHandler);
    }

    @Override
    public int getItemCount() {
        if(articles != null) { return articles.size(); }
        return 0;
    }

    public static class MyArticlesEnVenteViewHolder extends RecyclerView.ViewHolder {
        final MyArticlesBinding mBinding;

        private MyArticlesEnVenteViewHolder(MyArticlesBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Article article, MyArticlesAdapterOnClickHandler clickHandler) {
            mBinding.setArticle(article);
            mBinding.setClickListener(clickHandler);
        }

        @NotNull
        public static MyArticlesEnVenteViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyArticlesEnVenteViewHolder(MyArticlesBinding.inflate(inflater, parent, false));
        }

    }
}
