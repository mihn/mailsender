package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.sendgird;

import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTO;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class SendEmailDTOToSendGridEmailRequestConverter {
    SendGridEmailRequest convertToRequest(SendEmailDTO sendEmailDTO) {
        SendGridPersonalizations personalizations = convertToPersonalization(sendEmailDTO);
        SendGridNameEmail from = convertToNameEmail(sendEmailDTO.getFrom());
        SendGridContent content = convertToContent(sendEmailDTO.getBody());
        return new SendGridEmailRequest(personalizations, from, sendEmailDTO.getSubject(), content);
    }

    private SendGridContent convertToContent(String body) {
        return new SendGridContent(body);
    }

    private SendGridNameEmail convertToNameEmail(EmailNameDTO from) {
        return new SendGridNameEmail(from.getName(), from.getEmail());
    }

    private SendGridPersonalizations convertToPersonalization(SendEmailDTO sendEmailDTO) {
        Collection<SendGridNameEmail> to = convertToNameEmail(sendEmailDTO.getTo());
        Collection<SendGridNameEmail> cc = convertToNameEmail(sendEmailDTO.getCc());
        Collection<SendGridNameEmail> bcc = convertToNameEmail(sendEmailDTO.getBcc());
        return new SendGridPersonalizations(to, cc, bcc);
    }

    private Collection<SendGridNameEmail> convertToNameEmail(List<EmailNameDTO> email) {
        return email.stream()
                .map(this::convertToNameEmail)
                .collect(Collectors.toList());
    }
}
