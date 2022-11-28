package com.fazpass.social.object;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.List;

public class Feed {
    private UserView user;
    @Nullable
    private Drawable image;
    private String text;
    private List<Comment> comments;

    public Feed(UserView user, @Nullable Drawable image, String text, List<Comment> comments) {
        this.user = user;
        this.image = image;
        this.text = text;
        this.comments = comments;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    @Nullable
    public Drawable getImage() {
        return image;
    }

    public void setImage(@Nullable Drawable image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
