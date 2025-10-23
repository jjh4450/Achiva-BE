package unicon.Achiva.domain.auth.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import unicon.Achiva.global.config.AwsProperties;

/**
 * Cognito 사용자 관리 서비스
 *
 * <p>
 * 회원 탈퇴, 비활성화, 활성화, 전체 세션 종료 등
 * 관리자 권한 기반의 Cognito 작업을 처리한다.
 * </p>
 *
 * <p>
 * 필요한 IAM 권한:
 * <ul>
 *   <li>cognito-idp:AdminDeleteUser</li>
 *   <li>cognito-idp:AdminDisableUser</li>
 *   <li>cognito-idp:AdminEnableUser</li>
 *   <li>cognito-idp:AdminUserGlobalSignOut</li>
 * </ul>
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final AwsProperties aws;


    /**
     * 지정된 사용자를 Cognito User Pool에서 삭제 (회원 탈퇴)
     *
     * @param username Cognito 사용자명
     */
    public void deleteUser(String username) {
        var USER_POOL_ID = aws.getCognito().getUserPoolId();
        try {
            cognitoClient.adminDeleteUser(
                    AdminDeleteUserRequest.builder()
                            .userPoolId(USER_POOL_ID)
                            .username(username)
                            .build()
            );
            log.info("[Cognito] User deleted: {}", username);
        } catch (CognitoIdentityProviderException e) {
            log.error("[Cognito] Failed to delete user '{}': {}", username, e.awsErrorDetails().errorMessage(), e);
            throw e;
        }
    }

    /**
     * 지정된 사용자를 비활성화하여 로그인을 차단
     *
     * @param username Cognito 사용자명
     */
    public void disableUser(String username) {
        var USER_POOL_ID = aws.getCognito().getUserPoolId();
        try {
            cognitoClient.adminDisableUser(
                    AdminDisableUserRequest.builder()
                            .userPoolId(USER_POOL_ID)
                            .username(username)
                            .build()
            );
            log.info("[Cognito] User disabled: {}", username);
        } catch (CognitoIdentityProviderException e) {
            log.error("[Cognito] Failed to disable user '{}': {}", username, e.awsErrorDetails().errorMessage(), e);
            throw e;
        }
    }

    /**
     * 비활성화된 사용자를 다시 활성화
     *
     * @param username Cognito 사용자명
     */
    public void enableUser(String username) {
        var USER_POOL_ID = aws.getCognito().getUserPoolId();
        try {
            cognitoClient.adminEnableUser(
                    AdminEnableUserRequest.builder()
                            .userPoolId(USER_POOL_ID)
                            .username(username)
                            .build()
            );
            log.info("[Cognito] User enabled: {}", username);
        } catch (CognitoIdentityProviderException e) {
            log.error("[Cognito] Failed to enable user '{}': {}", username, e.awsErrorDetails().errorMessage(), e);
            throw e;
        }
    }

    /**
     * 사용자의 모든 디바이스 세션을 강제 만료시킴
     *
     * @param username Cognito 사용자명
     */
    public void globalSignOut(String username) {
        var USER_POOL_ID = aws.getCognito().getUserPoolId();
        try {
            cognitoClient.adminUserGlobalSignOut(
                    AdminUserGlobalSignOutRequest.builder()
                            .userPoolId(USER_POOL_ID)
                            .username(username)
                            .build()
            );
            log.info("[Cognito] Global sign-out executed for: {}", username);
        } catch (CognitoIdentityProviderException e) {
            log.error("[Cognito] Failed to sign out user globally '{}': {}", username, e.awsErrorDetails().errorMessage(), e);
            throw e;
        }
    }
}