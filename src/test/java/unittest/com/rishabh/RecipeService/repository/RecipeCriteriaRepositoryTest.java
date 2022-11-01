package com.rishabh.RecipeService.repository;

import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RecipeCriteriaRepository.class)
class RecipeCriteriaRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCriteriaRepository recipeCriteriaRepository;

    @Test
    void findAllRecipesFilteredByIsVegetarian() {

        recipeRepository.saveAll(createMultipleRecipe());

        RecipeSearchCriteria recipeSearchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .pageSize(10)
                .isVegetarian(true)
                .build();

        List<Recipe> recipeList = recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);

        assertThat(recipeList.get(0).isVegetarian()).isTrue();
        assertThat(recipeList).hasSize(1);
    }

    @Test
    void findAllRecipesFilteredByInstruction() {

        recipeRepository.saveAll(createMultipleRecipe());

        RecipeSearchCriteria recipeSearchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .pageSize(10)
                .instructions("Spread tomato sauce")
                .build();

        List<Recipe> recipeList = recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);

        assertThat(recipeList).hasSize(2);
    }

    @Test
    void findAllRecipesFilteredByNoOfServing() {

        recipeRepository.saveAll(createMultipleRecipe());

        RecipeSearchCriteria recipeSearchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .pageSize(10)
                .noOfServing(2)
                .build();

        List<Recipe> recipeList = recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);

        assertThat(recipeList.get(0).getRecipeName()).isEqualTo("Ham Sandwich");
        assertThat(recipeList).hasSize(1);
    }

    @Test
    void findAllRecipesFilteredByMatchingIngredients() {

        recipeRepository.saveAll(createMultipleRecipe());

        RecipeSearchCriteria recipeSearchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .pageSize(10)
                .inIngredients(List.of("Cheese", "tomato"))
                .build();

        List<Recipe> recipeList = recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);

        assertThat(recipeList.get(0).isVegetarian()).isTrue();
        assertThat(recipeList).hasSize(1);
    }

    @Test
    void findAllRecipesFilteredByNotMatchingIngredients() {

        recipeRepository.saveAll(createMultipleRecipe());

        RecipeSearchCriteria recipeSearchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .pageSize(10)
                .notInIngredients(List.of("Ham"))
                .build();

        List<Recipe> recipeList = recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);

        assertThat(recipeList.get(0).isVegetarian()).isTrue();
        assertThat(recipeList).hasSize(1);
    }

    private List<Recipe> createMultipleRecipe() {
        List<Recipe> recipeList = new ArrayList<>();
        Recipe recipe1 = Recipe.builder()
                .recipeName("Vegetable Sandwich")
                .isVegetarian(true)
                .noOfServing(1)
                .instructions("1) Cut slices for cucumber, onion, tomatoes. " +
                        "2) Spread tomato sauce on bread" +
                        "3) Add cheese and veggies inside bread")
                .ingredients(List.of(Ingredient.builder().ingredientName("Cheese").calorie(50).fat(100).build()))
                .build();
        Recipe recipe2 = Recipe.builder()
                .recipeName("Ham Sandwich")
                .isVegetarian(false)
                .noOfServing(2)
                .instructions("1) Cut slices for cucumber, onion, tomatoes. " +
                        "2) Spread tomato sauce on bread" +
                        "3) Add Ham and veggies inside bread")
                .ingredients(List.of(Ingredient.builder().ingredientName("Ham").calorie(100).fat(200).build()))
                .build();

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        return recipeList;
    }
}