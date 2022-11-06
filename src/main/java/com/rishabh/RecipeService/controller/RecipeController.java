package com.rishabh.RecipeService.controller;

import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.payload.PostResponse;
import com.rishabh.RecipeService.service.RecipeService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Object> findRecipeById(@PathVariable final Long id) {
        try {
            Recipe recipe = recipeService.findRecipeById(id);
            return new ResponseEntity<>(recipe, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recipes")
    public ResponseEntity<PostResponse> findAllRecipes(
            @RequestParam(value = "pageNo", required = false) final int pageNo,
            @RequestParam(value = "pageSize", required = false) final int pageSize,
            @RequestParam(value = "sortBy", required = false) final String sortBy,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "ASC") final String sortOrder) {

        PostResponse response = recipeService.findAllRecipesWithPagination(pageNo, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/recipes")
    public ResponseEntity<Integer> saveRecipes(@RequestBody List<Recipe> recipeList) {

        recipeService.saveAllRecipes(recipeList);
        return new ResponseEntity<>(recipeList.size(), HttpStatus.CREATED);
    }

    @PostMapping("/recipe")
    public ResponseEntity<Recipe> saveRecipe(@NonNull @RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
    }

    @PostMapping("/recipe/filter")
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestBody RecipeSearchCriteria recipeSearchCriteria) {

        List<Recipe> recipeList = recipeService.findAllRecipes(recipeSearchCriteria);
        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable final Long id) {
        recipeService.deleteRecipeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe) {

        Recipe updatedRecipe = recipeService.updateRecipe(recipe);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

}
