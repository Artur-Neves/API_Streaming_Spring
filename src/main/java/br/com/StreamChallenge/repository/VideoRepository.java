package br.com.StreamChallenge.repository;

import br.com.StreamChallenge.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findByTitleIgnoreCaseContaining(String search, Pageable pageable);

}
