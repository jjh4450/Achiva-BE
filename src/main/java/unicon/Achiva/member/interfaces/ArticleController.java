package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.common.S3Service;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.domain.ArticleService;
import unicon.Achiva.member.domain.AuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;
    private final S3Service s3Service;

    @Operation(summary = "새 게시글 작성")
    @PostMapping("/api/articles")
    public ResponseEntity<ApiResponseForm<ArticleResponse>> createArticle(
            @RequestBody ArticleRequest request,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        ArticleResponse response = articleService.createArticle(request, memberId);
        return ResponseEntity.ok(ApiResponseForm.created(response, "게시글 작성 성공"));
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/api/articles/{articleId}")
    public ResponseEntity<ApiResponseForm<ArticleResponse>> updateArticle(
            @RequestBody ArticleRequest request,
            @RequestParam Long articleId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        ArticleResponse response = articleService.updateArticle(request, articleId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "게시글 수정 성공"));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/api/articles/{articleId}/delete")
    public ResponseEntity<ApiResponseForm<Void>> deleteArticle(
            HttpServletRequest httpServletRequest,
            @RequestParam Long articleId
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        articleService.deleteArticle(articleId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(null, "게시글 삭제 성공"));
    }

    @Operation(summary = "게시글 검색")
    @GetMapping("/api/articles")
    public ResponseEntity<ApiResponseForm<Page<ArticleResponse>>> searchArticles(
            SearchArticleCondition condition,
            Pageable pageable
    ) {
        Page<ArticleResponse> response = articleService.getArticles(condition, pageable);
        return ResponseEntity.ok(ApiResponseForm.success(response, "게시글 검색 성공"));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/api/articles/{articleId}")
    public ResponseEntity<ApiResponseForm<ArticleResponse>> getArticle(
            HttpServletRequest httpServletRequest,
            @PathVariable Long articleId
    ) {
        ArticleResponse response = articleService.getArticle(articleId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "게시글 상세 조회 성공"));
    }

    @Operation(summary = "내 게시글 목록 조회")
    @GetMapping("/api/articles/my-articles")
    public ResponseEntity<ApiResponseForm<Page<ArticleResponse>>> getMyArticles(
            HttpServletRequest httpServletRequest,
            Pageable pageable
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        Page<ArticleResponse> response = articleService.getArticlesByMember(memberId, pageable);
        return ResponseEntity.ok(ApiResponseForm.success(response, "내 게시글 목록 조회 성공"));
    }

    @Operation(summary = "특정 유저 게시글 목록 조회")
    @GetMapping("/api/member/{memberId}/articles")
    public ResponseEntity<ApiResponseForm<Page<ArticleResponse>>> getArticlesByMember(
            @PathVariable Long memberId,
            Pageable pageable
    ) {
        Page<ArticleResponse> response = articleService.getArticlesByMember(memberId, pageable);
        return ResponseEntity.ok(ApiResponseForm.success(response, "특정 유저 게시글 목록 조회 성공"));
    }

    @Operation(summary = "게시글 사진 저장용 presigned URL 발급. 이후 게시글 생성, 수정 등 요청 보낼 때 본 api에서 발급받은 url에서 쿼리스트링 뺀 부분을 photoUrl로 사용해야 함.")
    @GetMapping("/api/articles/presigned-url")
    public ResponseEntity<ApiResponseForm<Map<String, String>>> getPresignedUrl(
            @RequestParam(defaultValue = "application/octet-stream") String contentType
    ) {
        String url = s3Service.generatePresignedUrl(contentType);
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(ApiResponseForm.success(response, "Presigned URL 발급 성공"));
    }

    @Operation(summary = "홈화면 게시글 목록 조회")
    @GetMapping("/api/articles/home")
    public ResponseEntity<Page<ArticleResponse>> getCombinedFeed(
            @PageableDefault(size = 10) Pageable pageable,
            HttpServletRequest request
    ) {
        Long memberId = authService.getMemberIdFromToken(request);
        Page<ArticleResponse> page = articleService.getHomeArticles(memberId, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "멤버 관심 카테고리 기반 최신글 게시글 목록")
    @GetMapping("/api/members/{memberId}/feed")
    public ResponseEntity<ApiResponseForm<Page<ArticleResponse>>> getFeed(
            @PathVariable Long memberId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ArticleResponse> response = articleService.getMemberInterestFeed(memberId, pageable);
        return ResponseEntity.ok(ApiResponseForm.success(response, "멤버 관심 카테고리 기반 최신글 게시글 목록 조회 성공"));
    }
}
