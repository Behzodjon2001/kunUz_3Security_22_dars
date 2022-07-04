package com.company.controller;

import com.company.dto.RegionDTO;
import com.company.enums.ProfileRole;
import com.company.service.RegionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "Region CRUD")
@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@RequestBody RegionDTO dto) {
        log.info("Request for create {}" , dto);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        RegionDTO regionDTO = regionService.create(dto);
        return ResponseEntity.ok().body(regionDTO);
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> listByAdmin() {
        log.info("Request for list {}" );
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<RegionDTO> list = regionService.list();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/public/list")
    public ResponseEntity<?> list() {
        log.info("Request for list {}" );

        List<RegionDTO> list = regionService.list();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/adm/update/{key}")
    public ResponseEntity<?> update(@PathVariable String key, @RequestBody RegionDTO dto) {
        log.info("Request for update {}" , dto);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        regionService.update(dto, key);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @DeleteMapping("/adm/delete/{key}")
    public ResponseEntity<?> delete(@PathVariable("key") String key) {
        log.info("Request for delete {}" , key);
//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        regionService.delete(key);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}
