package com.rishabh.RecipeService.repository;

import com.rishabh.RecipeService.model.Ingredient;
import com.rishabh.RecipeService.model.Recipe;
import com.rishabh.RecipeService.model.RecipeSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class RecipeCriteriaRepository {

    @Autowired
    private EntityManager entityManager;

    public List<Recipe> findAllWithDynamicFilters(final RecipeSearchCriteria recipeSearchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class).distinct(true);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
        Predicate predicate = getPredicate(recipeSearchCriteria, recipeRoot, criteriaBuilder, criteriaQuery);
        criteriaQuery.where(predicate);

        if(recipeSearchCriteria.getSortOrder() != null){
            if (recipeSearchCriteria.getSortOrder().equalsIgnoreCase("ASC")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(recipeRoot.get(recipeSearchCriteria.getSortBy())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(recipeRoot.get(recipeSearchCriteria.getSortBy())));
            }
        }

        TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(recipeSearchCriteria.getPageNumber() * recipeSearchCriteria.getPageSize());
        typedQuery.setMaxResults(recipeSearchCriteria.getPageSize());
        return typedQuery.getResultList();
    }

    private Predicate getPredicate(final RecipeSearchCriteria recipeSearchCriteria,
                                   final Root<Recipe> recipeRoot,
                                   final CriteriaBuilder criteriaBuilder,
                                   final CriteriaQuery criteriaQuery) {
        final List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(recipeSearchCriteria.getIsVegetarian())) {
            predicates.add(
                    criteriaBuilder.equal(recipeRoot.get("isVegetarian"),
                            recipeSearchCriteria.getIsVegetarian()));
        }

        if (Objects.nonNull(recipeSearchCriteria.getNoOfServing())) {
            predicates.add(
                    criteriaBuilder.equal(recipeRoot.get("noOfServing"),
                            recipeSearchCriteria.getNoOfServing()));
        }

        if (Objects.nonNull(recipeSearchCriteria.getInstructions())) {
            predicates.add(
                    criteriaBuilder.like(recipeRoot.get("instructions"),
                            "%" + recipeSearchCriteria.getInstructions() + "%")
            );
        }

        if (Objects.nonNull(recipeSearchCriteria.getInIngredients()) && !recipeSearchCriteria.getInIngredients().isEmpty()) {
            Join<Ingredient, Recipe> ingredientRecipeJoin = recipeRoot.join("ingredients");
            predicates.add(ingredientRecipeJoin.get("ingredientName").in(recipeSearchCriteria.getInIngredients()));
        }

        if (Objects.nonNull(recipeSearchCriteria.getNotInIngredients()) && !recipeSearchCriteria.getNotInIngredients().isEmpty()) {

            for (String ingredientName : recipeSearchCriteria.getNotInIngredients()) {
                // Initialize the subquery
                Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
                Root<Recipe> subQueryRecipe = subQuery.from(Recipe.class);
                Join<Ingredient, Recipe> subQueryIngredient = subQueryRecipe.join("ingredients");

                // Select the Recipe ID where one of their Ingredient matches
                subQuery.select(subQueryRecipe.get("recipeId")).where(
                        criteriaBuilder.equal(subQueryIngredient.get("ingredientName"), ingredientName));

                predicates.add(recipeRoot.get("recipeId").in(subQuery).not());
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
