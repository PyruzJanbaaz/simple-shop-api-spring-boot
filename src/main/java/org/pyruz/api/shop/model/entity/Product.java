package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "product")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity<Integer> {

    @Column(name = "title", length = 100, nullable = false)
    String title;

    @Column(name = "image", length = 100)
    String image;

    @Column(name = "price", nullable = false)
    Float price;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Comment> comments;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Rate> rates;

}
