package com.example.place.domain.usertag.entity;

import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_tags")
@Getter
public class UserTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private UserTag(Tag tag, User user) {
        this.tag = tag;
        this.user = user;
    }

    public static UserTag of(Tag tag, User user) {
        return new UserTag(tag,user);
    }

    public void setUser(User user) {
        this.user = user;
    }

}

