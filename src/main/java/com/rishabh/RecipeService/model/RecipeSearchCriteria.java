package com.rishabh.RecipeService.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RecipeSearchCriteria {

    private Integer pageNumber = 0;

    private Integer pageSize = 10;

    private Boolean isVegetarian;

    private Integer noOfServing;

    private String instructions;

    private List<String> inIngredients;

    private List<String> notInIngredients;
}
