package com.jjenda;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jjenda.databinding.ListeArticlesBinding;
import com.jjenda.reseau.Article;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesEnVenteViewHolder> {
    private List<Article> articles;

    final private ArticlesAdapterOnClickHandler mClickHandler;

    public interface ArticlesAdapterOnClickHandler {
        void onClick(Article article);
    }

    public ArticlesAdapter(ArticlesAdapterOnClickHandler clickHandler) {
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
    public ArticlesEnVenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ArticlesEnVenteViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesEnVenteViewHolder articlesEnVenteViewHolderholder, int position) {
        Article article = articles.get(position);

        articlesEnVenteViewHolderholder.bind(article, mClickHandler);
    }

    @Override
    public int getItemCount() {
        if(articles != null) { return articles.size(); }
        return 0;
    }

    public static class ArticlesEnVenteViewHolder extends RecyclerView.ViewHolder {
        final ListeArticlesBinding mBinding;

        private ArticlesEnVenteViewHolder(ListeArticlesBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(Article article, ArticlesAdapterOnClickHandler clickHandler) {
            mBinding.setArticle(article);
            mBinding.setClickListener(clickHandler);
        }

        @NotNull
        public static ArticlesEnVenteViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ArticlesEnVenteViewHolder(ListeArticlesBinding.inflate(inflater, parent, false));
        }

    }
}