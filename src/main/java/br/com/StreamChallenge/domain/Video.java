package br.com.StreamChallenge.domain;

import br.com.StreamChallenge.dto.video.VideoDto;
import br.com.StreamChallenge.dto.video.VideoDtoComplete;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url;
    @ManyToOne()
    @JoinColumn(name = "id_category")
    private Category category = new Category(1L);
    public Video(VideoDto dto){
        this.title=dto.title();
        this.description=dto.description();
        this.url= dto.url();
        if (dto.idCategory()!=null) {
            this.category= new Category(dto.idCategory());
        }
    }
    public Video(VideoDtoComplete dto){
        this.id=dto.id();
        this.title=dto.title();
        this.description=dto.description();
        this.url= dto.url();
    }

    public void merge(Video video) {
        this.id=video.getId();
        this.title=video.getTitle();
        this.description=video.getDescription();
        this.url= video.getUrl();
    }
}
