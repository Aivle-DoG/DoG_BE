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
    @Column(name = "company_name")
    private String companyName;

    @NotNull
    @Column(name = "business_number", length = 15)
    private String businessNumber;

    @Builder
    public User(@NotNull String username, @NotNull String password, @NotNull String companyName, @NotNull String businessNumber) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.businessNumber = businessNumber;
    }


    public User updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
        return this;
    }

}
