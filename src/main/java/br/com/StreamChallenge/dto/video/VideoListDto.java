package br.com.StreamChallenge.dto.video;

import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.category.CategoryDto;
import jakarta.validation.constraints.NotNull;

public record VideoListDto(
                           long id,
                           String title,
                           String description,
                           String url
) {
    public VideoListDto(Video video){
        this(video.getId(), video.getTitle(), video.getDescription(), video.getUrl());
    }
}
