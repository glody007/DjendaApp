package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.reseau.Article;

import java.util.List;

public class ArticlesEnVenteAdapter extends RecyclerView.Adapter<ArticlesEnVenteAdapter.ArticlesEnVenteViewHolder> {
    private final Context mContext;
    private List<Article> articles;

    final private ArticlesEnVenteAdapterOnClickHandler mClickHandler;

    public interface ArticlesEnVenteAdapterOnClickHandler {
        void onClick();
    }

    public ArticlesEnVenteAdapter(@NonNull Context context, ArticlesEnVenteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticlesEnVenteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.liste_articles;

        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new ArticlesEnVenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesEnVenteViewHolder articlesEnVenteViewHolderholder, int position) {
        Article article = articles.get(position);
        articlesEnVenteViewHolderholder.categorieView.setText(article.getCategorie());
        articlesEnVenteViewHolderholder.descriptionView.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        if(articles != null) { return articles.size(); }
        return 0;
    }

    class ArticlesEnVenteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView articlePhotoView;
        final TextView categorieView;
        final TextView descriptionView;


        ArticlesEnVenteViewHolder(View view) {
            super(view);

            articlePhotoView = (ImageView) view.findViewById(R.id.article_image);
            categorieView = (TextView) view.findViewById(R.id.article_categorie);
            descriptionView = (TextView) view.findViewById(R.id.article_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}