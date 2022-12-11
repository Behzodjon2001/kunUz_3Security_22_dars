package com.company;

import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@SpringBootTest
class KunUz3Security22DarsApplicationTests {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void contextLoads() {
        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
        System.out.println(b.encode("123"));
    }
    @Test
    void contextLoads1() {
        ProfileEntity entity = new ProfileEntity();
        entity.setRole(ProfileRole.ROLE_ADMIN);
        entity.setName("Behzod");
        entity.setSurname("Malikov");
        entity.setEmail("malikovbehzod2001@gmail.com");
        entity.setPassword(MD5Util.getMd5("444"));

//        Optional<AttachEntity> attaches = attachRepository.findById(dto.getAttachId());
//        if (attaches.isEmpty()) {
//            log.error("attache not found {}" , dto);
//            throw new ItemNotFoundException("attache not found");
//        }
//        AttachEntity attachEntity = attaches.get();
//        entity.setAttach(attachEntity);

        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(true);

        profileRepository.save(entity);
    }

}
