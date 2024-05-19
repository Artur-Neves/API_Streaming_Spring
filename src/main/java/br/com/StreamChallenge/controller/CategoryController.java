package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import br.com.StreamChallenge.dto.category.CategoryDto;
import br.com.StreamChallenge.dto.category.CategoryListDto;
import br.com.StreamChallenge.dto.video.VideoListDto;
import br.com.StreamChallenge.service.CategoryService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @PostMapping
    public ResponseEntity<CategoryCompleteDto> saveCategory(@Valid @RequestBody CategoryDto dto, UriComponentsBuilder uriComponentsBuilder){
        Category category = service.save(new Category(dto));
        URI uri = uriComponentsBuilder.path("/category/{id}").buildAndExpand(category.getId()).toUri();
        return  ResponseEntity.created(uri).body(new CategoryCompleteDto(category));
    }
    @GetMapping
    public ResponseEntity<Page<CategoryListDto>> findAllCategory(@PageableDefault(size = 10) Pageable pageable){
       return ResponseEntity.ok(service.findAll(pageable).map(CategoryListDto::new));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<CategoryCompleteDto> updateCategory(@RequestBody @Valid CategoryListDto dto){
      return ResponseEntity.ok(new CategoryCompleteDto(service.update(new Category(dto))));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryCompleteDto> findByIdCategory (@PathVariable Long id){
        return ResponseEntity.ok( new CategoryCompleteDto(service.findById(id)));
    }
    @GetMapping("/{id}/videos")
    public ResponseEntity<Page<VideoListDto>> findAllVideosForCategory(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long id){
        return ResponseEntity.ok(service.findVideosById(id, pageable).map(VideoListDto::new));
    }
}
