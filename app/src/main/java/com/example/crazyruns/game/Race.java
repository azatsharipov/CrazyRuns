package com.example.crazyruns.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.crazyruns.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class Race extends View implements Runnable {
    final float FPS = 60;
    final float SECOND = 1000000000;
    final float UPDATE_TIME = SECOND / FPS;
    final int WIDTH = 1440;
    final int HEIGHT = 720;
    int distance;
    float raceTime;
    Thread gameThread = null;
    ArrayList<Racer> racers;
    ArrayList<Integer> colors;
    ArrayList<Integer> places;
    ArrayList<Float> times;
    ArrayList<Boolean> jumps;
    Paint paint = null;
    boolean isRunning;
    boolean isGameOver;
    RaceFragment rf;
    int timeLeft;
    int maxReaction;

    public Race(Context context, RaceFragment rf, ArrayList<Racer> racers, int distance, ArrayList<Boolean> jumps) {
        super(context);
        this.rf = rf;
        paint = new Paint();
        isRunning = false;
        this.racers = racers;
        maxReaction = 0;
        for (int i = 0; i < racers.size(); i++) {
            maxReaction = Math.max(maxReaction, racers.get(i).getReaction());
        }
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.rgb(255, 127, 80)); // Color.ORANGE
        places = new ArrayList<>();
        times = new ArrayList<>();
        this.jumps = jumps;
        this.distance = distance;
    }

    public void startGame() {
        if (isRunning)
            return;
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGame() {
        isRunning = false;
        try {
            rf.stopRace();
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int adaptWidth(int x) {
        return x * getWidth() / WIDTH;
    }

    private int adaptHeight(int y) {
        return y * getHeight() / HEIGHT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius = 25;
//        clearScene(canvas);
        drawLines(canvas);
        for (int i = 0; i < racers.size(); i++) {
            paint.setColor(colors.get(i));
            Racer racer = racers.get(i);
            canvas.drawCircle(racer.getPosX(), racer.getPosY(), radius, paint);
        }
        if (!isRunning && !isGameOver) {
            float textSize = 300;
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            canvas.drawText(String.valueOf(timeLeft), x / 2, y / 2, paint);
        }
    }

    private void clearScene(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.gradient_background);
        canvas.drawBitmap(bitmap,
                null,
                new Rect(0,0, getWidth(), getHeight()), paint);
    }

    private void drawLines(Canvas canvas) {
        paint.setColor(Color.BLACK);
        float textSize = 30;
        paint.setTextSize(textSize);
        int topLine = 150;
        paint.setStrokeWidth(5);
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_running_track_layer);
        for (int i = 0; i < getWidth(); i += 100) {
            for (int j = 0; j < racers.size(); j++) {
                canvas.drawBitmap(bitmap,
                        null,
                        new Rect(i,topLine + j * 100,100 + i,topLine + j * 100 + 100), paint);
            }
        }
        paint.setColor(Color.WHITE);
        for (int i = 0; i <= distance; i += 100) {
            canvas.drawText(String.valueOf(i) + "m", 50 + i, 125, paint);
            if (jumps.get(i / 100)) {
                if (i == 0 || i == distance) {
                    paint.setStrokeWidth(5);
                    canvas.drawLine(100 + i, topLine, 100 + i, topLine + racers.size() * 100, paint);
                } else {
                    for (int j = 0; j < racers.size(); j++) {
                        paint.setStrokeWidth(8);
                        canvas.drawLine(100 + i, topLine + j * 100 + 10, 100 + i, topLine + j * 100 + 90, paint);
                    }
                }
            }
        }
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);
        for (int i = 0; i <= racers.size(); i++) {
            canvas.drawLine(0, topLine + i * 100,
                    getWidth(), topLine + i * 100, paint);
        }
        textSize = 100;
        paint.setTextSize(textSize);
        for (int i = 0; i < racers.size(); i++) {
            canvas.drawText(String.valueOf(i + 1), 25, topLine + i * 100 + 85, paint);
            if (!isRunning && !isGameOver)
                canvas.drawText(racers.get(i).getName(), 125, topLine + i * 100 + 85, paint);
        }
    }

    public void drawTimer(int timeLeft) {
        this.timeLeft = timeLeft;
        postInvalidate();
    }

    @Override
    public void run() {
        float lastTime = System.nanoTime();
        float delta = 0;
        raceTime = 0;
        while (isRunning) {
            float nowTime = System.nanoTime();
            float elapsedTime = nowTime - lastTime;
            lastTime = nowTime;
            raceTime += elapsedTime / UPDATE_TIME;
            delta += elapsedTime / UPDATE_TIME;
            if (delta > 1) {
                updateGame();
                postInvalidate();
                delta--;
            }
            isGameOver = true;
            ArrayList<Integer> toPlaces = new ArrayList<>();
            for (int i = 0; i < racers.size(); i++) {
                Racer racer = racers.get(i);
                if (racer.getPosX() <= distance + 100) {
                    isGameOver = false;
                } else if (!places.contains(i)) {
                    toPlaces.add(i);
                }
            }
            Collections.shuffle(toPlaces);
            for (int i = 0; i < toPlaces.size(); i++) {
                places.add(toPlaces.get(i));
                times.add(raceTime * UPDATE_TIME / SECOND + (float)i / 100);
            }
            if (isGameOver)
                stopGame();
        }
    }

    public ArrayList<Integer> getPlaces() {
        return places;
    }

    public ArrayList<Float> getTimes() {
        return times;
    }

    private void updateGame() {
        for (int i = 0; i < racers.size(); i++) {
            Racer racer = racers.get(i);
            racer.move(jumps, raceTime, maxReaction);
        }
    }
}
