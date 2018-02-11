package pl.mniczyporuk.smacc.mailsender.emailsender.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
class EmailSenderConfiguration {

    @Bean
    EmailSender emailSender(Collection<EmailClient> emailClients) {
        return new EmailSender(emailClients);
    }
}
