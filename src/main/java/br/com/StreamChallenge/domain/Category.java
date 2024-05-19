package br.com.StreamChallenge.domain;

import br.com.StreamChallenge.dto.category.CategoryCompleteDto;
import br.com.StreamChallenge.dto.category.CategoryDto;
import br.com.StreamChallenge.dto.category.CategoryListDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String color;
    @OneToMany( mappedBy = "category")
    private List<Video> video = new ArrayList<Video>();
    public Category(Long id){
        this.id=id;
    }
    public Category(CategoryDto dto){
        this.title = dto.title();
        this.color= dto.color();
    }
    public Category(CategoryListDto dto){
        this.id= dto.id();
        this.title = dto.title();
        this.color= dto.color();
    }
    public Category(CategoryCompleteDto dto){
        this.id = dto.id();
        this.title = dto.title();
        this.color= dto.color();
    }

    public void merge(Category category) {
        this.title = category.getTitle();
        this.color = category.getColor();
    }

    public void addVideo(Video video) {
        this.getVideo().add(video);
    }
}
