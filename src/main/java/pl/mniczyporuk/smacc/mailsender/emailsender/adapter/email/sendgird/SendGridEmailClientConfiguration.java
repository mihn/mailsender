package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.sendgird;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.EmailClient;

@Configuration
@ConditionalOnProperty(prefix = "mailsender.sendgrid", value = "apiKey")
class SendGridEmailClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "mailsender.sendgrid")
    SendGridEmailClientProperties sendGridEmailClientProperties() {
        return new SendGridEmailClientProperties();
    }

    @Bean
    EmailClient sendGridEmailClient() {
        return new SendGridEmailRestV3Client(sendGridEmailClientProperties());
    }
}
