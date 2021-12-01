package org.pyruz.api.shop.model.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseEntityDTO<T> {
    protected T id;
    protected Timestamp createDate;
    protected Timestamp updateDate;
    protected Boolean isActive;
}
