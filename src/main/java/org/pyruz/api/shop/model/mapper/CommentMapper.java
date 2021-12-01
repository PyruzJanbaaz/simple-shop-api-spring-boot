package org.pyruz.api.shop.model.mapper;

import org.mapstruct.Mapper;
import org.pyruz.api.shop.model.dto.CommentDTO;
import org.pyruz.api.shop.model.entity.Comment;

@Mapper
public interface CommentMapper {
    CommentDTO COMMENT_DTO(Comment comment);
}
