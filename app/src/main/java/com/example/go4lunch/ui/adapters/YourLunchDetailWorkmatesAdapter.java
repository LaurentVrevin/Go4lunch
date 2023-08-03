package com.example.go4lunch.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

public class YourLunchDetailWorkmatesAdapter extends RecyclerView.Adapter<YourLunchDetailWorkmatesAdapter.YourLunchDetailWorkmatesViewHolder> {

    private List<User> userList;

    public YourLunchDetailWorkmatesAdapter(List<User> userList) {
        if (userList == null) {
            this.userList = new ArrayList<>();
        } else {
            this.userList = userList;
        }
    }

    @NonNull
    @Override
    public YourLunchDetailWorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_workmates_view, parent, false);
        return new YourLunchDetailWorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourLunchDetailWorkmatesViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getName() + " vous êtes sur WorkmatesListViewAdapter !");
        String profilePictureUrl = user.getPictureUrl();
        if (profilePictureUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(profilePictureUrl)
                    .circleCrop()
                    .into(holder.workmatesAvatar);
        }
    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    public class YourLunchDetailWorkmatesViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private ImageView workmatesAvatar;

        public YourLunchDetailWorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.tv_workmate_name);
            workmatesAvatar = itemView.findViewById(R.id.im_workmate);
        }
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }


}
