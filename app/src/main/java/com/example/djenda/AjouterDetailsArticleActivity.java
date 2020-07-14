package com.example.djenda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.djenda.reseau.Repository;


public class AjouterDetailsArticleActivity extends AppCompatActivity {
    Button posterBouton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_details_article);
        posterBouton = findViewById(R.id.btn_poster_details_article);
        posterBouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Repository repository = Repository.getInstance();
                repository.createArticle();
            }
        });

    }
}