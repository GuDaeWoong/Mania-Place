package com.example.place.domain.tag.entity;

import com.example.place.domain.itemtag.entity.ItemTag;
import com.example.place.domain.usertag.entity.UserTag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 70, nullable = false, unique = true)
    private String tagName;

    @OneToMany(mappedBy = "tag")
    private List<ItemTag> itemTags = new ArrayList<>();

    @OneToMany(mappedBy = "tag")
    private List<UserTag> userTags = new ArrayList<>();

    private Tag(String tagName) {
        this.tagName = tagName;
    }
    public static Tag of(String tagName) {
        return new Tag(tagName);
    }

}
