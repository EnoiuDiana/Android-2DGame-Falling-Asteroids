package com.example.game2d;

import static com.example.game2d.GameView.screenRatioX;
import static com.example.game2d.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Spaceship {

    private final String TAG = "Test";

    public boolean isGoingUp = false;
    public boolean hasNotMovedYet = true;
    public int x, y, width, height;
    public Bitmap spaceship;

    Spaceship (int screenY, Resources resources) {

        this.spaceship = BitmapFactory.decodeResource(resources, R.drawable.spaceship);

        this.width = spaceship.getWidth();
        this.height = spaceship.getHeight();

        this.width /= 5;
        this.height /= 5;

        this.width = (int) (width * screenRatioX);
        this.height = (int) (height * screenRatioY);

        this.spaceship = Bitmap.createScaledBitmap(spaceship, width, height, false);

        this.y = screenY/2;
        this.x = (int) (64 * screenRatioX);
    }

    public Bitmap getSpaceship() {

        return spaceship;
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
