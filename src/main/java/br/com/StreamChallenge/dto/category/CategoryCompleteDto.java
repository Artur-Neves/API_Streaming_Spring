package br.com.StreamChallenge.dto.category;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.video.VideoListDto;

import java.util.List;

public record CategoryCompleteDto(
        Long id,
        String title,
        String color,
        List<VideoListDto> videos
) {
    public CategoryCompleteDto(Category category){
        this(category.getId(), category.getTitle(), category.getColor(), category.getVideo().stream().map(VideoListDto::new).toList());
    }
}
