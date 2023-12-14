package com.example.coderlab.service;

import com.example.coderlab.entity.Apply;
import com.example.coderlab.entity.UserEntity;
import com.example.coderlab.repository.ApplyRepository;
import com.example.coderlab.dto.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private UserServices userServices;
    public List<Apply> getAllApplies(){
        return applyRepository.findAll();
    }
    public void save(Apply apply){
       applyRepository.save(apply);
    }
    public Apply getApplyById(Long id){
        return applyRepository.findById(id).orElseThrow();
    }
    public void addApply(String jobName, String companyName, String address, String description, MultipartFile multipartFile) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userServices.findByEmail(email).orElseThrow();

        Apply apply = new Apply();
        apply.setJobName(jobName);
        apply.setCompanyName(companyName);
        apply.setAddress(address);
        apply.setDescription(description);
        apply.setCompany(user);
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        apply.setImgUrl(fileName);
        Apply saveApply = applyRepository.save(apply);
        String upLoadDir = "company-images/" + saveApply.getId();
        FileUploadUtil.saveFile(upLoadDir, fileName, multipartFile);
        applyRepository.save(apply);
    }
    public void updateApply(Apply apply, String description, MultipartFile multipartFile) throws IOException {
        Apply existingApply = applyRepository.findById(apply.getId()).orElse(null);
        existingApply.setJobName(apply.getJobName());
        existingApply.setCompanyName(apply.getCompanyName());
        existingApply.setAddress(apply.getAddress());
        existingApply.setDescription(description);

        if(multipartFile != null && !multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingApply.setImgUrl(fileName);
            String upLoadDir = "company-images/" + apply.getId();
            FileUploadUtil.saveFile(upLoadDir, fileName, multipartFile);
        }
        applyRepository.save(existingApply);
    }
}
