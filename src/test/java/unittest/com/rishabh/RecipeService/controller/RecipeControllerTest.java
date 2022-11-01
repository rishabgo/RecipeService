package com.rishabh.RecipeService.controller;

import com.rishabh.RecipeService.exception.NotFoundException;
import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.service.RecipeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    private static final String BASE_URL = "/api/v1";

    @MockBean
    private RecipeService recipeService;

    @Autowired
    protected MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveRecipe() throws Exception {

        Recipe recipe = createRecipe();
        String jsonRequest = objectMapper.writeValueAsString(recipe);
        Mockito.when(recipeService.saveRecipe(any())).thenReturn(recipe);

        mockMvc.perform(post(BASE_URL + "/recipe")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipeName", Matchers.is("Vegetable Sandwich")));
    }

    @Test
    void findRecipeById() throws Exception {

        Recipe recipe = createRecipe();
        Mockito.when(recipeService.findRecipeById(any())).thenReturn(recipe);

        ResultActions response = mockMvc.perform(get(BASE_URL + "/recipe/1"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(jsonPath("$.recipeName", Matchers.is("Vegetable Sandwich")));
    }

    @Test
    void getAllRecipes() throws Exception {
        Recipe recipe = createRecipe();
        Mockito.when(recipeService.findAllRecipes(any())).thenReturn(List.of(recipe));

        RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .pageNumber(0)
                .isVegetarian(true)
                .inIngredients(List.of("Ham"))
                .build();
        String jsonRequest = objectMapper.writeValueAsString(searchCriteria);
        mockMvc.perform(post(BASE_URL + "/recipe/filter")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)));
    }

    @Test
    void testGetRecipeWhenRecipeIdIsInvalid() throws Exception {

        Recipe recipe = createRecipe();
        Mockito.when(recipeService.findRecipeById(any())).thenThrow(new NotFoundException("Recipe with given id doesn't exist"));

        ResultActions response = mockMvc.perform(get(BASE_URL + "/recipe/100"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        response.andExpect(result -> assertEquals("Recipe with given id doesn't exist", result.getResponse().getContentAsString()));

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