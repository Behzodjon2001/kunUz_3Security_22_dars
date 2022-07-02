package com.company.controller;

import com.company.dto.TypesDTO;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.TypesService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Type CRUD")
@Slf4j
@RestController
@RequestMapping("/types")
public class TypesController {
    @Autowired
    private TypesService typesService;

    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@RequestBody TypesDTO dto) {
        log.info("Request for create {}" , dto);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        TypesDTO articleTypeDto = typesService.create(dto);
        return ResponseEntity.ok().body(articleTypeDto);
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list() {
        log.info("Request for list {}" );
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<TypesDTO> list = typesService.list();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/adm/update")
    public ResponseEntity<?> update(@RequestBody TypesDTO dto) {
        log.info("Request for update {}" , dto);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        typesService.update(dto);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<?> delete(@PathVariable("key") String key) {
        log.info("Request for delete {}" , key);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        typesService.delete(key);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @GetMapping("/public/list")
//    public ResponseEntity<?> getArticleList(@RequestParam(value = "lang", defaultValue = "uz")LangEnum lang){
    public ResponseEntity<?> getArticleList(@RequestHeader(value = "Accept-Language", defaultValue = "uz") LangEnum lang) {
        log.info("Request for get article list {}" , lang);
        List<TypesDTO> list = typesService.getList(lang);
        return ResponseEntity.ok().body(list);
    }
}
