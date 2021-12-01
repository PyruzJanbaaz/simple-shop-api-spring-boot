package org.pyruz.api.shop.model.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "role")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity<Integer> {

    @Column(name = "title", length = 50)
    private String title;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users;
}
