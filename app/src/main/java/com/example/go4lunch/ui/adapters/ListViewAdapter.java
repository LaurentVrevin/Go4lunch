package com.example.go4lunch.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import java.util.List;

import com.example.go4lunch.models.nearbysearch.Result;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private List<Result> restaurantList;

    public ListViewAdapter(List<Result> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result restaurant = restaurantList.get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_place_name);
            textViewAddress = itemView.findViewById(R.id.tv_place_address);
        }

        public void bind(Result restaurant) {
            textViewName.setText(restaurant.getName());
            textViewAddress.setText(restaurant.getVicinity());
        }
    }
}
