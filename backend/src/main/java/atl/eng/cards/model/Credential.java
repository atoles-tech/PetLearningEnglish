package atl.eng.cards.model;

import java.time.LocalDateTime;

import atl.eng.cards.model.util.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="credentials")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Credential {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", length = 70, unique = true, nullable = false)
    private String username;

    @Column(name = "hash_password", length = 255, nullable = false)
    private String hashPassword;

    @Column(name = "role", length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "credential", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
