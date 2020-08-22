package com.example.djenda.ui.bottomnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.djenda.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bottom_nav, container, false);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) root.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_mes_articles);
        return root;
    }

}