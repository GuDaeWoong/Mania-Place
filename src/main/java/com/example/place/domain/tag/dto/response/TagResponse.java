package com.example.place.domain.tag.dto.response;

import com.example.place.domain.tag.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagResponse {
    private Long tagId;
    private String tagName;

    private TagResponse(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public static TagResponse from(Tag tag) {
        return new TagResponse(tag.getId(),tag.getTagName());
    }
}
