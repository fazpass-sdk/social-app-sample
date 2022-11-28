package com.fazpass.social.data;

import android.content.Context;

import androidx.appcompat.content.res.AppCompatResources;

import com.fazpass.social.R;
import com.fazpass.social.object.Feed;
import com.fazpass.social.object.UserView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleFeed {
   private Context context;

   public SampleFeed(Context context) {
      this.context = context;
   }

   public List<Feed> getFeed1() {
      final Feed[] list = {
              new Feed(
                      new UserView(AppCompatResources.getDrawable(context, R.drawable.avatar_guy), "Samuel Smith"),
                      AppCompatResources.getDrawable(context, R.drawable.apple),
                      "I just got this super apple from a supermarket near stadium!",
                      new ArrayList<>()),
              new Feed(
                      new UserView(AppCompatResources.getDrawable(context, R.drawable.avatar_girl), "Wendigoo"),
                      null,
                      "Today I don't feel like doing anything....",
                      new ArrayList<>()),
              new Feed(
                      new UserView(AppCompatResources.getDrawable(context, R.drawable.avatar_guy), "Samuel Smith"),
                      AppCompatResources.getDrawable(context, R.drawable.avocado),
                      "Bro, this thing kinda mid.",
                      new ArrayList<>()),
              new Feed(
                      new UserView(AppCompatResources.getDrawable(context, R.drawable.avatar_guy), "Samuel Smith"),
                      AppCompatResources.getDrawable(context, R.drawable.black_hole),
                      "So I asked an AI to make me an image..... You won't believe it.",
                      new ArrayList<>()),
              new Feed(
                      new UserView(AppCompatResources.getDrawable(context, R.drawable.avatar_girl), "Wendigoo"),
                      AppCompatResources.getDrawable(context, R.drawable.beach),
                      "Time to take another vacation, i guess.......",
                      new ArrayList<>()),
      };
      return Arrays.asList(list);
   }
}
