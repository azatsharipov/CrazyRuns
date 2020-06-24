package com.example.crazyruns.game;

import java.util.ArrayList;

public class Racer extends Player {
    private float currentSpeed;
    private float maxSpeed;
    private float potentialMaxSpeed;
    private float posX;
    private float posY;
    private float mainPosY;
    private int jump;
    private final float borderX = 100;
    private final float jumpHigh = 25;
    private float time;

    public Racer(String name, int speed, int stamina, int agility, int reaction, float posY) {
        super(name, speed, stamina, agility, reaction);
        this.posY = posY;
        this.mainPosY = posY;
        this.posX = 75;
        this.jump = 0;
        currentSpeed = 0;
        maxSpeed = 1;
        potentialMaxSpeed = maxSpeed;
        time = 0;
    }

    public void move(ArrayList<Boolean> jumps, float raceTime, int maxReaction) {
        if ((maxReaction - reaction) < raceTime) {
            for (int i = 1; i < jumps.size() - 1; i++) {
                if (Math.abs((posX - borderX) - (i * 100 - jumpHigh)) < 1 && jumps.get(i)) {
                    jump = 1;
                }
            }
//            if (raceTime - (1000f - reaction) / 5 > stamina) // stamina by time
            if (posX - borderX > stamina)  // stamina by distance
                maxSpeed = Math.max(0.1f * (speed + stamina) / 200, maxSpeed - 0.1f);
            currentSpeed = Math.min(maxSpeed, currentSpeed + (float) speed / 50000);
            if (mainPosY - posY >= jumpHigh)
                jump = 2;
            if (jump == 1) {
                float newSpeed = (agility * potentialMaxSpeed) / 1000;
                posY -= newSpeed;
                posX += newSpeed;
            } else if (jump == 2) {
                float newSpeed = (agility * potentialMaxSpeed) / 1000;
                posY += newSpeed;
                posX += newSpeed;
                if (posY >= mainPosY) {
                    jump = 0;
                    currentSpeed /= 2;
                }
            } else
                posX += currentSpeed;
        }
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
