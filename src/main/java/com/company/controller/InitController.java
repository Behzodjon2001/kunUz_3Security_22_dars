package com.company.controller;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.repository.ProfileRepository;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(tags = "Admin create")
@Slf4j
@RestController
@RequestMapping("/init")
public class InitController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/initAdmin")
    public String initAdmin() {
        log.info("Request for init admin {}");
        Optional<ProfileEntity> optional = profileRepository.findByEmail("admin");
        if (optional.isPresent()) {
            return "Exists";
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName("Adminjon");
        entity.setSurname("AdminAli");
        entity.setEmail("admin");
        entity.setPassword("123");
        entity.setRole(ProfileRole.ROLE_ADMIN);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(true);
        profileRepository.save(entity);
        return "Success";
    }
}
