package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

import com.example.go4lunch.models.nearbysearch.Result;
import com.example.go4lunch.ui.adapters.ListViewAdapter;

public class ListViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListViewAdapter listViewAdapter;
    private List<Result> restaurantList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        recyclerView = view.findViewById(R.id.rv_list_view);
        restaurantList = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(restaurantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(listViewAdapter);
        return view;
    }

    public void updateRestaurantList(List<Result> restaurants) {
        restaurantList.clear();
        restaurantList.addAll(restaurants);
        listViewAdapter.notifyDataSetChanged();
    }
}
