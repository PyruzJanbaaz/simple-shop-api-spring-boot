package org.pyruz.api.shop.model.dto;

import lombok.*;

@Data
public class CommentDTO extends BaseEntityDTO<Integer> {
    private String comment;
}
