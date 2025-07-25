package com.example.place.domain.tag.service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.tag.dto.request.TagRequest;
import com.example.place.domain.tag.dto.response.TagResponse;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;
import com.example.place.domain.tag.util.TagUtil;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.usertag.entity.UserTag;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.itemtag.entity.ItemTag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public TagResponse createTag(TagRequest tagRequest) {
        String tagName = tagRequest.getTagName();

        if(tagRepository.existsByTagName(tagName)) {
            throw new CustomException(ExceptionCode.DUPLICATED_TAG_NAME);
        }

        Tag tag = Tag.of(tagRequest.getTagName());
        tagRepository.save(tag);

        return TagResponse.from(tag);
    }

    @Transactional(readOnly = true)
    public TagResponse getTag(Long tagId) {
        Tag tag = findByIdOrElseThrow(tagId);
        return TagResponse.from(tag);
    }

    @Transactional
    public TagResponse updateTag(Long tagId, TagRequest tagRequest) {
        Tag tag = findByIdOrElseThrow(tagId);

        if (!tagRequest.getTagName().equals(tag.getTagName()) && tagRepository.existsByTagName(tagRequest.getTagName())) {
            throw new CustomException(ExceptionCode.DUPLICATED_TAG_NAME);
        }

        tag.updateTag(tagRequest.getTagName());
        return TagResponse.from(tag);
    }

    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = findByIdOrElseThrow(tagId);
        tagRepository.delete(tag);
    }

    // 유저 태그 저장 메서드
    public void saveTags(User user, Set<String> tagNames) {
        Set<String> normalizedTags = new LinkedHashSet<>();

        for (String tagName : tagNames) {
            normalizedTags.add(TagUtil.normalizeTag(tagName));
        }

        for (String tagName : normalizedTags) {
            Tag tag = findOrCreateTag(tagName);
            user.addUserTag(UserTag.of(tag, user));
        }
    }

    // 아이템 태그 저장 메서드
    public void saveTags(Item item, Set<String> tagNames) {
        Set<String> normalizedTags = new HashSet<>();

        for (String tagName : tagNames) {
            normalizedTags.add(TagUtil.normalizeTag(tagName));
        }

        for (String tagName : normalizedTags) {
            Tag tag = findOrCreateTag(tagName);
            item.addItemTag(ItemTag.of(tag, item));
        }
    }

    public Tag findByIdOrElseThrow(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TAG));
    }

    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByTagName(tagName)
            .orElseGet(() -> tagRepository.save(Tag.of(tagName)));
    }

}
