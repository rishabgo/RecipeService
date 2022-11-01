package com.rishabh.RecipeService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recipeId;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "vegetarian")
    private boolean isVegetarian;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;

    @Column(name = "serving")
    private int noOfServing;

    private String instructions;
}
