package com.example.crazyruns.game;

public class Player {
    protected int speed;
    protected int stamina;
    protected int agility;
    protected int reaction;
    protected String name;
    protected int points;
    protected int listNumber;
    protected int playerNumber;
    protected int availablePoints;

    public Player(String name, int speed, int stamina, int agility, int reaction) {
        this.speed = speed;
        this.stamina = stamina;
        this.agility = agility;
        this.reaction = reaction;
        this.name = name;
        points = 0;
        availablePoints = 2;
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

    public void setName(String name) {
        this.name = name;
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

    public int getPower() {
        return speed + stamina + agility + reaction;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
