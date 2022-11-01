package com.rishabh.RecipeService.service;

import com.rishabh.RecipeService.exception.NotFoundException;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.repository.RecipeCriteriaRepository;
import com.rishabh.RecipeService.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCriteriaRepository recipeCriteriaRepository;

    private static final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);

    public RecipeServiceImpl(final RecipeRepository recipeRepository,
                             final RecipeCriteriaRepository recipeCriteriaRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeCriteriaRepository = recipeCriteriaRepository;
    }

    @Override
    public Recipe saveRecipe(final Recipe recipe) {

        final Recipe savedRecipe = recipeRepository.save(recipe);
        logger.info("Created recipe with id {}", savedRecipe.getRecipeId());
        return savedRecipe;
    }

    @Override
    public Recipe findRecipeById(final Long id) {
        logger.info("Getting recipe with Id {}", id);
        final Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            return optionalRecipe.get();
        } else {
            logger.error("Recipe with given id {} doesn't exist", id);
            throw new NotFoundException("Recipe with given id doesn't exist");
        }
    }

    @Override
    public void deleteRecipeById(final Long id) {
        logger.info("Deleting recipe with id {}", id);
        final Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            recipeRepository.deleteById(id);
        } else {
            logger.error("Recipe with given id {} doesn't exist", id);
            throw new NotFoundException("Recipe with given id doesn't exist");
        }
    }

    @Override
    public List<Recipe> findAllRecipes(RecipeSearchCriteria recipeSearchCriteria) {
        logger.info("Find recipes using dynamic filter {}", recipeSearchCriteria);
        return recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);
    }

    @Override
    public Recipe updateRecipe(final Recipe recipe) {
        logger.info("Updating recipe with id {}", recipe.getRecipeId());
        return recipeRepository.save(recipe);
    }

    @Override
    public void saveAllRecipes(final List<Recipe> recipeList) {
        logger.info("Saving {} new recipes in DB", recipeList.size());
        recipeRepository.saveAll(recipeList);
    }

}
