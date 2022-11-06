package com.rishabh.RecipeService.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RecipeSearchCriteria {

    private Integer pageNumber;

    private Integer pageSize;

    private String sortBy;

    private String sortOrder;

    private Boolean isVegetarian;

    private Integer noOfServing;

    private String instructions;

    private List<String> inIngredients;

    private List<String> notInIngredients;
}
