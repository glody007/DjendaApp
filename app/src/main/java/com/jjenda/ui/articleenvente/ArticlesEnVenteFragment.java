package com.jjenda.ui.articleenvente;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.jjenda.ArticlesAdapter;
import com.jjenda.databinding.FragmentArticlesEnVenteBinding;
import com.jjenda.reseau.Article;

import com.jjenda.R;
import com.jjenda.reseau.Location;
import com.jjenda.reseau.Repository;
import com.jjenda.ui.SharedArticleViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.jjenda.ui.main.MainFragmentDirections;

import java.util.List;


public class ArticlesEnVenteFragment extends Fragment implements
        ArticlesAdapter.ArticlesAdapterOnClickHandler {

    private ArticlesAdapter mAdapter;
    private ArticlesEnVenteViewModel articlesEnVenteViewModel;
    private SharedArticleViewModel sharedArticleViewModel;
    private FragmentArticlesEnVenteBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback callback;
    private LocationRequest locationRequest;

    private final int LOCATION_SETTING_REQUEST = 999;
    private final int LOCATION_PERMISSION_REQUEST = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles_en_vente, container, false);

        RecyclerView recyclerView = (RecyclerView) binding.recyclerviewArticlesEnVente;
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ArticlesAdapter(this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        articlesEnVenteViewModel = new ViewModelProvider(this).get(ArticlesEnVenteViewModel.class);
        sharedArticleViewModel = new ViewModelProvider(requireActivity()).get(SharedArticleViewModel.class);

        binding.setViewModel(articlesEnVenteViewModel);

        articlesEnVenteViewModel.getArticles().observe(getViewLifecycleOwner(),new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> newArticles) {
                mAdapter.setArticlesAndMyLocation(newArticles, articlesEnVenteViewModel.getLocation());
                articlesEnVenteViewModel.showArticles();
            }
        });

        articlesEnVenteViewModel.getEventLoadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean load) {
                if(load) {
                    articlesEnVenteViewModel.showLoading();
                    getLocation();
                    articlesEnVenteViewModel.onLoadArticlesFinished();
                }
            }
        });

        articlesEnVenteViewModel.getNavigateToArticleDetails().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if(navigate) {
                    Navigation.findNavController(binding.getRoot()).navigate(MainFragmentDirections
                            .actionArticlesFragmentToArticleDetailsFragment());
                    articlesEnVenteViewModel.onArticleDetailsNavigated();
                }

            }
        });

        articlesEnVenteViewModel.getEventErrorDownloadArticles().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean error) {
                if(error) {
                    binding.swiperefresh.setRefreshing(false);
                    articlesEnVenteViewModel.showErrorDownload();
                    articlesEnVenteViewModel.onErrorDownloadArticlesFinished();
                }
            }
        });

        articlesEnVenteViewModel.getEventLocationLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean locationLoaded) {
                if(locationLoaded) {
                    articlesEnVenteViewModel.loadArticlesFromRepository();
                    articlesEnVenteViewModel.onEventLocationLoadedFinished();
                }
            }
        });

        sharedArticleViewModel.getEventArticlePosted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean articlePosted) {
                if(articlePosted) {
                    Snackbar.make(binding.getRoot(), R.string.article_posted_message, Snackbar.LENGTH_SHORT).show();
                    Repository.getInstance().setUserArticlesCache(null);
                    sharedArticleViewModel.onArticlePostedFinished();
                }
            }
        });

        binding.swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        articlesEnVenteViewModel.loadArticles();
                    }
                }
        );

        return binding.getRoot();
    }


    @Override
    public void onClick(Article article) {
        sharedArticleViewModel.selectArticle(article);
        sharedArticleViewModel.setLocation(articlesEnVenteViewModel.getLocation());
        articlesEnVenteViewModel.onArticleClicked();
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private void getLocation() {
        locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task task = LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(builder.build());

        setupListenersForLocationSettings(task);

    }

    @RequiresApi(Build.VERSION_CODES.M)
    void setupListenersForLocationSettings(Task<LocationSettingsResponse> task) {
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(), LOCATION_SETTING_REQUEST);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @RequiresApi(Build.VERSION_CODES.M)
    void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            callback = locationCallback();

            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    Looper.getMainLooper()
            );
        } else {
            // You can directly ask for the permission.
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }
    }

    private LocationCallback locationCallback(){
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if(locationResult != null) {
                    Location myLocation = new Location(locationResult.getLastLocation().getLongitude(),
                                                       locationResult.getLastLocation().getLatitude());
                    articlesEnVenteViewModel.setLocation(myLocation);
                    articlesEnVenteViewModel.eventLocationLoaded();
                    fusedLocationClient.removeLocationUpdates(callback);
                }
            }
        };
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOCATION_SETTING_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    startLocationUpdates();
                }
                else {
                    getLocation();
                }
                break;
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("Not Empty", "Granted");
                } else {
                    Log.d("Not Empty", "Not Granted");
                }
                startLocationUpdates();
                return;
        }
    }


}

