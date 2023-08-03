package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.go4lunch.R;

import java.util.ArrayList;

import com.example.go4lunch.ui.adapters.WorkmatesFragmentAdapter;
import com.example.go4lunch.viewmodels.RestaurantViewModel;
import com.example.go4lunch.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WorkmatesFragment extends Fragment {

    private UserViewModel userViewModel;
    private RestaurantViewModel restaurantViewModel;
    private RecyclerView workmatesRecyclerView;
    private WorkmatesFragmentAdapter workmatesFragmentAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("WorkmatesFragment", "onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        workmatesRecyclerView = view.findViewById(R.id.rv_workmates_list_view);

        configureRecyclerView();
        configureViewModels();
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_workmates_fragment_rv);
        swipeRefreshLayout.setOnRefreshListener(this::refreshWorkmatesList);

        return view;
    }

    private void configureViewModels() {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        restaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);

        //Je récupère la liste des users depuis firestore
        userViewModel.getWorkmatesListFromFirestore(false);

        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                workmatesFragmentAdapter.setUserList(userList);
            }
        });
        restaurantViewModel.getListRestaurantLiveData().observe(getViewLifecycleOwner(), restaurantList -> {
            if (restaurantList != null) {
                workmatesFragmentAdapter.setRestaurantList(restaurantList);
            }
        });
    }

    private void configureRecyclerView() {
        workmatesFragmentAdapter = new WorkmatesFragmentAdapter(new ArrayList<>(), new ArrayList<>());
        workmatesRecyclerView.setAdapter(workmatesFragmentAdapter);

        // Création d'un DividerItemDecoration
        workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workmatesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
    // Méthode de rafraîchissement de la liste des workmates
    private void refreshWorkmatesList() {
        //Rafraichit la liste des workmates
        userViewModel.getWorkmatesListFromFirestore(true);
        // Stop l'animation
        swipeRefreshLayout.setRefreshing(false);
    }
}

