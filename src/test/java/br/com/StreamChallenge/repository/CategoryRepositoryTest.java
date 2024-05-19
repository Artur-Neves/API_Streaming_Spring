package br.com.StreamChallenge.repository;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.service.BaseServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


public class CategoryRepositoryTest extends BaseRepositoryTest<CategoryRepository> {

    @DisplayName("Deveria buscar os videos de uma categoria pelo id")
    @Test
    void test01(){
        Category category = randomCategory();
        Page<Video> pageVideos = new PageImpl<Video>(category.getVideo());
        Page<Video> result = repository.findVideosById(category.getId(), Pageable.unpaged());
        Assertions.assertEquals(pageVideos.getContent(), result.getContent());
    }
    @Transactional
    private Category randomCategory (){
        Category c = em.persist(new Category(null, "Ação", "red", List.of(randomVideo())));
        c.getVideo();
        return c;
    }
    private Video randomVideo (){
        return em.persist(new Video(null,  "Video do você sabia", "Este vídeo foi feito pelo você Sabia", "www.youtube.VoceSabia", null));
    }
}
