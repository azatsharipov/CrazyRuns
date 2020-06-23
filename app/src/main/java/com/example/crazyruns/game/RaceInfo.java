package com.example.crazyruns.game;

public class RaceInfo {
    private int raceNumber;
    private int distance;
    private int jumpsAmount;


    public RaceInfo(int raceNumber, int distance, int jumpsAmount) {
        this.raceNumber = raceNumber;
        this.distance = distance;
        this.jumpsAmount = jumpsAmount;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getJumpsAmount() {
        return jumpsAmount;
    }

    public void setJumpsAmount(int jumpsAmount) {
        this.jumpsAmount = jumpsAmount;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }
}
