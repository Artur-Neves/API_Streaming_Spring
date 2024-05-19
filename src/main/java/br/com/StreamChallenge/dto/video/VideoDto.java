package br.com.StreamChallenge.dto.video;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VideoDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String url,

        Long idCategory) {
    public VideoDto(Video video){
        this(video.getTitle(), video.getDescription(), video.getUrl(), video.getCategory().getId());
    }
}
