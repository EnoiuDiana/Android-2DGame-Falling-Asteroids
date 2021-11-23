package com.example.game2d;

import static com.example.game2d.GameView.screenRatioX;
import static com.example.game2d.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Asteroid {
    public int x, y, width, height;
    public Bitmap asteroid;
    public int speed = 20;
    private Random random;

    public Asteroid (int screenX, int screenY, Resources resources) {
        this.asteroid = BitmapFactory.decodeResource(resources, R.drawable.asteroid_brown);
        this.width = asteroid.getWidth();
        this.height = asteroid.getHeight();
        this.width /= 4;
        this.height /= 4;
        this.width *= (int) screenRatioX;
        this.height *= (int) screenRatioY;
        asteroid = Bitmap.createScaledBitmap(asteroid, width, height, false);
        x = screenX + 1000;
        random = new Random();
        y = random.nextInt(screenY - asteroid.getHeight() - 70);
        if (y < 70) {
            y = 70;
        }

    }

    public Bitmap getAsteroid() {
        return asteroid;
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }

}
