package com.example.place.domain.tag.controller;

import com.example.place.common.dto.ApiResponseDto;
import com.example.place.common.security.jwt.CustomPrincipal;
import com.example.place.domain.tag.dto.request.TagRequest;
import com.example.place.domain.tag.dto.response.TagResponse;
import com.example.place.domain.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ApiResponseDto<TagResponse>> createTag(@Valid @RequestBody TagRequest tagRequest) {
        TagResponse tag = tagService.createTag(tagRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("태그 생성이 완료되었습니다.", tag));
    }

    @GetMapping("{tagId}")
    public ResponseEntity<ApiResponseDto<TagResponse>> getTag(@PathVariable Long tagId) {
        TagResponse tag = tagService.getTag(tagId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.of("태그가 조회되었습니다.",tag));
    }



}
