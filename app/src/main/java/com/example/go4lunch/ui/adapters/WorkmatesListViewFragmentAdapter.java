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

import java.util.List;

public class WorkmatesListViewFragmentAdapter extends RecyclerView.Adapter<WorkmatesListViewFragmentAdapter.WorkmatesViewHolder> {


    //Je dois récupérer la liste des Users qui ont uniquement le même restaurantId que dans le champ selectedRestaurantId

    private List<User> userList;

    public WorkmatesListViewFragmentAdapter(List<User> userList) {
        this.userList = userList;

    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates_listview, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
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

    public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTextView;
        private ImageView workmatesAvatar;

        public WorkmatesViewHolder(@NonNull View itemView) {
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
