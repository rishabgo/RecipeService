package com.rishabh.RecipeService.service;

import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;

import java.util.List;

public interface RecipeService {
    Recipe saveRecipe(Recipe recipe);

    Recipe findRecipeById(Long id);

    void deleteRecipeById(Long id);

    List<Recipe> findAllRecipes(RecipeSearchCriteria recipeSearchCriteria);

    Recipe updateRecipe(Recipe recipe);

    void saveAllRecipes(List<Recipe> recipeList);
}
