package com.thesoulapp.domain.model;

import com.thesoulapp.domain.valueobject.Email;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Protected for JPA
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class User extends BaseEntity implements UserDetails {

    // --- Value Objects ---    
    @Embedded
    @Valid
    @NotNull
    private Email email;

    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false)
    private String name;

    // --- Other Attributes --- 
    @Column(nullable = false)
    @NotBlank
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private UserStatus status = UserStatus.PENDING;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column
    private String avatarUrl;

    @Column
    private String preferredLanguage;

    @Column
    private String timezone;
    
    @Column
    private String externalId;

    @Column
    private LocalDateTime lastLoginAt;
    
    // --- Relationships --- 
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription subscription;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryEntry> diaryEntries;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAssessment> userAssessments;

    // --- UserDetails Implementation --- 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email.getValue();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }

    // --- Domain State Query Methods --- 
    public boolean hasVerifiedEmail() {
        return this.emailVerified;
    }

    // --- State Mutating Methods --- 
    public void markEmailAsVerified() {
        if (!this.emailVerified) {
            this.emailVerified = true;
        }
    }
    
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
    
    public void changeStatus(UserStatus newStatus) {
        this.status = newStatus;
    }

    // --- Helper method to link Subscription --- 
    public void setSubscription(Subscription subscription) {
        if (subscription == null) {
            if (this.subscription != null) {
                this.subscription.setUser(null);
            }
        } else {
            subscription.setUser(this);
        }
        this.subscription = subscription;
    }
} 