package com.example.crazyruns.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.example.crazyruns.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class Race extends View implements Runnable {
    final float FPS = 60;
    final float SECOND = 1000000000;
    final float UPDATE_TIME = SECOND / FPS;
    int distance;
    float raceTime;
    Thread gameThread = null;
    Player player;
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
        Log.e("MY", "Race");
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
        colors.add(Color.rgb(255, 127, 80));
        places = new ArrayList<>();
        times = new ArrayList<>();
        this.jumps = jumps;
        this.distance = distance;
    }

    public void startGame() {
        Log.e("MY", "start");
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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("MY", "1");
        super.onDraw(canvas);
        int x = getWidth();
        int y = getHeight();
        int radius;
        radius = 25;
        clearScene(canvas);
        paint.setColor(R.color.colorMenuBackground);
        canvas.drawRect(0, 0, x, y, paint);
        drawLines(canvas);
        for (int i = 0; i < racers.size(); i++) {
            paint.setColor(colors.get(i));
            Racer racer = racers.get(i);
            canvas.drawCircle(racer.getPosX(), racer.getPosY(), radius, paint);
        }
        if (!isRunning && !isGameOver) {
            float textSize = 100;
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize);
            canvas.drawText(String.valueOf(timeLeft), x / 2, y / 2, paint);
        }
    }

    private void clearScene(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
    }

    private void drawLines(Canvas canvas) {
        paint.setColor(Color.BLACK);
        float textSize = 30;
        paint.setTextSize(textSize);
        float topLine = 150;
        for (int i = 0; i <= distance; i += 100) {
            canvas.drawText(String.valueOf(i) + "m", 50 + i, 125, paint);
            if (jumps.get(i / 100)) {
                canvas.drawLine(100 + i, topLine, 100 + i, topLine + racers.size() * 100, paint);
            }
        }
        for (int i = 0; i <= racers.size(); i++) {
            canvas.drawLine(0, topLine + i * 100,
                    distance + 100, topLine + i * 100, paint);
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
        Log.e("MY", "5");
        while (isRunning) {
            Log.e("MY", "run");
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
