package com.example.coderlab.service;

import com.example.coderlab.entity.*;
import com.example.coderlab.exception.AlreadyExistsException;
import com.example.coderlab.exception.ResourceNotFoundException;
import com.example.coderlab.repository.UserRepository;
import com.example.coderlab.repository.UserRoleRepository;
import com.example.coderlab.repository.VerificationTokenRepository;
import com.example.coderlab.dto.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    public List<UserEntity> getList() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findByEmail(String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            return result;
        } else return null;
    }
    public Optional<UserEntity> findById(Long id) {
        Optional<UserEntity> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result;
        } else return null;
    }

    public void save(UserEntity user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                throw new ResourceNotFoundException("Người dùng với email " + user.getEmail() + " đã tồn tại");
            } else {
                throw e;
            }
        }
    }
    public void updateUser(UserEntity user, MultipartFile multipartFile) throws IOException {
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setFullName(user.getFullName());
        if (user.getEducation() != null) {
            existingUser.setEducation(user.getEducation());
        } if (user.getWork_experiences() != null) {
            existingUser.setWork_experiences(user.getWork_experiences());
        } if (user.getSkills() != null) {
            existingUser.setSkills(user.getSkills());
        } if (user.getCertificates() != null) {
            existingUser.setCertificates(user.getCertificates());
        }
        if(multipartFile != null && !multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingUser.setAvatarUrl(fileName);
            String upLoadDir = "avt-images/" + user.getId();
            FileUploadUtil.saveFile(upLoadDir, fileName, multipartFile);
        }
        userRepository.save(existingUser);
    }
    public void updatePassword(UserEntity user, String newPassword){
        UserEntity existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }
    public boolean checkPassword(UserEntity user, String password){
        return passwordEncoder.matches(password, user.getPassword());
    }
    public void delete(Long id) {
        Long count = userRepository.countById(id);
        if (count == null || count == 0) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public Optional get(Long id) {
        Optional<UserEntity> result = userRepository.findById(id);
        if (result.isPresent()) {
            return result;
        } else
            throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id + "!");
    }

    public UserEntity registerUser(RegistrationRequest request) {
        try {
        Optional<UserEntity> user = this.findByEmail(request.email());
            if (user!=null) {
                throw new AlreadyExistsException("Người dùng với email " + request.email() + " đã tồn tại!");
            }
            else {
            var newUser = new UserEntity();
            newUser.setFullName(request.fullName());
            newUser.setEmail(request.email());
            newUser.setPassword(passwordEncoder.encode(request.password()));
            Role foundRole = roleService.getRoleByName(request.role());
            var userRole = new UserRole();
            userRole.setUser(newUser);
            userRole.setRole(foundRole);
            userRoleRepository.save(userRole);
            return userRepository.save(newUser);}
        }catch (Exception e){
            throw new AlreadyExistsException("Người dùng với email " + request.email() + " đã tồn tại!");
        }
    }

    public void saveUserVerificationToken(UserEntity user, String token) {
        var verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }

    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        UserEntity user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            //tokenRepository.delete(token);
            return "Token already expired!";
        }
        user.setActive(true);
        userRepository.save(user);
        return "Valid";
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        if (verificationToken == null) {
            throw new ResourceNotFoundException("Token " + oldToken + " is not valid");
        }
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }

    public Integer getSovledAssignment(Long id) {
        return userRepository.getSovledAssignment(id);
    }

    public List<Object[]> getLanguagePercentageByStudentId(Long id) {
        return userRepository.getLanguagePercentageByStudentId(id);
    }

    public List<UserEntity> getListRole(Role teacherRole) {
        return userRepository.getListRole(teacherRole);
    }
}
