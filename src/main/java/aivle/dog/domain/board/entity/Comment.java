package aivle.dog.domain.board.entity;

import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.entity.User;
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
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Builder
    public Comment(@NotNull String name, @NotNull String description, Board board, User user, Admin admin) {
        this.name = name;
        this.description = description;
        this.board = board;
        this.user = user;
        this.admin = admin;
    }
}
