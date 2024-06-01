package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.category.CategoryListDto;
import br.com.StreamChallenge.dto.video.VideoDto;
import br.com.StreamChallenge.dto.video.VideoDtoComplete;
import br.com.StreamChallenge.dto.video.VideoListDto;
import br.com.StreamChallenge.service.VideoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/videos")
@SecurityRequirement(name = "bearer-key")
public class VideoController {
    @Autowired
    private VideoService service;
    @GetMapping
    ResponseEntity<Page<VideoListDto>> findAllVideos(@PageableDefault(size = 10)  Pageable pageable, @RequestParam(required = false, defaultValue = "") String title){
        return ResponseEntity.ok(service.findAll(pageable, title).map(VideoListDto::new));
    }

    @PostMapping
    ResponseEntity<VideoDto> saveVideos(@Valid @RequestBody VideoDto dto, UriComponentsBuilder uriComponentsBuilder){

        Video v = service.save(new Video(dto));
        URI uri = uriComponentsBuilder.path("/video/{id}").buildAndExpand(v.getId()).toUri();
        return ResponseEntity.created(uri).body(new VideoDto(v));
    }
    @PutMapping()
    ResponseEntity<VideoListDto> updateVideos (@Valid @RequestBody VideoDtoComplete dto){
        return ResponseEntity.ok( new VideoListDto(service.update( new Video(dto))));
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteVideos (@PathVariable Long id){
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    ResponseEntity<VideoDtoComplete> findVideoById (@PathVariable Long id){
        return ResponseEntity.ok(new VideoDtoComplete(service.findById(id)));
    }

}
