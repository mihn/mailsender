package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.sendgird;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.EmailClient;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;
import pl.mniczyporuk.smacc.mailsender.infrastructure.rest.HttpHeaderInterceptor;

import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.springframework.http.HttpStatus.ACCEPTED;

@Slf4j
class SendGridEmailRestV3Client implements EmailClient {
    private final RestTemplate restTemplate;
    private final SendEmailDTOToSendGridEmailRequestConverter converter = new SendEmailDTOToSendGridEmailRequestConverter();

    public SendGridEmailRestV3Client(SendGridEmailClientProperties properties) {
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(properties.getConnectionTimeout())
                .setReadTimeout(properties.getSocketTimeout())
                .interceptors(new HttpHeaderInterceptor("Authorization",
                        format(ENGLISH, "Bearer %s", properties.getApiKey())))
                .uriTemplateHandler(new RootUriTemplateHandler(properties.getBaseUrl()))
                .build();
    }

    @Override
    public boolean sendEmail(SendEmailDTO sendEmailDTO) {
        log.debug("sending email via SendGrid {}", sendEmailDTO);
        SendGridEmailRequest request = converter.convertToRequest(sendEmailDTO);
        try {
            ResponseEntity<Void> voidResponseEntity = this.restTemplate.postForEntity("/v3/mail/send", request, Void.class);
            return voidResponseEntity.getStatusCode().equals(ACCEPTED);
        } catch (RestClientException e) {
            log.error("received error while sending email via sendgrid", e);
            return false;
        }
    }
}

@Getter
class SendGridEmailRequest {
    private final Collection<SendGridPersonalizations> personalizations;
    private final SendGridNameEmail from;
    private final SendGridNameEmail reply_to;
    private final String subject;
    private final Collection<SendGridContent> content;

    public SendGridEmailRequest(SendGridPersonalizations personalizations,
                                SendGridNameEmail from,
                                String subject,
                                SendGridContent content) {
        this.personalizations = singletonList(personalizations);
        this.from = from;
        this.reply_to = from;
        this.subject = subject;
        this.content = singletonList(content);
    }
}

@AllArgsConstructor
@Getter
class SendGridPersonalizations {
    @JsonInclude(value = NON_EMPTY)
    private final Collection<SendGridNameEmail> to;
    @JsonInclude(value = NON_EMPTY)
    private final Collection<SendGridNameEmail> cc;
    @JsonInclude(value = NON_EMPTY)
    private final Collection<SendGridNameEmail> bcc;
}

@AllArgsConstructor
@Getter
class SendGridNameEmail {
    private final String name;
    private final String email;
}

@Getter
class SendGridContent {
    private final String type = "text/plain";
    private final String value;

    public SendGridContent(String value) {
        this.value = value;
    }
}
