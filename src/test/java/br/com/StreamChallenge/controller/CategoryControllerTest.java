package br.com.StreamChallenge.controller;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import br.com.StreamChallenge.dto.category.CategoryDto;
import br.com.StreamChallenge.dto.video.VideoDto;
import br.com.StreamChallenge.dto.video.VideoDtoComplete;
import br.com.StreamChallenge.dto.video.VideoListDto;
import br.com.StreamChallenge.repository.CategoryRepository;
import br.com.StreamChallenge.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends BaseControllerTest<CategoryService> {
    @MockBean
    protected CategoryService service;
    @Autowired
    private JacksonTester<Page<CategoryDto>> pageCategoryDtoJackson;
    @Autowired
    private JacksonTester<Page<VideoListDto>> pageVideoListDtoJackson;
    @Autowired
    private JacksonTester<CategoryDto> categoryDtoJackson;
    @Autowired
    private JacksonTester<CategoryCompleteDto> categoryCompleteDtoJackson;
    @Mock
    private CategoryDto dto;
    @Mock
    private Page<Video> pageVideo;
    @Mock
    private Pageable pageable;
    @WithMockUser()
    @DisplayName(" QUando solicitado o listamento das categorias " +
            "Deve-se retornar uma lista com os categorys")
    @Test
    void test1() throws Exception {
        Page<CategoryDto> dtoPage = new PageImpl<CategoryDto>(List.of(new CategoryDto(randomCategory())));
        Page<Category> category =  new PageImpl<Category>(List.of(randomCategory()));
        BDDMockito.given(service.findAll(any())).willReturn(category);
        mvc.perform(get("/category")).andExpectAll(status().isOk(),
                content().json(pageCategoryDtoJackson.write(dtoPage).getJson())
        );
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar-se a salvamento da categoria " +
            "deve-se retornar o codigo 201, bem como o cabeçalho com o link e o corpo contendo o dto salvo")
    @Test
    void test2() throws Exception {
        dto = new CategoryDto(randomCategory());
        when(service.save(any())).thenReturn(randomCategory());
        mvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryDtoJackson.write(dto).getJson()))
                .andExpectAll(status().isCreated(),
                        content().json(categoryCompleteDtoJackson.write(new CategoryCompleteDto(randomCategory())).getJson()));

    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete " +
            "Deve-se retornar o codigo 204 e não deve retornar nada")
    @Test
    void test3() throws Exception {
        Long id = anyLong();
        mvc.perform(delete("/category/"+id))
                .andExpectAll(status().isNoContent());
        then(service).should().remove(id);

    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete, mas passando um tipo diferente do que o long " +
            "Deve-se retornar um badRequest")
    @Test
    void test4() throws Exception {
        String id ="Artur";
        mvc.perform(delete("/category/"+id))
                .andExpectAll(status().isBadRequest());

    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete, mas passando um id que não existe no banco de dados " +
            "Deve-se retornar um error 404")
    @Test
    void test5() throws Exception {

        mvc.perform(delete("/category/"))
                .andExpectAll(status().isNotFound());
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o delete " +
            "Deve-se retornar o codigo 204 e não deve retornar nada")
    @Test
    void test6() throws Exception {
        Category category = randomCategory();
        mvc.perform(delete("/category/"+category.getId()))
                .andExpectAll(status().isNoContent());
    }
    @WithMockUser(authorities= "ADMIN")
    @DisplayName("Ao realizar o update " +
            "Deve-se retornar o codigo 200 e contendo a entidade persistida")
    @Test
    void test7() throws Exception {
        Category category = randomCategory();
        when(service.update(any())).thenReturn(category);
        mvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryCompleteDtoJackson.write( new CategoryCompleteDto(category)).getJson()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(categoryCompleteDtoJackson.write(new CategoryCompleteDto(category)).getJson()));
    }
    @WithMockUser
    @DisplayName("Ao a consulta de um category por id " +
            "Deve-se retornar o codigo 200 e contendo a o category")
    @Test
    void test8() throws Exception {
        Category v = randomCategory();
        when(service.findById(v.getId())).thenReturn(v);
        mvc.perform(get("/category/"+v.getId()))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(categoryCompleteDtoJackson.write(new CategoryCompleteDto(v)).getJson()));
    }
    @WithMockUser
    @DisplayName("Ao a consulta de um category por id que não existe " +
            "Deve-se retornar o codigo 404")
    @Test
    void test9() throws Exception {
        Category v = randomCategory();
        when(service.findById(v.getId())).thenThrow(EntityNotFoundException.class);
        mvc.perform(get("/category/"+v.getId()))
                .andExpectAll(status().isNotFound());
    }
    @WithMockUser
    @DisplayName("Ao realizar a consulta deve-se retornar uma lista com os determinados" +
            "videos")
    @Test
    void test10() throws Exception {
        Category g =randomCategory();
        Page<Video> video =  new PageImpl<Video>(List.of(randomVideo()));
        Page<VideoListDto> videoListDto =video.map(VideoListDto::new);
        given(service.findVideosById(eq(g.getId()), any(Pageable.class))).willReturn(video);
        mvc.perform(get("/category/"+g.getId()+"/videos"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(pageVideoListDtoJackson.write(videoListDto).getJson()));
    }



    private Category randomCategory (){
        return new Category(1L, "Ação", "red", List.of(randomVideo()));
    }
    private Video randomVideo (){
        return new Video(1L, "Video do você sabia", "Este vídeo foi feito pelo você Sabia", "www.youtube.VoceSabia", new Category(1L));
    }

}