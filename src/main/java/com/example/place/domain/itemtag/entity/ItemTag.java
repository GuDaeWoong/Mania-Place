package com.example.place.domain.itemtag.entity;

import com.example.place.domain.item.entity.Item;
import com.example.place.domain.tag.entity.Tag;
import jakarta.persistence.*;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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

    private ItemTag(Tag tag, Item item) {
        this.tag = tag;
        this.item = item;
    }

    public static ItemTag of(Tag tag, Item item) {
        return new ItemTag(tag,item);
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
