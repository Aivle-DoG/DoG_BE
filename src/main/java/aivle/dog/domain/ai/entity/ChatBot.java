package aivle.dog.domain.ai.entity;

import aivle.dog.domain.user.entity.User;
import aivle.dog.global.BaseTimeEntity;
import jakarta.persistence.*;
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
public class ChatBot extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String question;

    @Column
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public ChatBot(String question, String answer, User user) {
        this.question = question;
        this.answer = answer;
        this.user = user;
    }
}
