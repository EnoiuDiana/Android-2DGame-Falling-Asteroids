package com.example.game2d;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    float x = 0, y = 0;
    Bitmap background;

    Background(int screenX, int screenY, Resources resources) {
        background = BitmapFactory.decodeResource(resources, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

}
