package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", length = 50, unique = true)
    @NotBlank(message = "Username đã được sử dụng")
    @Size(min = 1, max = 50, message = "UserName không vượt quá 50 kí tự")
    private String username;

    @Column(name = "fullName")
    @NotBlank
    private String fullName;

    @Column
    @NotNull
    private LocalDate dateOfBirth;

    @Column
    @NotNull
    private boolean gender;

    @Column(name = "password", length = 250)
    private String password;

    @Column(name = "email", unique = true)
    @NotBlank(message = "Email đã tồn tại")
    @Email
    private String email;

    @Column(name = "phoneNumber", length = 10, unique = true)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDate created_at;

    @Column
    private String avatar;

    @Column
    private String coverPhoto;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GroupMembership> groupMemberships = new ArrayList<>();

    @Transient
    private MultipartFile avatarFile;

    @Transient
    private MultipartFile coverPhotoFile;

    @Column
    @NotNull
    @Builder.Default
    private boolean activeStatus = false; // online, offline, để mặc định offline

    private String bio;

    @Column
    @NotNull
    @Builder.Default
    private boolean accountStatus = true; // bị chặn, ko bị chặn, để mặc định là bị chặn

    private LocalDateTime lastLogin;

        // @ManyToMany
    // @JoinTable(
    //     name = "group_membership",
    //     joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @JoinColumn(name = "group_id")
    // )
    // private List<Group> groups;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
