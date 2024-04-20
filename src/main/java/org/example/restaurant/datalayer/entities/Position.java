package org.example.restaurant.datalayer.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Position extends Entity {
    private String positionName;
    private BigDecimal price;
    private Double weight;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Boolean isVegan;
    private String ingredients;

    public Position() {
    }

    public Position(Long id, String positionName,
                    BigDecimal price, Double weight,
                    Double protein, Double fat,
                    Double carbohydrate, Boolean isVegan,
                    String ingredients) {
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Boolean isVegan() {
        return isVegan;
    }

    public void setVegan(Boolean vegan) {
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
        Position position = (Position) o;
        return Objects.equals(positionName, position.positionName) && Objects.equals(price, position.price) && Objects.equals(weight, position.weight) && Objects.equals(protein, position.protein) && Objects.equals(fat, position.fat) && Objects.equals(carbohydrate, position.carbohydrate) && Objects.equals(isVegan, position.isVegan) && Objects.equals(ingredients, position.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionName, price, weight, protein, fat, carbohydrate, isVegan, ingredients);
    }
}
