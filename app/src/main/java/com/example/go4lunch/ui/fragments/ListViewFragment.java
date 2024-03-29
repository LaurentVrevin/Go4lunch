package com.example.go4lunch.ui.fragments;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.go4lunch.R;
import com.example.go4lunch.models.Restaurant;
import com.example.go4lunch.models.User;
import com.example.go4lunch.ui.adapters.ListViewAdapter;
import com.example.go4lunch.utils.LikesCounter;
import com.example.go4lunch.viewmodels.LocationPermissionViewModel;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ListViewFragment extends Fragment {

    private LocationPermissionViewModel locationPermissionViewModel;
    private RestaurantViewModel restaurantViewModel;
    private List<Restaurant> restaurantListData;
    private List<User> workmatesListData;
    private List<User> usersList;
    private Location location;
    private User currentUser;
    private RecyclerView listviewRecyclerView;
    private ListViewAdapter listViewAdapter;
    private UserViewModel userViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        listviewRecyclerView = view.findViewById(R.id.rv_list_view);
        configureViewModels();
        configureRecyclerView();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_listview_fragment);
        swipeRefreshLayout.setOnRefreshListener(this::refreshListView);
        return view;
    }

    private void configureRecyclerView() {
        listViewAdapter = new ListViewAdapter(restaurantListData, location, workmatesListData);
        listviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        listviewRecyclerView.setAdapter(listViewAdapter);
        // Create a DividerItemDecoration
        listviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listviewRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observePermission();
    }

    private void configureViewModels() {
        locationPermissionViewModel = new ViewModelProvider(requireActivity()).get(LocationPermissionViewModel.class);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
        });
    }

    private void observePermission() {
        locationPermissionViewModel.observePermission().observe(requireActivity(), this::observeData);
    }

    private void observeData(Boolean hasPermission) {
        if (hasPermission) {
            locationPermissionViewModel.getCurrentLocation().observe(requireActivity(), this::updateLocation);
            restaurantViewModel.getListRestaurantLiveData().observe(getViewLifecycleOwner(), this::updateRestaurantList);

            userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userList -> {
                if (userList != null && currentUser != null) {
                    // Exclude the logged-in user from the list of workmates
                    List<User> filteredWorkmatesList = new ArrayList<>();

                    for (User user : userList) {
                        if (!user.getUserId().equals(currentUser.getUserId())) {
                            filteredWorkmatesList.add(user);
                        }
                    }
                    listViewAdapter.setAllUsersList(userList);
                    updateWorkmatesList(filteredWorkmatesList, userList);
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRestaurantList(List<Restaurant> restaurants) {
        restaurantListData = restaurants;
        if (listViewAdapter != null) {
            listViewAdapter.setRestaurantList(restaurantListData);
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private void updateWorkmatesList(List<User> filteredWorkmatesList, List<User> allUsersList) {
        workmatesListData = filteredWorkmatesList;
        usersList = allUsersList;
        if (listViewAdapter != null) {
            listViewAdapter.setWorkmatesList(workmatesListData);
            listViewAdapter.setAllUsersList(usersList);
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private void updateLocation(Location location) {
        if (this.isAdded()) {
            this.location = location;
        }
    }

    // SEARCH
    public void filterRestaurantList(String query) {
        List<Restaurant> filteredList = new ArrayList<>();

        for (Restaurant restaurant : restaurantListData) {
            if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }

        listViewAdapter.setRestaurantList(filteredList);
        listViewAdapter.notifyDataSetChanged();
    }

    private void refreshListView() {
        // Refresh the list of workmates
        userViewModel.getWorkmatesListFromFirestore(true);
        LikesCounter.updateLikesCount(restaurantListData, usersList);
        // Stop the animation
        swipeRefreshLayout.setRefreshing(false);
    }
}
