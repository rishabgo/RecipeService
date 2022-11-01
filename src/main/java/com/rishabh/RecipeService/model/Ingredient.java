package com.rishabh.RecipeService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ingredientName;
    private int calorie;
    private int fat;

    @JsonIgnore
    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipeList;

}

/*
enum INGREDIENT_CATEGORY{
    FRUIT, VEGETABLE, MEAT, OTHER;
}*/
