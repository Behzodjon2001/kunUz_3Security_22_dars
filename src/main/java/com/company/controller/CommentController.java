package com.company.controller;

import com.company.dto.article.ArticleDTO;
import com.company.dto.comment.CommentDTO;
import com.company.enums.ProfileRole;
import com.company.service.CommentService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Comment CRUD")
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/user/create/{id}")
    public ResponseEntity<?> create(@RequestBody CommentDTO dto,
                                    @PathVariable("id") String id,
                                    HttpServletRequest request) {
        log.info("Request for create {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request);
        CommentDTO commentDTO = commentService.create(dto, id, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body(commentDTO);
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> update(@RequestBody CommentDTO dto,
                                    HttpServletRequest request) {
        log.info("Request for update {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request);
        commentService.update(dto, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body("Successfully updated");
    }

    @GetMapping("/public/byArticle")
    public ResponseEntity<?> listByArticle(@RequestBody ArticleDTO dto){
        log.info("Request for list by article {}" , dto);
        List<CommentDTO> list = commentService.list(dto);
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> deleteByAdmin(@RequestHeader("Content-ID") Integer id,
                                           HttpServletRequest request){
        log.info("Request for delete by admin {}" , id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        commentService.delete(id);
        return ResponseEntity.ok().body("Success");
    }

    @DeleteMapping("/user/delete/byUser")
    public ResponseEntity<?> deleteByUser(@RequestHeader("Content-ID") Integer id,
                                          HttpServletRequest request){
        log.info("Request for delete by user {}" , id);
//        Integer profileId = HttpHeaderUtil.getId(request);
        commentService.delete(CurrentUser.getCurrentUser().getProfile().getId(), id);
        return ResponseEntity.ok().body("Deleted");
    }
}
