package com.rishabh.RecipeService.repository;

import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void saveRecipeTest(){

        //given
         Recipe recipe = createRecipe();

         //when
         recipeRepository.save(recipe);

         //then
        assertThat(recipe.getRecipeId()).isGreaterThan(0);
    }

    @Test
    void getRecipeByIdTest() {

        Recipe recipe = recipeRepository.save(createRecipe());

        Recipe fetchedRecipe = recipeRepository.findById(recipe.getRecipeId()).get();

        assertThat(fetchedRecipe.getRecipeName()).isEqualTo("Vegetable Sandwich");
        assertThat(fetchedRecipe.getNoOfServing()).isEqualTo(1);
    }

    @Test
    void updateRecipeTest(){

        Recipe recipe = recipeRepository.save(createRecipe());

        Recipe updatedRecipe = Recipe.builder()
                .recipeName("Veg deluxe sandwich")
                .recipeId(recipe.getRecipeId())
                .noOfServing(2)
                .isVegetarian(true)
                .build();

        Recipe updateRecipe = recipeRepository.save(updatedRecipe);

        assertThat(updateRecipe.getRecipeName()).isEqualTo("Veg deluxe sandwich");
        assertThat(updateRecipe.getNoOfServing()).isEqualTo(2);

    }

    @Test
    void deleteRecipeTest(){
        Recipe recipe = recipeRepository.save(createRecipe());

        Long recipeId = recipe.getRecipeId();
        Recipe fetchedRecipe = recipeRepository.findById(recipeId).get();

        recipeRepository.deleteById(recipeId);

        Recipe deleteRecipe = recipeRepository.findById(recipeId).orElse(null);
        assertThat(deleteRecipe).isNull();
    }

    private Recipe createRecipe() {
        return Recipe.builder()
                .recipeName("Vegetable Sandwich")
                .isVegetarian(true)
                .noOfServing(1)
                .instructions("1) Cut slices for cucumber, onion, tomatoes. " +
                        "2) Spread tomato sauce on bread" +
                        "3) Add cheese and veggies inside bread")
                .ingredients(List.of(Ingredient.builder().ingredientName("Cheese").calorie(50).fat(100).build(),
                        Ingredient.builder().ingredientName("Cucumber").build(),
                        Ingredient.builder().ingredientName("tomato").build()))
                .build();
    }
}