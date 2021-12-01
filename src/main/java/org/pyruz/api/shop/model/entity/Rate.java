package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "rate")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Rate extends BaseEntity<Integer> {

    @Column(name = "rate")
    private Float rate;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
