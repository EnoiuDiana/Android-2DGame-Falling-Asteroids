package com.example.game2d;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private final String TAG = "Test";

    private Thread thread;
    private boolean isRunning;
    public static float screenRatioX;
    public static float screenRatioY;
    private final Background background1;
    private final Background background2;
    private int screenX;
    private int screenY;
    private Paint paint;
    private Spaceship spaceship;
    private List<LaserBeam> laserBeams;
    private Asteroid[] asteroids;
    private int updateCount = 0;
    private Random random;
    private boolean isGameOver = false;
    private Bitmap game_over;
    private int asteroidNumber = 4;
    private int score = 0;
    private GameActivity gameActivity;
    private int laserBeamSpeed = 20;


    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);

        this.gameActivity = gameActivity;

        this.screenX = screenX;
        this.screenY = screenY;

        screenRatioX = 2340f / screenX;
        screenRatioY = 1080f / screenY;


        this.background1 = new Background(screenX, screenY, getResources());
        this.background2 = new Background(screenX, screenY, getResources());

        this.spaceship = new Spaceship(screenY, getResources());
        this.laserBeams = new ArrayList<>();
        asteroids = new Asteroid[asteroidNumber];
        for (int i = 0; i < asteroidNumber; i++) {
            Asteroid asteroid = new Asteroid(screenX, screenY, getResources());
            asteroids[i] = asteroid;
        }

        random = new Random();

        this.background2.x = screenX;

        this.paint = new Paint();
        paint.setTextSize(60);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void run() {

        sleep();

        while (isRunning) {
            update();
            draw();
            //sleep();
        }

    }

    private void update() {

        background1.x -= 5;

        if (background1.x + this.screenX < 0){
            background1.x = screenX;
        }
        if (background1.x < 0) {
            background2.x = background1.x + this.screenX;
        }
        else {
            background2.x = background1.x - this.screenX;
        }

        if(!spaceship.hasNotMovedYet) {
            if (spaceship.isGoingUp) {
                spaceship.y -= 30 * screenRatioX;
            } else {
                spaceship.y += 30 * screenRatioX;
            }

            if (spaceship.y < 10) {
                spaceship.y = 10;
            }

            if (spaceship.y > screenY - spaceship.height - 10) {
                spaceship.y = screenY - spaceship.height - 10;
            }
        }

        List<LaserBeam> removeLaserBeams = new ArrayList<>();
        for(LaserBeam laserBeam : laserBeams) {
            if((laserBeam.x > screenX - 20) || (laserBeam.x < 0)) {
                removeLaserBeams.add(laserBeam);
            }
            laserBeam.x += laserBeamSpeed * screenRatioX;
            for (Asteroid asteroid : asteroids) { //asteroid intersect laser
                if (Rect.intersects(asteroid.getCollisionShape(), laserBeam.getCollisionShape())){
                    score += 1;
                    asteroid.x = screenX + 500;
                    asteroid.y = random.nextInt(screenY - asteroid.height);
                    laserBeam.x = - 200;
                }
            }
        }
        for(LaserBeam laserBeam : removeLaserBeams) {
            laserBeams.remove(laserBeam);
        }

        updateCount ++;
        if(updateCount == 7)  {
            newLaserBeams();  // maybe make a thread for this?
            updateCount = 0;
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.x -= asteroid.speed;
            //asteroid out of map
            if (asteroid.x + asteroid.width < 0) {
                isGameOver = true;
                makeGameOverBitmap();
            }
            // asteroid intersect ship
            if (Rect.intersects(asteroid.getCollisionShape(), spaceship.getCollisionShape())) {
                isGameOver = true;
                makeGameOverBitmap();
                return;
            }
        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawText("Score: " + score, 20, 80, paint);

            if(isGameOver) {
                isRunning = false;
                canvas.drawBitmap(game_over, (float)screenX/2 - (float) game_over.getWidth()/2,
                        (float)screenY/2 - (float) game_over.getHeight()/2, paint);
                getHolder().unlockCanvasAndPost(canvas);
                waitBeforeExiting();
                return;
            }

            for (Asteroid asteroid : asteroids) {
                canvas.drawBitmap(asteroid.getAsteroid(), asteroid.x, asteroid.y, paint);
            }

            canvas.drawBitmap(spaceship.getSpaceship(), spaceship.x, spaceship.y, paint);
            for(LaserBeam laserBeam : laserBeams) {
                canvas.drawBitmap(laserBeam.getLaserBeam(), laserBeam.x, laserBeam.y, paint);
            }
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(2000);
            gameActivity.startActivity(new Intent(gameActivity, MainActivity.class));
            gameActivity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause() {
        try {
            isRunning = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (spaceship.hasNotMovedYet) {
                    spaceship.hasNotMovedYet = false;
                }
                spaceship.isGoingUp = true;
                break;
            case MotionEvent.ACTION_UP:
                spaceship.isGoingUp = false;
                break;
        }

        return true;
    }

    public void newLaserBeams () {
        LaserBeam laserBeam = new LaserBeam(getResources());
        laserBeam.x = spaceship.x + spaceship.width;
        laserBeam.y = spaceship.y + (spaceship.height / 2) - 20;
        laserBeams.add(laserBeam);
    }

    private void makeGameOverBitmap() {
        game_over = BitmapFactory.decodeResource(getResources(), R.drawable.game_over_2);
        int width = game_over.getWidth();
        int height = game_over.getHeight();
        width /= 2;
        height /= 2;
        game_over = Bitmap.createScaledBitmap(game_over, width,
                height, false);

    }
}
