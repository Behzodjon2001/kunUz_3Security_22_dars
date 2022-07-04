package com.company.controller;

import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFilterDTO;
import com.company.dto.article.ArticleListDTO;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "Article CRUD")
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "Article create", notes="Method for Article create")
    @PostMapping("/mod/create")
    public ResponseEntity<?> create(@RequestBody @Valid ArticleCreateDTO dto) {
        log.info("Request for create {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        ArticleDTO articleDTO = articleService.create(dto, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body(articleDTO);
    }

    @ApiOperation(value = "Article update", notes="Method for Article update")
    @PutMapping("/mod/update{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ArticleCreateDTO dto) {
        log.info("Request for update {}" , dto);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        ArticleDTO articleDTO = articleService.update(id, dto, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body("\tSuccessfully updated \n\n" + articleDTO);
    }

    @ApiOperation(value = "list By Category", notes="Method for list By Category")
    @GetMapping("/public/list/cat/{id}")
    public ResponseEntity<?> listByCategory(@PathVariable("id") Integer id) {
        log.info("Request for listByCategory {}" , id);
        List<ArticleDTO> list = articleService.listByCategory(id);
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "list By Type", notes="Method for list By Type")
    @GetMapping("/public/list/type")
    public ResponseEntity<?> listByType(@RequestParam(name = "key") String key) {
        log.info("Request for listByType {}" , key);
        List<ArticleDTO> list = articleService.listByType(key);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> listByModerator(@RequestParam(name = "mod") Integer id) {
        log.info("Request for listByModerator {}" , id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<ArticleDTO> list = articleService.listByModerator(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/list")
    public ResponseEntity<?> listAll() {
        log.info("Request for listAll {}");
        List<ArticleDTO> list = articleService.listAll();
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/adm/delete")
    public ResponseEntity<?> delete(@RequestHeader("Content-ID") String id) {
        log.info("Request for delete {}" , id);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        articleService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @PutMapping("/mod/publish")
    public ResponseEntity<?> publish(@RequestHeader("Content-ID") String id) {
        log.info("Request for publish {}" , id);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.publish(id, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body("Published");
    }

    @PutMapping("/publisher/remove")
    public ResponseEntity<?> remove(@RequestHeader("Content-ID") String id) {
        log.info("Request for remove {}" , id);
//        Integer profileId = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.remove(id, CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body("Removed");
    }

    @GetMapping("/public/pagination")
    public ResponseEntity<?> getPagination(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "size", defaultValue = "2") int size) {
        log.info("Request for getPagination {}" , page);
        PageImpl<ArticleDTO> response = articleService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/public/list/5")
    public ResponseEntity<?> getLast5ArticleByType(@RequestParam(value = "type") String type) {
        log.info("Request for get last 5 Article bt type {}" , type);
        List<ArticleDTO> list = articleService.getListByType(type, 5);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/list/3")
    public ResponseEntity<?> getLast3ArticleByType(@RequestParam(value = "type") String type) {
        log.info("Request for get last 3 article by type {}" , type);
        List<ArticleDTO> list = articleService.getListByType(type, 3);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/list/8")
    public ResponseEntity<?> getLast8ArticleByTypeNotInId(@RequestBody ArticleListDTO dto) {
        log.info("Request for het last 8 article by type not in id {}" , dto);
        List<ArticleDTO> list = articleService.getLast8ArticleNotIn(dto);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/listByType/4")
    public ResponseEntity<?> getLast4ArticleByTypeNotInId(@RequestBody ArticleListDTO dto) {
        log.info("Request for get last 4 article by type not in id {}" , dto);
        List<ArticleDTO> list = articleService.getListByType(dto, 4);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/list4_most")
    public ResponseEntity<?> get4MostReadArticle() {
        log.info("Request for get 4 most read article {}");
        List<ArticleDTO> list = articleService.get4MostArticle(4);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/category/{categoryKey}")
    public ResponseEntity<List<ArticleDTO>> getLast5ArticleByCategory(@PathVariable("categoryKey") String categoryKey) {
        log.info("Request for get last 5 article vy category {}" , categoryKey);
        List<ArticleDTO> response = articleService.getLast5ArticleByCategory2(categoryKey);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-11
    @GetMapping("/public/listByTag/{tag}")
    public ResponseEntity<?> getLast4ArticleByTag(@PathVariable("tag") String key) {
        log.info("Request for get last 4 article by tag {}" , key);
        List<ArticleDTO> response = articleService.getLast4ArticleByTag(key);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-12
    @GetMapping("/public/listByTypeByRegion/{type}/{region}")
    public ResponseEntity<?> getLast5eByTypeByRegion(@PathVariable("type") String type, @PathVariable("region") String region) {
        log.info("Request for get last 4 article by region {}" , type);
        List<ArticleDTO> response = articleService.getLast5ByTagByRegion(type, region);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-13
    @GetMapping("/public/listByRegionByPage")
    public ResponseEntity<?> getListByRegionByPagination(@RequestParam(value = "region") String region,
                                                         @RequestParam(value = "page") Integer page,
                                                         @RequestParam(value = "size") Integer size) {
        log.info("Request for get last  region by pagination {}" , region);
        List<ArticleDTO> response = articleService.getListByRegionByPagination(region, page, size);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-14
    @GetMapping("/public/listByCategory")
    public ResponseEntity<?> getLast5ByCategory(@RequestParam("category") String key) {
        log.info("Request for get last 5  by category {}" , key);
        List<ArticleDTO> response = articleService.getLast5ByCategory(key);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-15
    @GetMapping("/public/listByCategoryByPagination")
    public ResponseEntity<?> getListByCategoryByPaging(@RequestParam("category") String key,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        log.info("Request for get last category by pagination {}" , key);
        List<ArticleDTO> response = articleService.getListByCategoryByPaging(key, page, size);
        return ResponseEntity.ok().body(response);
    }

    // Talablar : Article-19
    @PutMapping("/adm/view_count")
    public ResponseEntity<?> increaseViewCount(@RequestHeader("Content-ID") String articleId) {
        log.info("Request for increase view count {}" , articleId);
//        HttpHeaderUtil.getId(request);
        articleService.increaseViewCount(articleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/listFullInfo")
    public ResponseEntity<?> fullInfo(@RequestHeader(value = "Accept-Language" , defaultValue = "uz") String lang,
                                      @RequestHeader("Content-ID") String id) {
        log.info("Request for fuul info {}" , lang);
        ArticleDTO response = articleService.getFullInfoById(id, lang);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/public/filter")
    public ResponseEntity<List<ArticleDTO>> filter(@RequestBody ArticleFilterDTO dto) {
        log.info("Request for filter {}" , dto);
        List<ArticleDTO> response = articleService.filter(dto);
        return ResponseEntity.ok().body(response);
    }
}
