package com.example.place.domain.itemtag.entity;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.user.entity.User;
import jakarta.persistence.*;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Table(name = "item_tags")
public class ItemTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;


}
