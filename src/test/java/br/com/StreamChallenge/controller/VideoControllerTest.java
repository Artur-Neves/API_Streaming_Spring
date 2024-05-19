package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.video.VideoDto;
import br.com.StreamChallenge.dto.video.VideoDtoComplete;
import br.com.StreamChallenge.dto.video.VideoListDto;
import br.com.StreamChallenge.service.VideoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


 class VideoControllerTest extends BaseControllerTest<VideoService> {
    @Autowired
    private JacksonTester<Page<VideoDto>> pageVideoDtoJackson;
     @Autowired
     private JacksonTester<Page<VideoListDto>> pageVideoListDtoJackson;
    @Autowired
    private JacksonTester<VideoDto> videoDtoJackson;
    @Autowired
    private JacksonTester<VideoDtoComplete> videoDtoCompleteJackson;
    @Autowired
    private JacksonTester<VideoListDto> videoListDtoJackson;
    @Mock
    private VideoDto dto;
    @WithMockUser
    @DisplayName(" QUando solicitado o listamento dos vídeos" +
            "Deve-se retornar uma lista com os videos")
    @Test
    void test1() throws Exception {
        Page<Video> video =  new PageImpl<Video>(List.of(randomVideo()));
        Page<VideoListDto> dtoPage = video.map(VideoListDto::new);
        BDDMockito.given(service.findAll(any(), eq(""))).willReturn(video);
        mvc.perform(get("/videos")).andExpectAll(status().isOk(),
                content().json(pageVideoListDtoJackson.write(dtoPage).getJson())
        );
    }
     @WithMockUser(authorities= "ADMIN")
        @DisplayName("Ao realizar-se a salvamento do video " +
                "deve-se retornar o codigo 201, bem como o cabeçalho com o link e o corpo contendo o dto salvo")
        @Test
        void test2() throws Exception {
        dto = new VideoDto(randomVideo());
        when(service.save(any())).thenReturn(randomVideo());
            mvc.perform(post("/videos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(videoDtoJackson.write(dto).getJson()))
                    .andExpectAll(status().isCreated(),
                            content().json(videoDtoJackson.write(dto).getJson()));

        }
     @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete " +
            "Deve-se retornar o codigo 204 e não deve retornar nada")
    @Test
    void test3() throws Exception {
        Long id = anyLong();
        mvc.perform(delete("/videos/"+id))
                .andExpectAll(status().isNoContent());
        then(service).should().remove(id);

    }
     @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete, mas passando um tipo diferente do que o long " +
            "Deve-se retornar um badRequest")
    @Test
    void test4() throws Exception {
        String id ="Artur";
        mvc.perform(delete("/videos/"+id))
                .andExpectAll(status().isBadRequest());

    }
     @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete, mas passando um id que não existe no banco de dados " +
            "Deve-se retornar um error 404")
    @Test
    void test5() throws Exception {

        mvc.perform(delete("/videos/"))
                .andExpectAll(status().isNotFound());
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete " +
            "Deve-se retornar o codigo 204 e não deve retornar nada")
    @Test
    void test6() throws Exception {
        Video video = randomVideo();
        mvc.perform(delete("/videos/"+video.getId()))
                .andExpectAll(status().isNoContent());
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o update " +
            "Deve-se retornar o codigo 200 e contendo a entidade persistida")
    @Test
    void test7() throws Exception {
        Video video = randomVideo();
        when(service.update(any())).thenReturn(video);
        mvc.perform(put("/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(videoDtoCompleteJackson.write( new VideoDtoComplete(video)).getJson()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(videoListDtoJackson.write(new VideoListDto(video)).getJson()));
    }
    @WithMockUser
    @DisplayName("Ao consultar de um video por id " +
            "Deve-se retornar o codigo 200 e contendo a o video")
    @Test
    void test8() throws Exception {
        Video v = randomVideo();
        when(service.findById(v.getId())).thenReturn(v);
        mvc.perform(get("/videos/"+v.getId()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(videoDtoCompleteJackson.write(new VideoDtoComplete(v)).getJson()));
    }
    @WithMockUser
    @DisplayName("Ao consultar de um video por id que não existe " +
            "Deve-se retornar o codigo 404")
    @Test
    void test9() throws Exception {
        Video v = randomVideo();
        when(service.findById(v.getId())).thenThrow(EntityNotFoundException.class);
        mvc.perform(get("/videos/"+v.getId()))
                .andExpectAll(status().isNotFound());
    }
     @WithMockUser
     @DisplayName("Ao consultar de um video com o titulo  " +
             "Deve-se retornar o vídeo e o status ok")
     @Test
     void test10() throws Exception {
         Video v = randomVideo();
         Page<Video> videos = new PageImpl<Video>(List.of(v));
         when(service.findAll(any(Pageable.class), eq(v.getTitle()))).thenReturn(videos);
         mvc.perform(get("/videos?title="+v.getTitle()))
                 .andExpectAll(status().isOk(),
                         content().contentType(MediaType.APPLICATION_JSON),
                         content().json(pageVideoListDtoJackson.write(videos.map(VideoListDto::new)).getJson()));
     }


    private Video randomVideo (){
        return new Video(1L, "Video do você sabia", "Este vídeo foi feito pelo você Sabia", "www.youtube.VoceSabia", new Category(1L, "title", "color", null));
        }

}
