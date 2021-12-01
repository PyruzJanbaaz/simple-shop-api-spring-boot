package org.pyruz.api.shop.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected T id;

    @Column(name = "create_date")
    protected Timestamp createDate;

    @Column(name = "update_date")
    protected Timestamp updateDate;

    @Column(name = "is_active")
    protected Boolean isActive = true;

    @Column(name = "is_deleted")
    protected Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createDate = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Timestamp(System.currentTimeMillis());
    }
}
