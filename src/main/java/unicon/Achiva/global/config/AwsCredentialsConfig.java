package unicon.Achiva.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * AWS SDK credential 관련 Bean 구성
 */
@Configuration
@RequiredArgsConstructor
public class AwsCredentialsConfig {

    private final AwsProperties aws;

    @PostConstruct
    public void logRegion() {
        System.out.println("AWS region = " + aws.getRegion());
    }

    @Bean
    public S3Presigner s3Presigner() {
        var s3 = aws.getS3();
        return S3Presigner.builder()
                .region(Region.of(aws.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        s3.getAccessKeyId(),
                                        s3.getSecretAccessKey()
                                )
                        )
                )
                .build();
    }

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        var cognito = aws.getCognito();
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(aws.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        cognito.getAccessKeyId(),
                                        cognito.getSecretAccessKey()
                                )
                        )
                )
                .build();
    }
}