package com.company.service;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.AttachRepository;
import com.company.repository.ProfileRepository;
import com.company.repository.custome.CustomProfileRepository;
import com.company.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AttachRepository attachRepository;

    @Autowired
    private AttachService attachService;

    @Autowired
    private CustomProfileRepository customProfileRepository;

    public ProfileDTO create(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.error("User already exists {}" , dto);
            throw new BadRequestException("User already exists");
        }
        ProfileRole role = checkRole(dto.getRole().name());

        ProfileEntity entity = new ProfileEntity();
        entity.setRole(role);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setPhone(dto.getPhone());

        Optional<AttachEntity> attaches = attachRepository.findById(dto.getAttachId());
        if (attaches.isEmpty()) {
            log.error("attache not found {}" , dto);
            throw new ItemNotFoundException("attache not found");
        }
        AttachEntity attachEntity = attaches.get();
        entity.setAttach(attachEntity);

        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);

        profileRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer pId, ProfileDTO dto) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()) {
            log.error("Profile Not Found {}" , dto);
            throw new ItemNotFoundException("Profile Not Found ");
        }


        ProfileEntity entity = profile.get();


        // 1st photo
        // bor edi yangisiga almashtiradi
        // null

//        if (entity.getAttach() == null && dto.getAttachId() != null) {
//
//            entity.setAttach(new AttachEntity(dto.getAttachId()));
//
//        } else if (entity.getAttach() != null && dto.getAttachId() == null) {
//
//            attachService.delete(entity.getAttach().getId());
//            entity.setAttach(null);
//
//        } else if (entity.getAttach() != null && dto.getAttachId() != null && entity.getAttach().getId().equals(dto.getAttachId())) {
//
//            entity.setAttach(new AttachEntity(dto.getAttachId()));
//
//        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        profileRepository.save(entity);
    }

    public void updateCurrentAttach(Integer pId, ProfileDTO dto) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()) {
            log.error("Profile Not Found {}" , dto);
            throw new ItemNotFoundException("Profile Not Found ");
        }


        ProfileEntity entity = profile.get();


        // 1st photo
        // bor edi yangisiga almashtiradi
        // null

        if (entity.getAttach() == null && dto.getAttachId() != null) {

            entity.setAttach(new AttachEntity(dto.getAttachId()));

        } else if (entity.getAttach() != null && dto.getAttachId() == null) {

            attachService.delete(entity.getAttach().getId());
            entity.setAttach(null);

        } else if (entity.getAttach() != null && dto.getAttachId() != null && entity.getAttach().getId().equals(dto.getAttachId())) {

            entity.setAttach(new AttachEntity(dto.getAttachId()));

        }
        profileRepository.save(entity);
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findByIdAndVisible(id, true).orElseThrow(() -> {
            log.error("Profile Not Found {}" , id);
            throw new ItemNotFoundException("Profile not found");
        });
    }

    public PageImpl<ProfileDTO> list(String role , int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProfileEntity> all1 = (Page<ProfileEntity>) profileRepository.findAll(pageable);

        List<ProfileEntity> entityList = all1.getContent();
        List<ProfileDTO> dtoList = entityToDtoList(entityList);

        return new PageImpl<>(dtoList, pageable, all1.getTotalElements());
    }

    private List<ProfileDTO> entityToDtoList(Iterable<ProfileEntity> entityList) {
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : entityList) {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setSurname(entity.getSurname());
            dto.setEmail(entity.getEmail());
//            dto.setAttachId(entity.getAttach().getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private ProfileRole checkRole(String role) {
        try {
            return ProfileRole.valueOf(role);
        } catch (RuntimeException e) {
            log.error("Role is wrong {}" , role);
            throw new BadRequestException("Role is wrong");
        }
    }

    public void delete(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.checkDeleted(ProfileStatus.ACTIVE, id);
        if (optional.isEmpty()) {
            log.error("This user not found or already deleted! {}" , id);
            throw new BadRequestException("This user not found or already deleted!");
        }
        profileRepository.changeStatus(ProfileStatus.NOT_ACTIVE, id);
    }

    public List<ProfileDTO> filter(ProfileFilterDTO dto) {
        ProfileDTO dto1 = new ProfileDTO();
        List<ProfileDTO> listdto = new ArrayList<>();
        for (ProfileEntity profileEntity : customProfileRepository.filter(dto)) {
            dto1.setName(profileEntity.getName());
            dto1.setSurname(profileEntity.getSurname());
            dto1.setPassword(profileEntity.getPassword());
            dto1.setPhone(profileEntity.getPhone());
            dto1.setVisible(profileEntity.getVisible());
            dto1.setCreatedDate(profileEntity.getCreatedDate());
            dto1.setStatus(profileEntity.getStatus());
            dto1.setEmail(profileEntity.getEmail());
            listdto.add(dto1);
        }
        return listdto;
    }
}
