package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.repository.VideoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository repository;

    public Video save(Video video){
        return repository.save(video);
    }

    public Page<Video> findAll(Pageable page, String title) {
    return repository.findByTitleIgnoreCaseContaining(title, page);
    }

    public void remove(Long id) {
       Video video= findById(id);
        repository.delete(video);
    }
    @Transactional()
    public Video update(Video v) {
        Video video = findById(v.getId());
        video.merge(v);
        return video;
    }

    public Video findById(Long id) {
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id não encontrado"));
    }


}
