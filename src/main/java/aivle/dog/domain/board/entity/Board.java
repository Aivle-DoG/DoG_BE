package aivle.dog.domain.board.entity;

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

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@ToString
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private InquiryType inquiryType;

    @NotNull
    @Column(length = 50)
    private String title;

    @NotNull
    @Column(length = 1000)
    private String description;

    @NotNull
    @Column(name = "view_count")
    private Long viewCount;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Builder
    public Board(User user, InquiryType inquiryType, String title, String description, Long viewCount) {
        this.user = user;
        this.inquiryType = Objects.requireNonNullElse(inquiryType, InquiryType.ETC);
        this.title = title;
        this.description = description;
        this.viewCount = viewCount;
    }

    public Board updateBoard(String title, String description) {
        if (title != null)
            this.title = title;
        if (description != null)
            this.description = description;
        return this;
    }

    public Board updateViewCount(Long viewCount) {
        if (viewCount != null)
            this.viewCount = viewCount + 1;
        return this;
    }
}
