package com.rishabh.RecipeService.repository;

import com.rishabh.RecipeService.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {
}
