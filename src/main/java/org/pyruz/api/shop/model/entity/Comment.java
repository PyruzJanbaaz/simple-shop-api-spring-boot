package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@Entity(name = "comment")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity<Integer> {

    @Column(name = "comment", length = 500, nullable = false)
    private String comment;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
