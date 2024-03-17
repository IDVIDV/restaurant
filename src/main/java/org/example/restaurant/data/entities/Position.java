package org.example.restaurant.data.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Position extends Entity {
    private String positionName;
    private BigDecimal price;
    private double weight;
    private double protein;
    private double fat;
    private double carbohydrate;
    private boolean isVegan;
    private String ingredients;

    public Position() {}

    public Position(long id, String positionName, BigDecimal price,
                    double weight, double protein, double fat,
                    double carbohydrate, boolean isVegan, String ingredients) {
        super(id);
        this.positionName = positionName;
        this.price = price;
        this.weight = weight;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.isVegan = isVegan;
        this.ingredients = ingredients;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Position position = (Position) o;
        return Double.compare(weight, position.weight) == 0 &&
                Double.compare(protein, position.protein) == 0 &&
                Double.compare(fat, position.fat) == 0 &&
                Double.compare(carbohydrate, position.carbohydrate) == 0 &&
                isVegan == position.isVegan &&
                Objects.equals(positionName, position.positionName) &&
                price.equals(position.price) &&
                Objects.equals(ingredients, position.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), positionName, price,
                weight, protein, fat, carbohydrate, isVegan, ingredients);
    }
}
