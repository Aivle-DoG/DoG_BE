package aivle.dog.domain.user.entity;

import aivle.dog.global.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Table(name = "USER_APP", indexes = @Index(name = "idx_username", columnList = "username", unique = true))
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor//(access = AccessLevel.PRIVATE)
@ToString
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @Column(updatable = false)
    private String username;

    @NotNull
    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length=10)
    private String name;

    @NotNull
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Builder
    public User(String username, String password, String name, String phoneNumber){
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

}
