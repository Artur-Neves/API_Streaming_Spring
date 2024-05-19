package br.com.StreamChallenge.repository;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select v from Category c, Video v where c.id= :id")
    Page<Video> findVideosById(Long id, Pageable pageable);


}
