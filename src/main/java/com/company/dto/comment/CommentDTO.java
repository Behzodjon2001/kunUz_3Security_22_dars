package com.company.dto.comment;

import com.company.dto.article.ArticleDTO;
import com.company.dto.profile.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    @NotNull(message = "Content qani ?")
    private String content;
    private LocalDateTime createdDate;
    private ProfileDTO profile;
    private ArticleDTO article;
    private Integer replyId;
}
