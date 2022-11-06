package com.rishabh.RecipeService.service;

import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.payload.PostResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecipeService {
    Recipe saveRecipe(Recipe recipe);

    Recipe findRecipeById(Long id);

    void deleteRecipeById(Long id);

    List<Recipe> findAllRecipes(RecipeSearchCriteria recipeSearchCriteria);

    Recipe updateRecipe(Recipe recipe);

    void saveAllRecipes(List<Recipe> recipeList);

    PostResponse findAllRecipesWithPagination(int pageNo, int pageSize, String sortBy, String sortOrder);
}
