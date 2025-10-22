package unicon.Achiva.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * .env 또는 환경변수에서 AWS_* 프로퍼티를 매핑한다.
 */
@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {

    private String region;
    private final S3 s3 = new S3();
    private final Cognito cognito = new Cognito();

    @Data
    public static class S3 {
        private String accessKeyId;
        private String secretAccessKey;
        private String bucketName;
    }

    @Data
    public static class Cognito {
        private String accessKeyId;
        private String secretAccessKey;
        private String userPoolId;
    }
}