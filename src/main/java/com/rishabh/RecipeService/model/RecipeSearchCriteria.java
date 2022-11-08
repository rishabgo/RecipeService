package com.rishabh.RecipeService.model;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
