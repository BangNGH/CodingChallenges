package com.example.coderlab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không hợp lệ")
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @NotBlank(message = "Tên không được để trống!")
    @Column(name = "full_name", length = 127)
    private String fullName;
    @Column(name = "password")
    private String password;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "is_active")
    private boolean isActive = false;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "user_added", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKit> assignmentKits;

    @OneToMany(mappedBy = "user_submited", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AssignmentKitSubmission> assignmentKitSubmissions;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    public String getImagesPath(){
        if(avatarUrl == null || id == null) return null;
        return "/avt-images/" + id + "/" + avatarUrl;
    }
}
