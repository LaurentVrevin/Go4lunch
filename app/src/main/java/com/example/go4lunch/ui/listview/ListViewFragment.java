package com.example.go4lunch.ui.listview;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.go4lunch.databinding.FragmentListviewBinding;

public class ListViewFragment extends Fragment {

    private FragmentListviewBinding binding;
    public static ListViewFragment newInstance(){
        return new ListViewFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewViewModel listViewViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ListViewViewModel.class);

        binding = FragmentListviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textListview;
        listViewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}