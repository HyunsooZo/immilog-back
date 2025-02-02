package com.backend.immilog.post.presentation.controller;

import com.backend.immilog.global.enums.Country;
import com.backend.immilog.post.application.result.PostResult;
import com.backend.immilog.post.application.services.PostDeleteService;
import com.backend.immilog.post.application.services.PostInquiryService;
import com.backend.immilog.post.application.services.PostUpdateService;
import com.backend.immilog.post.application.services.PostUploadService;
import com.backend.immilog.post.domain.enums.Categories;
import com.backend.immilog.post.domain.enums.SortingMethods;
import com.backend.immilog.post.presentation.request.PostUpdateRequest;
import com.backend.immilog.post.presentation.request.PostUploadRequest;
import com.backend.immilog.post.presentation.response.PostApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "PostEntity API", description = "게시물 관련 API")
@RequestMapping("/api/v1/posts")
@RestController
public class PostController {
    private final PostUploadService postUploadService;
    private final PostUpdateService postUpdateService;
    private final PostDeleteService postDeleteService;
    private final PostInquiryService postInquiryService;

    public PostController(
            PostUploadService postUploadService,
            PostUpdateService postUpdateService,
            PostDeleteService postDeleteService,
            PostInquiryService postInquiryService
    ) {
        this.postUploadService = postUploadService;
        this.postUpdateService = postUpdateService;
        this.postDeleteService = postDeleteService;
        this.postInquiryService = postInquiryService;
    }

    @PostMapping("/users/{userSeq}")
    @Operation(summary = "게시물 작성", description = "게시물을 작성합니다.")
    public ResponseEntity<PostApiResponse> createPost(
            @PathVariable("userSeq") Long userSeq,
            @Valid @RequestBody PostUploadRequest postUploadRequest
    ) {
        postUploadService.uploadPost(userSeq, postUploadRequest.toCommand());
        return ResponseEntity.status(CREATED).build();
    }

    @PatchMapping("/{postSeq}/users/{userSeq}")
    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다.")
    public ResponseEntity<PostApiResponse> updatePost(
            @PathVariable("postSeq") Long postSeq,
            @PathVariable("userSeq") Long userSeq,
            @Valid @RequestBody PostUpdateRequest postUpdateRequest
    ) {
        postUpdateService.updatePost(userSeq, postSeq, postUpdateRequest.toCommand());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{postSeq}/delete/users/{userSeq}")
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다.")
    public ResponseEntity<Void> deletePost(
            @PathVariable("postSeq") Long postSeq,
            @PathVariable("userSeq") Long userSeq
    ) {
        postDeleteService.deletePost(userSeq, postSeq);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PatchMapping("/{postSeq}/view")
    @Operation(summary = "게시물 조회수 증가", description = "게시물 조회수를 증가시킵니다.")
    public ResponseEntity<PostApiResponse> increaseViewCount(
            @PathVariable Long postSeq
    ) {
        postUpdateService.increaseViewCount(postSeq);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping
    @Operation(summary = "게시물 목록 조회", description = "게시물 목록을 조회합니다.")
    public ResponseEntity<PostApiResponse> getPosts(
            @RequestParam(required = false, name = "country") Country country,
            @RequestParam(required = false, name = "sortingMethod") SortingMethods sortingMethod,
            @RequestParam(required = false, name = "isPublic") String isPublic,
            @RequestParam(required = false, name = "category") Categories category,
            @RequestParam(required = false, name = "page") Integer page
    ) {
        Page<PostResult> posts = postInquiryService.getPosts(country, sortingMethod, isPublic, category, page);
        return ResponseEntity.status(OK).body(PostApiResponse.of(posts));
    }

    @GetMapping("/{postSeq}")
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 정보를 조회합다.")
    public ResponseEntity<PostApiResponse> getPost(@PathVariable("postSeq") Long postSeq) {
        PostResult post = postInquiryService.getPostDetail(postSeq);
        return ResponseEntity.status(OK).body(PostApiResponse.of(post));
    }

    @GetMapping("/search")
    @Operation(summary = "게시물 검색", description = "게시물을 검색합니다.")
    public ResponseEntity<PostApiResponse> searchPosts(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page") Integer page
    ) {
        Page<PostResult> posts = postInquiryService.searchKeyword(keyword, page);
        return ResponseEntity.status(OK).body(PostApiResponse.of(posts));
    }

    @GetMapping("/users/{userSeq}/page/{page}")
    @Operation(summary = "사용자 게시물 목록 조회", description = "사용자 게시물 목록을 조회합니다.")
    public ResponseEntity<PostApiResponse> getUserPosts(
            @PathVariable("userSeq") Long userSeq,
            @PathVariable("page") Integer page
    ) {
        Page<PostResult> posts = postInquiryService.getUserPosts(userSeq, page);
        return ResponseEntity.status(OK).body(PostApiResponse.of(posts));
    }

    @GetMapping("/hot")
    @Operation(summary = "인기 게시물 조회", description = "인기 게시물을 조회합니다.")
    public ResponseEntity<PostApiResponse> getHotPosts() {
        List<PostResult> posts = postInquiryService.getHotPosts();
        return ResponseEntity.status(OK).body(PostApiResponse.of(posts));
    }

    @GetMapping("/most-viewed")
    @Operation(summary = "가장 많이 조회된 게시물 조회", description = "가장 많이 조회된 게시물을 조회합니다.")
    public ResponseEntity<PostApiResponse> getMostViewedPosts() {
        List<PostResult> posts = postInquiryService.getMostViewedPosts();
        return ResponseEntity.status(OK).body(PostApiResponse.of(posts));
    }

}
