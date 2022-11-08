package com.rishabh.RecipeService.service;

import com.rishabh.RecipeService.exception.NotFoundException;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import com.rishabh.RecipeService.payload.PostResponse;
import com.rishabh.RecipeService.repository.RecipeCriteriaRepository;
import com.rishabh.RecipeService.repository.RecipeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCriteriaRepository recipeCriteriaRepository;

    @Override
    public Recipe saveRecipe(final Recipe recipe) {

        final Recipe savedRecipe = recipeRepository.save(recipe);
        log.info("Created recipe with id {}", savedRecipe.getRecipeId());
        return savedRecipe;
    }

    @Override
    public Recipe findRecipeById(final Long id) {
        log.info("Getting recipe with Id {}", id);
        final Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            return optionalRecipe.get();
        } else {
            log.error("Recipe with given id {} doesn't exist", id);
            throw new NotFoundException("Recipe with given id doesn't exist");
        }
    }

    @Override
    public void deleteRecipeById(final Long id) {
        log.info("Deleting recipe with id {}", id);
        final Optional<Recipe> optionalRecipe = recipeRepository.findById(id);

        if (optionalRecipe.isPresent()) {
            recipeRepository.deleteById(id);
        } else {
            log.error("Recipe with given id {} doesn't exist", id);
            throw new NotFoundException("Recipe with given id doesn't exist");
        }
    }

    @Override
    public List<Recipe> findAllRecipes(RecipeSearchCriteria recipeSearchCriteria) {
        log.info("Find recipes using dynamic filter {}", recipeSearchCriteria);
        return recipeCriteriaRepository.findAllWithDynamicFilters(recipeSearchCriteria);
    }

    @Override
    public Recipe updateRecipe(final Recipe recipe) {
        log.info("Updating recipe with id {}", recipe.getRecipeId());
        return recipeRepository.save(recipe);
    }

    @Override
    public void saveAllRecipes(final List<Recipe> recipeList) {
        log.info("Saving {} new recipes in DB", recipeList.size());
        recipeRepository.saveAll(recipeList);
    }

    @Override
    public PostResponse findAllRecipesWithPagination(final int pageNo,
                                                     final int pageSize,
                                                     final String sortBy,
                                                     final String sortOrder) {

        Sort sort = (sortOrder.equals("ASC")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Recipe> recipePage = recipeRepository.findAll(pageable);

        return PostResponse.builder()
                .recipeList(recipePage.getContent())
                .pageNo(recipePage.getNumber())
                .pageSize(recipePage.getSize())
                .totalElements(recipePage.getNumberOfElements())
                .totalPages(recipePage.getTotalPages())
                .lastPage(recipePage.isLast())
                .build();
    }

}
