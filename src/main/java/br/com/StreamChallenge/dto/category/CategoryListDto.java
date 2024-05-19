package br.com.StreamChallenge.dto.category;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoryListDto(
        @NotNull
        Long id,
        String title,
        String color
) {
    public CategoryListDto(Category category){
        this(category.getId(), category.getTitle(), category.getColor());
    }
}
