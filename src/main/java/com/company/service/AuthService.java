package com.company.service;

import com.company.dto.AuthDTO;
import com.company.dto.EmailDTO;
import com.company.dto.RegistrationDTO;
import com.company.dto.VerificationDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.response.ResponseInfoDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.sms.SmsEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.repository.sms.SmsRepository;
import com.company.service.sms.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SmsService smsService;
    @Autowired
    SmsRepository smsRepository;
    @Autowired
    EmailService emailService;

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.error("This user already exist {}" , dto);
            throw new BadRequestException("This user already exist");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());

        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(true);

        profileRepository.save(entity);

        emailService.sendRegistrationEmail(entity.getEmail(), entity.getId());


        return "Message was send";
    }

    public ProfileDTO login(AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword());
        if (optional.isEmpty()) {
            log.error("Email or password is wrongt {}" , dto);
            throw new BadRequestException("Email or password is wrong");
        }

        ProfileEntity profile = optional.get();

        if (profile.getStatus().equals(ProfileStatus.NOT_ACTIVE)){
            log.error("Not access {}" , dto);
            throw new BadRequestException("Not access");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(profile.getName());
        profileDTO.setSurname(profile.getSurname());

        return profileDTO;
    }

    public String verification(VerificationDTO dto) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(dto.getPhone());
        if (optional.isEmpty()) {
            return "Phone Not Found";
        }

        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (validDate.isBefore(LocalDateTime.now())) {
            return "Time is out";
        }

        profileRepository.updateStatusByPhone(dto.getPhone(), ProfileStatus.ACTIVE);
        return "Verification Done";
    }

    public ResponseInfoDTO resendSms(String phone) {
        Long count = smsService.getSmsCount(phone);
        if (count >= 4) {
            return new ResponseInfoDTO(-1, "Limit dan o'tib getgan");
        }

        smsService.sendRegistrationSms(phone);
        return new ResponseInfoDTO(1);
    }

    public ResponseInfoDTO resendEmail(EmailDTO dto, Integer id) {
        Long count = emailService.getEmailCount(id);
        if (count >= 4) {
            return new ResponseInfoDTO(-1, "Limit dan o'tib getgan");
        }

        emailService.sendRegistrationEmail(dto.getToAccount(),id);
        return new ResponseInfoDTO(1);
    }

    public String emailVerification(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()) {
            return "<h1>User Not Found</h1>";
        }

        ProfileEntity profile = optional.get();
        profile.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profile);
        return "<h1 style='align-text:center'>Success. Tabriklaymiz.</h1>";
    }
}
