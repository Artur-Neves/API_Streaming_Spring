package br.com.StreamChallenge.dto.category;


import br.com.StreamChallenge.domain.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        @NotBlank
        String title,
        @NotBlank
        String color
) {
    public  CategoryDto(Category category){
        this(category.getTitle(), category.getColor());
    }
}
