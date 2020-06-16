package com.example.crazyruns.game;

public class Player {
    protected int speed;
    protected int stamina;
    protected int agility;
    protected int reaction;
    protected String name;
    protected int points;
    protected int listNumber;

    public Player(int speed, int stamina, int agility, int reaction) {
        this.speed = speed;
        this.stamina = stamina;
        this.agility = agility;
        this.reaction = reaction;
        name = "Some Name " + String.valueOf(this.speed);
        points = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getListNumber() {
        return listNumber;
    }

    public void setListNumber(int listNumber) {
        this.listNumber = listNumber;
    }
}
