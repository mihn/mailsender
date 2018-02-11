package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.aws;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class AwsSesEmailClientProperties {
    private String accessKey;
    private String secretKey;
    private String region;
    private int socketTimeout;
    private int requestTimeout;
    private int connectionTimeout;
}
