package br.com.StreamChallenge.service;

import br.com.StreamChallenge.domain.Category;
import br.com.StreamChallenge.domain.Video;
import br.com.StreamChallenge.repository.CategoryRepository;
import br.com.StreamChallenge.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    protected CategoryService service;
    @Mock
    protected Pageable pageable;
    @Mock
    private CategoryRepository repository;
    @Mock
    protected Category categoryMock;


    @DisplayName("O service deve chamar o repository.save e deve retorna-lo")
    @Test
    void test01(){
        Category category = randomCategory();
        given(repository.save(category)).willReturn(category);
        Assertions.assertEquals(service.save(category), category);
        then(repository).should().save(category);
    }
    @DisplayName("O service deve chamar o método findById o qual deve retornar o category que foi passado")
    @Test
    void test02(){
        Category category = randomCategory();
        given(repository.findById(category.getId())).willReturn(Optional.of(category));
        Assertions.assertEquals(service.findById(category.getId()), category);
        then(repository).should().findById(category.getId());
    }
    @DisplayName("O service deve chamar o repository.remove")
    @Test
    void test03(){
        Category category = randomCategory();
        given(repository.findById(category.getId())).willReturn(Optional.of(category));
        service.remove(category.getId());
        then(repository).should().delete(category);
    }
    @DisplayName("O service deve chamar o repository.remove")
    @Test
    void test04(){
        Category category = randomCategory();
        given(repository.findById(category.getId())).willReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,()-> service.remove(category.getId()));

    }
    @DisplayName("O service deve chamar o merge além de retonar o novo objeto")
    @Test
    void test05(){
        Category category1 = randomCategory();
        given(repository.findById(category1.getId())).willReturn(Optional.of(categoryMock));
        Assertions.assertEquals(service.update(category1), categoryMock);
        then(categoryMock).should().merge(category1);
    }
    @DisplayName("O service deve chamar o findByALl passando o page correto")
    @Test
    void test06(){
        Category category1 = randomCategory();
        Page<Category> categoryPage = new PageImpl<Category>(List.of(randomCategory()));
        given(repository.findAll(pageable)).willReturn(categoryPage);
        Assertions.assertEquals(service.findAll(pageable), categoryPage);
    }
    @DisplayName("O service deve chamar o findVideosById passando o page correto")
    @Test
    void test07(){
        Category category1 = randomCategory();
        Page<Video> videoPage = new PageImpl<Video>(List.of(randomVideo()));
        given(repository.findVideosById(category1.getId(),pageable)).willReturn(videoPage);
        Assertions.assertEquals(service.findVideosById(category1.getId(), pageable), videoPage);
    }

    private Category randomCategory (){
        return new Category(1L, "Ação", "red", List.of(new Video()));
    }

    private Video randomVideo (){
        return new Video(1L, "Video do você sabia", "Este vídeo foi feito pelo você Sabia", "www.youtube.VoceSabia", new Category(1L));
    }
}