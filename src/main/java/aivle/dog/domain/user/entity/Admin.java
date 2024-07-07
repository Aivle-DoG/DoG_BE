package aivle.dog.domain.user.entity;

import aivle.dog.global.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Table
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@ToString
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(updatable = false)
    private String username;

    @NotNull
    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length = 10)
    private String name;

    @Builder
    public Admin(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

}
