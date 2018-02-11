package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.sendgird;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class SendGridEmailClientProperties {
    private String apiKey;
    private String baseUrl;
    private int connectionTimeout;
    private int socketTimeout;
}
