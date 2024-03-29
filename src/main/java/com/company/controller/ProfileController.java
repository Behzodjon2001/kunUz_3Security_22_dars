package com.company.controller;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.service.ProfileService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "Profile CRUD for Admin")
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/adm/create")
    public ResponseEntity<?> create(@Valid @RequestBody ProfileDTO dto) {
        log.info("Request for create {}" , dto);
        ProfileDTO profileDTO = profileService.create(dto);
        return ResponseEntity.ok().body(profileDTO);
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> update(@RequestBody ProfileDTO dto) {
        log.info("Request for update {}" , dto);
        profileService.update(CurrentUser.getCurrentUser().getProfile().getId(), dto);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @PutMapping("/current/all")
    public ResponseEntity<?> updateCurrentAttach(@RequestBody ProfileDTO dto) {
        log.info("Request for update {}" , dto);
        profileService.updateCurrentAttach(CurrentUser.getCurrentUser().getProfile().getId(), dto);
        return ResponseEntity.ok().body("Successfully updated");
    }

    @PutMapping("/adm/updateByAdmin")
    public ResponseEntity<?> updateByAdmin(@RequestBody ProfileDTO dto) {
        log.info("Request for update by admin {}" , dto);
        profileService.update(dto.getId(), dto);
        return ResponseEntity.ok().body("Updated");
    }

    @GetMapping("/adm/list")
    public ResponseEntity<?> list(@RequestBody() String role,@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "2") int size) {
        log.info("Request for list {}" , role ,page, size);
        PageImpl<ProfileDTO> list = profileService.list(role, page ,size);
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        log.info("Request for delete {}" , id);
        profileService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }

    @PostMapping("/adm/filter")
    public ResponseEntity<List<ProfileDTO>> filter(@RequestBody ProfileFilterDTO dto) {
        log.info("Request for filter {}" , dto);
        List<ProfileDTO> response = profileService.filter(dto);
        return ResponseEntity.ok().body(response);
    }
}
