package com.spently.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "password_reset_token")
public class PasswordResetToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String passwordResetTokenId;

    @Column(nullable = false, name = "username", length = 50)
    String username;

    @Column(nullable = false, unique = true, length = 255)
    String token;

    @Column(nullable = false, name = "new_password", length = 100)
    String newPassword;
}
