package pl.mniczyporuk.smacc.mailsender.emailsender.domain;

import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;

public interface EmailClient {
    /**
     * Sends email via external service
     *
     * @param sendEmailDTO email contents
     * @return true if email was sent, false if it was not sent (even if error occurred)
     */
    boolean sendEmail(SendEmailDTO sendEmailDTO);
}
