package br.com.StreamChallenge.dto.video;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import br.com.StreamChallenge.dto.category.CategoryDto;

public record VideoDtoComplete(
        long id,
        String title,
        String description,
        String url,
        CategoryDto category
) {
    public VideoDtoComplete(Video video){
        this(video.getId(), video.getTitle(), video.getDescription(), video.getUrl(), new CategoryDto(video.getCategory()));
    }
}
