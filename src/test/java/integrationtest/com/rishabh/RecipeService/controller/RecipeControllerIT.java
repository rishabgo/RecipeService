package com.rishabh.RecipeService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishabh.RecipeService.RecipeServiceApplication;
import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.repository.IngredientsRepository;
import com.rishabh.RecipeService.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RecipeServiceApplication.class)
class RecipeControllerIT extends AbstractContainerBaseTest {

    @Test
    void contextLoads() {
        assertThat(MY_SQL_CONTAINER.isRunning()).isTrue();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        ingredientsRepository.deleteAll();
    }


    @Test
    void saveRecipe() throws JsonProcessingException {

        String postURL = getUrl("/api/v1/recipe");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Recipe recipe = createRecipe();
        String jsonRequest = objectMapper.writeValueAsString(recipe);
        ResponseEntity<Recipe> responseEntity = restTemplate.exchange(postURL, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), Recipe.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getRecipeName()).isEqualTo("Vegetable Sandwich");
    }

    @Test
    void findRecipeById() {

        Recipe recipe = createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        String getURL = getUrl("/api/v1/recipe/" + savedRecipe.getRecipeId());

        ResponseEntity<Recipe> responseEntity = restTemplate.exchange(getURL, HttpMethod.GET, null, Recipe.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getRecipeName()).isEqualTo("Vegetable Sandwich");
    }

    @Test
    void getAllRecipes() throws JsonProcessingException {

        Recipe recipe = createRecipe();
        recipeRepository.save(recipe);

        String getURL = getUrl("/api/v1/recipe/filter");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .isVegetarian(true)
                .noOfServing(1)
                .pageNumber(0)
                .pageSize(10)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(searchCriteria);
        ResponseEntity<Recipe[]> responseEntity = restTemplate.exchange(getURL, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), Recipe[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody())[0].getRecipeName()).isEqualTo("Vegetable Sandwich");
        assertThat(responseEntity.getBody()[0].getNoOfServing()).isEqualTo(1);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @Test
    void getAllRecipesFilteredByInstructions() throws JsonProcessingException {

        Recipe recipe = createRecipe();
        recipeRepository.save(recipe);

        String getURL = getUrl("/api/v1/recipe/filter");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .instructions("Spread tomato sauce")
                .pageNumber(0)
                .pageSize(10)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(searchCriteria);
        ResponseEntity<Recipe[]> responseEntity = restTemplate.exchange(getURL, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), Recipe[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody())[0].getRecipeName()).isEqualTo("Vegetable Sandwich");
        assertThat(responseEntity.getBody()[0].getNoOfServing()).isEqualTo(1);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @Test
    void getAllRecipesFilteredByIngredient() throws JsonProcessingException {

        List<Recipe> recipeList = createMultipleRecipe();
        ;
        recipeRepository.saveAll(recipeList);

        String getURL = getUrl("/api/v1/recipe/filter");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .inIngredients(List.of("Cheese"))
                .pageNumber(0)
                .pageSize(10)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(searchCriteria);
        ResponseEntity<Recipe[]> responseEntity = restTemplate.exchange(getURL, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), Recipe[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody())[0].getRecipeName()).isEqualTo("Vegetable Sandwich");
        assertThat(responseEntity.getBody()[0].getNoOfServing()).isEqualTo(1);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @Test
    void getAllRecipesFilteredByNotInIngredients() throws JsonProcessingException {

        List<Recipe> recipeList = createMultipleRecipe();
        recipeRepository.saveAll(recipeList);

        String getURL = getUrl("/api/v1/recipe/filter");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RecipeSearchCriteria searchCriteria = RecipeSearchCriteria.builder()
                .notInIngredients(List.of("Cheese"))
                .pageNumber(0)
                .pageSize(10)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(searchCriteria);
        ResponseEntity<Recipe[]> responseEntity = restTemplate.exchange(getURL, HttpMethod.POST, new HttpEntity<>(jsonRequest, headers), Recipe[].class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody())[0].getRecipeName()).isEqualTo("Ham Sandwich");
        assertThat(responseEntity.getBody()[0].getNoOfServing()).isEqualTo(2);
        assertThat(responseEntity.getBody()).hasSize(1);
    }

    @Test
    void deleteRecipeById() {

        Recipe recipe = createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        String deleteURL = getUrl("/api/v1/recipe/" + savedRecipe.getRecipeId());

        ResponseEntity<Void> responseEntity = restTemplate.exchange(deleteURL, HttpMethod.DELETE, null, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateRecipeTest() throws JsonProcessingException {

        Recipe recipe = createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        String getURL = getUrl("/api/v1/recipe/" + savedRecipe.getRecipeId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        recipe.setRecipeName("Veg deluxe sandwich");
        String jsonRequest = objectMapper.writeValueAsString(recipe);
        ResponseEntity<Recipe> responseEntity = restTemplate.exchange(getURL, HttpMethod.PUT, new HttpEntity<>(jsonRequest, headers), Recipe.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getRecipeName()).isEqualTo("Veg deluxe sandwich");

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

    private String getUrl(String url) {
        return UriComponentsBuilder.fromUriString("http://localhost:" + port + url).toUriString();
    }

    private List<Recipe> createMultipleRecipe() {
        List<Recipe> recipeList = new ArrayList<>();
        Recipe recipe1 = Recipe.builder()
                .recipeName("Vegetable Sandwich")
                .isVegetarian(true)
                .noOfServing(1)
                .instructions("1) Cut slices for tomatoes. " +
                        "2) Spread tomato sauce on bread" +
                        "3) Add cheese and veggies inside bread")
                .ingredients(List.of(Ingredient.builder().ingredientName("Cheese").calorie(50).fat(100).build(),
                        Ingredient.builder().ingredientName("tomato").build()))
                .build();
        Recipe recipe2 = Recipe.builder()
                .recipeName("Ham Sandwich")
                .isVegetarian(false)
                .noOfServing(2)
                .instructions("1) Cut slices for cucumber, lettuce " +
                        "2) Spread tomato sauce on bread" +
                        "3) Add Ham and veggies inside bread")
                .ingredients(List.of(Ingredient.builder().ingredientName("Ham").calorie(100).fat(200).build(),
                        Ingredient.builder().ingredientName("lettuce").build()))
                .build();

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        return recipeList;
    }
}