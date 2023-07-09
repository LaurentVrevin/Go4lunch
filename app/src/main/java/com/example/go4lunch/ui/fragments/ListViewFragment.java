package com.example.go4lunch.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.adapters.ListViewAdapter;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.example.go4lunch.viewmodels.RestaurantViewModel;

import java.util.List;

public class ListViewFragment extends Fragment {

    private LocationPermissionViewModel locationPermissionViewModel;
    private RestaurantViewModel restaurantViewModel;
    private List<Restaurant> restaurantListData;
    private Location location;
    private User user;
    private RecyclerView recyclerView;
    private ListViewAdapter listViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        recyclerView = view.findViewById(R.id.rv_list_view);
        configureViewModels();
        configureRecyclerView();

        return view;
    }

    private void configureRecyclerView() {
        listViewAdapter = new ListViewAdapter(restaurantListData, location);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(listViewAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observePermission();
    }

    private void configureViewModels() {
        locationPermissionViewModel = new ViewModelProvider(requireActivity()).get(LocationPermissionViewModel.class);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
    }

    private void observePermission() {
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::observeData);
    }

    private void observeData(Boolean hasPermission) {
        if (hasPermission) {
            locationPermissionViewModel.getCurrentLocation().observe(requireActivity(), this::updateLocation);
            restaurantViewModel.getRestaurantsLiveData().observe(requireActivity(), this::updateRestaurantList);
        }
    }

    private void updateRestaurantList(List<Restaurant> restaurants) {
        restaurantListData = restaurants;
        if (listViewAdapter != null) {
            listViewAdapter.setRestaurantList(restaurantListData);
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private void updateLocation(Location location) {
        if (this.isAdded()) {
            this.location = location;
        }
    }



}
