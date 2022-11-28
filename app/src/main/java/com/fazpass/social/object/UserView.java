package com.fazpass.social.object;

import android.graphics.drawable.Drawable;

public class UserView {
   private Drawable pic;
   private String name;

   public UserView(Drawable pic, String name) {
      this.pic = pic;
      this.name = name;
   }

   public Drawable getPic() {
      return pic;
   }

   public void setPic(Drawable pic) {
      this.pic = pic;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
