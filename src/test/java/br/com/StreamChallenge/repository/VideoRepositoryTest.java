package br.com.StreamChallenge.repository;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class VideoRepositoryTest extends BaseRepositoryTest<VideoRepository> {
    @DisplayName("Deveria buscar todos os videos quando não passamos um titulo")
    @Test
    void test01(){
        Video video1 = randomVideo();
        Video video2 = randomVideo2();
        Page<Video> pageVideos = new PageImpl<Video>(List.of(video1, video2));
        Page<Video> result = repository.findByTitleIgnoreCaseContaining("", Pageable.unpaged());
        Assertions.assertEquals(pageVideos.getContent(), result.getContent());
    }
    @DisplayName("Deveria buscar apenas aqueles que tiverem o determinado trecho do titulo")
    @Test
    void test02(){
        Video video1 = randomVideo();
        randomVideo2();
        Page<Video> pageVideos = new PageImpl<Video>(List.of(video1));
        Page<Video> result = repository.findByTitleIgnoreCaseContaining(video1.getTitle(), Pageable.unpaged());
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
    private Video randomVideo2 (){
        return em.persist(new Video(null,  "Manual do mundo", "Este vídeo foi feito pelo Manual do mundo", "www.youtube.Manual do mundo", null));
    }
}
