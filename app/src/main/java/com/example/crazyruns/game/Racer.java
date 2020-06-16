package com.example.crazyruns.game;

public class Racer extends Player {
    private float currentSpeed;
    private float maxSpeed;
    private float posX;
    private float posY;

    public Racer(int speed, int stamina, int agility, int reaction, float posY) {
        super(speed, stamina, agility, reaction);
        this.posY = posY;
        this.posX = 75;
        currentSpeed = 0;
        maxSpeed = 1;
    }

    public void move(boolean isBounce, float raceTime) {
        if (isBounce) {
            ;
        }
        if ((1000f - reaction) / 5 < raceTime) {
            if (raceTime - (1000f - reaction) / 5 > stamina)
                maxSpeed = Math.max(0.1f * speed / 100, maxSpeed - 0.1f);
            currentSpeed = Math.min(maxSpeed, currentSpeed + (float) speed / 10000);
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
}
