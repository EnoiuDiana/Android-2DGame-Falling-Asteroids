package com.example.game2d;

import static com.example.game2d.GameView.screenRatioX;
import static com.example.game2d.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class LaserBeam {
    public int x,y, width, height;
    public Bitmap laserBeam;

    LaserBeam(Resources resources) {
        this.laserBeam = BitmapFactory.decodeResource(resources, R.drawable.laserbeams);

        this.width = laserBeam.getWidth();
        this.height = laserBeam.getHeight();

        this.width = (int) (width * screenRatioX);
        this.height = (int) (height * screenRatioY);

        this.laserBeam = Bitmap.createScaledBitmap(laserBeam, width, height, false);
    }

    public Bitmap getLaserBeam() {
        return laserBeam;
    }

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
