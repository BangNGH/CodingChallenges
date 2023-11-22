package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "apply")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "job_name")
    private String jobName;

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name")
    private String companyName;

    @NotBlank(message = "Description is required")
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private UserEntity company;

    public String getImagesPath(){
        if(imgUrl == null || id == null) return null;
        return "/company-images/" + id + "/" + imgUrl;
    }
}
