package pl.mniczyporuk.smacc.mailsender.emailsender.domain.exceptions;

public class EmailWasNotSent extends RuntimeException {
    public EmailWasNotSent() {
        super("Email was not sent");
    }
}
