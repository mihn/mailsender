package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.aws;

import com.amazonaws.services.simpleemail.model.*;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTO;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;

import java.util.List;
import java.util.stream.Collectors;

class SendEmailDTOToAwsSesEmailClientConverter {
    SendEmailRequest convertToSendMailRequest(SendEmailDTO sendEmailDTO) {
        return new SendEmailRequest()
                .withSource(sendEmailDTO.getFrom().getEmail())
                .withDestination(new Destination()
                        .withToAddresses(extractEmailAddresses(sendEmailDTO.getTo()))
                        .withCcAddresses(extractEmailAddresses(sendEmailDTO.getCc()))
                        .withBccAddresses(extractEmailAddresses(sendEmailDTO.getBcc()))
                )
                .withMessage(new Message()
                        .withSubject(createUTF8ContentWithData(sendEmailDTO.getSubject()))
                        .withBody(new Body().withText(createUTF8ContentWithData(sendEmailDTO.getBody()))));
    }

    private List<String> extractEmailAddresses(List<EmailNameDTO> emailNameDTOS) {
        return emailNameDTOS
                .stream()
                .map(EmailNameDTO::getEmail)
                .collect(Collectors.toList());
    }

    private Content createUTF8ContentWithData(String subject) {
        return new Content().withCharset("UTF-8").withData(subject);
    }
}
