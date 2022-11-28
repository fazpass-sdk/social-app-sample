package com.fazpass.social.ui.main;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fazpass.social.R;
import com.fazpass.social.object.Feed;

import java.util.List;

public class FeedRVAdapter extends RecyclerView.Adapter<FeedRVAdapter.FeedViewHolder> {
    //private static final int VIEW_PROFILE = 1;
    //private static final int VIEW_FEED = 2;

    //private User user;
    private List<Feed> feeds;

    public FeedRVAdapter(List<Feed> feeds) {
        //this.user = user;
        this.feeds = feeds;
    }

    /*public User getUser() {
        return user;
    }*/

    public List<Feed> getFeeds() {
        return feeds;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*if (viewType == VIEW_PROFILE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vh_profile, parent, false);
            return new ProfileViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vh_feed, parent, false);
            return new FeedViewHolder(view);
        }*/
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vh_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int position) {
        final Context feedContext = feedViewHolder.itemView.getContext();
        final Feed feed = feeds.get(position/* - 1*/);

        feedViewHolder.userImg.setImageDrawable(feed.getUser().getPic());
        feedViewHolder.userName.setText(feed.getUser().getName());
        if (feed.getImage() == null) {
            feedViewHolder.feedImg.setVisibility(View.GONE);
        } else {
            feedViewHolder.feedImg.setImageDrawable(feed.getImage());
        }
        feedViewHolder.feedText.setText(feed.getText());

        AnimatorSet likeBtnSet = (AnimatorSet) AnimatorInflater.loadAnimator(feedContext,
                R.animator.toggle_button);
        likeBtnSet.setTarget(feedViewHolder.feedLikeToggleBtn);
        feedViewHolder.feedLikeToggleBtn.setOnCheckedChangeListener((view, isChecked) -> {
            likeBtnSet.start();

            if (isChecked) {
                view.setTextColor(feedContext.getColor(R.color.red_bright));
            } else {
                view.setTextColor(feedContext.getColor(R.color.default_text_color));
            }
        });

        AnimatorSet dislikeBtnSet = (AnimatorSet) AnimatorInflater.loadAnimator(feedContext,
                R.animator.toggle_button);
        dislikeBtnSet.setTarget(feedViewHolder.feedDislikeToggleBtn);
        feedViewHolder.feedDislikeToggleBtn.setOnCheckedChangeListener((view, isChecked) -> {
            dislikeBtnSet.start();

            if (isChecked) {
                view.setTextColor(feedContext.getColor(R.color.gray_dark));
            } else {
                view.setTextColor(feedContext.getColor(R.color.default_text_color));
            }
        });

        AnimatorSet shareBtnSet = (AnimatorSet) AnimatorInflater.loadAnimator(feedContext,
                R.animator.toggle_button);
        shareBtnSet.setTarget(feedViewHolder.feedShareToggleBtn);
        feedViewHolder.feedShareToggleBtn.setOnCheckedChangeListener((view, isChecked) -> {
            shareBtnSet.start();

            if (isChecked) {
                view.setTextColor(feedContext.getColor(R.color.blue_dark));
            } else {
                view.setTextColor(feedContext.getColor(R.color.default_text_color));
            }
        });

        /*case VIEW_PROFILE:
        final ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;

        //profileViewHolder.userName.setText(user.getName());
        //profileViewHolder.userEmail.setText(user.getEmail());
        profileViewHolder.userPhone.setText(user.getPhone());*/
    }

    /*@Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_PROFILE;
        }
        return VIEW_FEED;
    }*/

    @Override
    public int getItemCount() {
        return feeds.size()/*+1*/;
    }

    protected static class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView userName;
        ImageView feedImg;
        TextView feedText;
        ToggleButton feedLikeToggleBtn;
        ToggleButton feedDislikeToggleBtn;
        ToggleButton feedShareToggleBtn;

        protected FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.feed_user_img);
            userName = itemView.findViewById(R.id.feed_user_name);
            feedImg = itemView.findViewById(R.id.feed_img);
            feedText = itemView.findViewById(R.id.feed_text);
            feedLikeToggleBtn = itemView.findViewById(R.id.feed_like_togglebtn);
            feedDislikeToggleBtn = itemView.findViewById(R.id.feed_dislike_togglebtn);
            feedShareToggleBtn = itemView.findViewById(R.id.feed_share_togglebtn);
        }
    }

    /*protected static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView userEmail;
        TextView userPhone;

        protected ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.profile_name);
            userEmail = itemView.findViewById(R.id.profile_email);
            userPhone = itemView.findViewById(R.id.profile_phone);
        }
    }*/
}
