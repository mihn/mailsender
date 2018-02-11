package pl.mniczyporuk.smacc.mailsender.emailsender.domain;

import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.exceptions.EmailWasNotSent;

import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

public class EmailSender {

    private final Collection<EmailClient> emailClients;
    private final SendEmailDTOValidator sendEmailDTOValidator;

    public EmailSender(Collection<EmailClient> emailClients) {
        this.emailClients = unmodifiableCollection(emailClients);
        this.sendEmailDTOValidator = new SendEmailDTOValidator();
    }

    public void sendEmail(SendEmailDTO sendEmailDTO) {
        sendEmailDTOValidator.validateRequest(sendEmailDTO);
        for (EmailClient emailClient : emailClients) {
            if (emailClient.sendEmail(sendEmailDTO)) {
                return;
            }
        }
        throw new EmailWasNotSent();
    }
}
