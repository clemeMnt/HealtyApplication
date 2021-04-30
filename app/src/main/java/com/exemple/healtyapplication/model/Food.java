package com.exemple.healtyapplication.model;

public class Food {

    private Double sugarG;
    private Double fiberG;
    private Long sodiumMg;
    private String name;
    private Long  potasiumMg;
    private Double calories;
    private Double proteinG;
    private Double fatTotalG;



    public Food(Double sugarG, Double fiberG, Long sodiumMg, String name, Long potasiumMg, Double calories, Double proteinG, Double fatTotalG) {
        this.sugarG = sugarG;
        this.fiberG = fiberG;
        this.sodiumMg = sodiumMg;
        this.name = name;
        this.potasiumMg = potasiumMg;
        this.calories = calories;
        this.proteinG = proteinG;
        this.fatTotalG = fatTotalG;
    }

    public Food() {
    }

    public Double getSugarG() {
        return sugarG;
    }

    public Double getFiberG() {
        return fiberG;
    }

    public Long getSodiumMg() {
        return sodiumMg;
    }

    public String getName() {
        return name;
    }

    public Long getPotasiumMg() {
        return potasiumMg;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getProteinG() {
        return proteinG;
    }

    public Double getFatTotalG() {
        return fatTotalG;
    }
}
