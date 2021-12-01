package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity<Integer> {

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "gender", length = 7)
    private String gender;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL ,mappedBy = "user")
    @PrimaryKeyJoinColumn
    private Rate rate;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts;

}
