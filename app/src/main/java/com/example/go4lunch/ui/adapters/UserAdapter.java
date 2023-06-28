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

import java.util.List;

import com.example.go4lunch.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_listview, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTextView.setText(user.getName());
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

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private ImageView workmatesAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.tv_workmate_name);
            workmatesAvatar = itemView.findViewById(R.id.im_workmate);
        }

        /*public void bind(User user) {
            userNameTextView.setText(user.getName());
        }*/
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }
}