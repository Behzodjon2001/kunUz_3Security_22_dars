package com.company.service;

import com.company.config.CustomUserDetails;
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
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private  EmailService emailService;

    public ProfileDTO login(AuthDTO authDTO) {

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();
        ProfileEntity profile = user.getProfile();

        ProfileDTO dto = new ProfileDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));

        return dto;
    }
    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setVisible(true);
        profileRepository.save(entity);

        return smsService.sendRegistrationSms(entity.getPhone());
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

        String s = smsService.sendRegistrationSms(phone);
        ResponseInfoDTO responseInfoDTO = new ResponseInfoDTO();
        responseInfoDTO.setCode(s);
        responseInfoDTO.setStatus(1);
        return responseInfoDTO;
    }

    public ResponseInfoDTO resendEmail(EmailDTO dto, Integer id) {
        Long count = emailService.getEmailCount(id);
        if (count >= 4) {
            return new ResponseInfoDTO(-1, "Limit dan o'tib getgan");
        }

        String s = smsService.sendRegistrationSms(dto.toString());
        ResponseInfoDTO responseInfoDTO = new ResponseInfoDTO();
        responseInfoDTO.setCode(s);
        responseInfoDTO.setStatus(1);
        return responseInfoDTO;
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
