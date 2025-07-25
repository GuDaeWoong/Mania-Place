package com.example.place.domain.tag.service;

import com.example.place.common.exception.enums.ExceptionCode;
import com.example.place.common.exception.exceptionclass.CustomException;
import com.example.place.domain.tag.dto.request.TagRequest;
import com.example.place.domain.tag.dto.response.TagResponse;
import com.example.place.domain.tag.entity.Tag;
import com.example.place.domain.tag.repository.TagRepository;
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

}
