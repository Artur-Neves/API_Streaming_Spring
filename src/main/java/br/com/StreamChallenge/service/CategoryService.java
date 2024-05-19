package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.video.VideoListDto;
import br.com.StreamChallenge.repository.CategoryRepository;
import br.com.StreamChallenge.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public Category save(Category category) {
        return repository.save(category);
    }

    public Page<Category> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void remove(Long id) {
        Category category = findById(id);
        repository.delete(category);
    }

    @Transactional
    public Category update(Category category) {
        Category cat = findById(category.getId());
        cat.merge(category);
        return cat;
    }

    public Category findById(Long id){
         return repository.findById(id).orElseThrow(()->new EntityNotFoundException("Id não encontrado"));
    }

    public Page<Video> findVideosById(Long id, Pageable pageable) {
     return repository.findVideosById(id, pageable);
    }
}
