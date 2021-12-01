package org.pyruz.api.shop.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "contact")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Contact extends BaseEntity<Long> {

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "mobile_number", length = 20, unique = true)
    private String mobileNumber;

    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "address", length = 250)
    private String address;

    @Column(name = "pin_code")
    private Integer pinCode;

    @Column(name = "temp_pin_code")
    private Integer tempPinCode;

    @Column(name = "expiration_pin_code")
    private Date expirationPinCode;

    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
