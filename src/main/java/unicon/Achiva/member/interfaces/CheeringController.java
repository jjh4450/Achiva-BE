package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.domain.AuthService;
import unicon.Achiva.member.domain.CheeringRequest;
import unicon.Achiva.member.domain.CheeringService;

@RestController
@RequiredArgsConstructor
public class CheeringController {

    private final CheeringService cheeringService;
    private final AuthService authService;

    @Operation(summary = "응원 작성")
    @PostMapping("/api/articles/{articleId}/cheerings")
    public ResponseEntity<ApiResponseForm<CheeringResponse>> createCheering(
            @RequestBody CheeringRequest request,
            @RequestParam Long articleId,
            HttpServletRequest httpServletRequest
            ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        CheeringResponse response = cheeringService.createCheering(request, memberId, articleId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "응원 작성 성공"));
    }

    @Operation(summary = "응원 수정")
    @PutMapping("api/articles/{articleId}/cheerings/{cheeringId}")
    public ResponseEntity<ApiResponseForm<CheeringResponse>> updateCheering(
            @RequestBody CheeringRequest request,
            @RequestParam Long cheeringId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        CheeringResponse response = cheeringService.updateCheering(request, cheeringId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "응원 수정 성공"));
    }

    @Operation(summary = "응원 삭제")
    @DeleteMapping("api/articles/{articleId}/cheerings/{cheeringId}")
    public ResponseEntity<ApiResponseForm<Void>> deleteCheering(
            @RequestParam Long cheeringId,
            HttpServletRequest httpServletRequest
    ) {
        Long memberId = authService.getMemberIdFromToken(httpServletRequest);
        cheeringService.deleteCheering(cheeringId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(null, "응원 삭제 성공"));
    }

    @Operation(summary = "특정 응원 조회")
    @GetMapping("api/articles/{articleId}/cheerings/{cheeringId}")
    public ResponseEntity<ApiResponseForm<CheeringResponse>> getCheering(
            @RequestParam Long cheeringId,
            HttpServletRequest httpServletRequest
    ) {
        CheeringResponse response = cheeringService.getCheering(cheeringId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "응원 조회 성공"));
    }
}
