package br.com.StreamChallenge.service;

import br.com.StreamChallenge.controller.BaseControllerTest;
import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.repository.VideoRepository;
import com.sun.istack.Pool;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class VideoServiceTest extends BaseServiceTest<VideoService> {
    @InjectMocks
    private VideoService service;
    @Mock
    private Pageable pageable;
    @Mock
    private VideoRepository repository;
    @Mock
    private Video videoMock;



    @DisplayName("O service deve chamar o repository.save e deve retorna-lo")
    @Test
    void test01(){
        Video video = randomVideo();
        given(repository.save(video)).willReturn(video);
        Assertions.assertEquals(service.save(video), video);
        then(repository).should().save(video);
    }
    @DisplayName("O service deve chamar o método findById o qual deve retornar o video que foi passado")
    @Test
    void test02(){
        Video video = randomVideo();
        given(repository.findById(video.getId())).willReturn(Optional.of(video));
        Assertions.assertEquals(service.findById(video.getId()), video);
        then(repository).should().findById(video.getId());
    }
    @DisplayName("O service deve chamar o repository.remove")
    @Test
    void test03(){
        Video video = randomVideo();
        given(repository.findById(video.getId())).willReturn(Optional.of(video));
        service.remove(video.getId());
        then(repository).should().delete(video);
    }
    @DisplayName("O service deve chamar o repository.remove")
    @Test
    void test04(){
        Video video = randomVideo();
        given(repository.findById(video.getId())).willReturn(Optional.empty());
         Assertions.assertThrows(EntityNotFoundException.class,()-> service.remove(video.getId()));

    }
    @DisplayName("O service deve chamar o merge além de retonar o novo objeto")
    @Test
    void test05(){
        Video video1 = randomVideo();
        given(repository.findById(video1.getId())).willReturn(Optional.of(videoMock));
        Assertions.assertEquals(service.update(video1), videoMock);
        then(videoMock).should().merge(video1);
    }
    @DisplayName("O service deve chamar o findByALl passando o page correto")
    @Test
    void test06(){
        Video video1 = randomVideo();
        Page<Video> videoPage = new PageImpl<Video>(List.of(video1));
        given(repository.findByTitleIgnoreCaseContaining(eq(""), any())).willReturn(videoPage);
        Assertions.assertEquals(service.findAll(pageable, ""), videoPage);
    }

    private Video randomVideo (){
        return new Video(1L, "Video do você sabia", "Este vídeo foi feito pelo você Sabia", "www.youtube.VoceSabia", new Category(1L));
    }
}

