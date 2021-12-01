package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@Data
@Builder
@Entity
@Table(name = "category")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity<Integer> {

    @Column(name = "title", length = 50, unique = true)
    private String title;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Product> products;

}
