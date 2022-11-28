package com.fazpass.social.ui.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fazpass.social.R;
import com.fazpass.social.object.Feed;

import java.util.List;

public class NotificationRVAdapter extends RecyclerView.Adapter<NotificationRVAdapter.ViewHolder> {
    private final List<Feed> notifications;

    public NotificationRVAdapter(List<Feed> notifications) {
        this.notifications = notifications;
    }

    public List<Feed> getNotifications() {
        return notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Feed feed = notifications.get(position);

        viewHolder.userName.setText(feed.getUser().getName());
        viewHolder.userImg.setImageDrawable(feed.getUser().getPic());
        viewHolder.notificationText.setText(feed.getText());

        // simulate notification unread
        if (position < 2) {
            viewHolder.itemView.setBackgroundResource(R.color.bg_unread_notif);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        TextView notificationText;

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.notif_user_img);
            userName = itemView.findViewById(R.id.notif_user_name);
            notificationText = itemView.findViewById(R.id.notif_text);
        }
    }
}
