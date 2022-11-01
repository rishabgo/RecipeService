package com.rishabh.RecipeService.controller;

import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.repository.IngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class IngredientController {

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @PostMapping("/ingredient")
    public ResponseEntity<Ingredient> createIngredient(@NonNull @RequestBody Ingredient ingredient) {

        Ingredient savedIng = ingredientsRepository.save(ingredient);
        return new ResponseEntity<>(savedIng, HttpStatus.CREATED);
    }

    @GetMapping("/ingredient")
    public ResponseEntity<Ingredient> getIngredientById(final Long id) {
        Ingredient ingredient = ingredientsRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient doesn't exist with given id"));
        return new ResponseEntity<>(ingredient, HttpStatus.OK);
    }
}
