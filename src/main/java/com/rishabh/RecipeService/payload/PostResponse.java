package com.rishabh.RecipeService.payload;

import com.rishabh.RecipeService.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private List<Recipe> recipeList;
    private int pageNo;
    private int pageSize;
    private int totalElements;
    private int totalPages;
    private boolean lastPage;
}
