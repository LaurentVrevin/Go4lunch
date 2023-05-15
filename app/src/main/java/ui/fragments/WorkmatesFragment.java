package ui.fragments;

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

import com.example.go4lunch.R;

import java.util.ArrayList;

import models.User;
import ui.adapters.UserAdapter;
import viewmodels.UserViewModel;


public class WorkmatesFragment extends Fragment {

    private UserViewModel userViewModel;
    private RecyclerView workmatesRecyclerView;
    private UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("WorkmatesFragment", "onCreateView() called");
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);

        workmatesRecyclerView = view.findViewById(R.id.rv_workmates_list_view);

        userAdapter = new UserAdapter(new ArrayList<User>());
        workmatesRecyclerView.setAdapter(userAdapter);

        // Création d'un DividerItemDecoration
        workmatesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workmatesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUserListLiveData().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                // Mettre à jour votre RecyclerView avec la nouvelle liste d'utilisateurs
                UserAdapter userAdapter = new UserAdapter(userList);
                workmatesRecyclerView.setAdapter(userAdapter);
            }
        });

        userViewModel.getUserListFromFirestore();

        return view;
    }
}

