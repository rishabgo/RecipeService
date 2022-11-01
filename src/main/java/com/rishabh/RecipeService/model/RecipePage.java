package com.rishabh.RecipeService.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class RecipePage {

    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
}
