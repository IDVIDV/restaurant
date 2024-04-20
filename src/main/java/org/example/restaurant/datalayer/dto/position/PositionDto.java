package org.example.restaurant.datalayer.dto.position;

import java.math.BigDecimal;
import java.util.Objects;

public class PositionDto {
    private Long id;
    private String positionName;
    private BigDecimal price;
    private Double weight;
    private Double protein;
    private Double fat;
    private Double carbohydrate;
    private Boolean isVegan;
    private String ingredients;

    public PositionDto() {
    }

    public PositionDto(Long id, String positionName,
                       BigDecimal price, Double weight,
                       Double protein, Double fat,
                       Double carbohydrate, Boolean isVegan,
                       String ingredients) {
        this.id = id;
        this.positionName = positionName;
        this.price = price;
        this.weight = weight;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.isVegan = isVegan;
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getVegan() {
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
        PositionDto that = (PositionDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(positionName, that.positionName) &&
                Objects.equals(price, that.price) &&
                Objects.equals(weight, that.weight) &&
                Objects.equals(protein, that.protein) &&
                Objects.equals(fat, that.fat) &&
                Objects.equals(carbohydrate, that.carbohydrate) &&
                Objects.equals(isVegan, that.isVegan) &&
                Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, positionName,
                price, weight,
                protein, fat,
                carbohydrate, isVegan,
                ingredients);
    }
}
