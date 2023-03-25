package com.example.go4lunch.ui.mapview;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.go4lunch.databinding.FragmentMapviewBinding;

public class MapViewFragment extends Fragment {

    private FragmentMapviewBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MapViewViewModel mapViewViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MapViewViewModel.class);
        binding = FragmentMapviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMapview;
        mapViewViewModel.getText().observe(getViewLifecycleOwner(), text -> textView.setText(text));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
