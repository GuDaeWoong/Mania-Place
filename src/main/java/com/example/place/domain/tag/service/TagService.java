package com.example.place.domain.tag.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.item.entity.Item;
import com.example.place.domain.itemtag.entity.ItemTag;
import com.example.place.domain.tag.dto.request.TagRequest;
import com.example.place.domain.tag.dto.response.TagResponse;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;
import com.example.place.domain.tag.util.TagUtil;
import com.example.place.domain.user.entity.User;
import com.example.place.domain.usertag.service.UserTagService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final UserTagService userTagService;

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
    @Transactional
    public Tag findOrCreateTagByName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(Tag.of(tagName)));
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

    // // 유저 태그 저장 메서드
    // public void saveTags(User user, Set<String> tagNames) {
    //     Set<String> normalizedTags = new LinkedHashSet<>();
    //
    //     for (String tagName : tagNames) {
    //         normalizedTags.add(TagUtil.normalizeTag(tagName));
    //     }
    //
    //     for (String tagName : normalizedTags) {
    //         Tag tag = findOrCreateTag(tagName);
    //         user.addUserTag(UserTag.of(tag, user));
    //     }
    // }

    public void saveTags(User user, Set<String> tagNames) {
        // 태그 정제
        Set<String> normalizedTags = tagNames.stream()
            .map(TagUtil::normalizeTag)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        // 기존 태그들 한번에 조회
        List<Tag> existingTags = tagRepository.findByTagNameIn(normalizedTags);
        Set<String> existingTagNames = existingTags.stream()
            .map(Tag::getTagName)
            .collect(Collectors.toSet());

        // 새로운 태그들만 추출
        Set<String> newTagNames = normalizedTags.stream()
            .filter(name -> !existingTagNames.contains(name))
            .collect(Collectors.toSet());

        // 새 태그들 배치 생성
        List<Tag> newTags = newTagNames.stream()
            .map(Tag::of)
            .collect(Collectors.toList());

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);  // 배치 INSERT
        }

        // 모든 태그 합치기
        List<Tag> allTags = new ArrayList<>(existingTags);
        allTags.addAll(newTags);

        // UserTag 배치 생성
        List<Long> tagIds = allTags.stream()
            .map(Tag::getId)
            .toList();
        userTagService.saveUserTags(user, tagIds);
    }

    // 아이템 태그 저장 메서드
    public void saveTags(Item item, Set<String> tagNames) {
        Set<String> normalizedTags = new LinkedHashSet<>();

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

    /**
    * 쿼리문을 직접 전송하여 DB값을 지우기때문에 영속성 컨텍스트 관리 필요함.
    */
    public void deleteAllByUser(User user) {
        tagRepository.deleteAllByUser(user);
    }
}
