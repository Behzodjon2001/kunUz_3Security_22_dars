package com.company.dto.article;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ArticleLikeDTO {
    @NotNull(message = "Article id qani")
    private String articleId;
}
