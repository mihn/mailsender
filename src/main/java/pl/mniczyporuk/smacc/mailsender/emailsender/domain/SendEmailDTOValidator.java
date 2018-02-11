package pl.mniczyporuk.smacc.mailsender.emailsender.domain;

import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTO;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

class SendEmailDTOValidator {
    private final static Pattern EMAIL_REGEX = Pattern.compile(".+\\@.+\\..+");
    private static final int EMAIL_MAXIMUM_LENGTH = 254;

    void validateRequest(SendEmailDTO sendEmailDTO) {
        notNull(sendEmailDTO.getFrom(), "sender must be provided");
        checkIfEmailIsValid("From", sendEmailDTO.getFrom().getEmail());
        isTrue(sendEmailDTO.getTo().size() > 0 && sendEmailDTO.getTo().size() <= 1_000, "you must provide between 1 and 1000 recipients");
        validateEmails("To", sendEmailDTO.getTo());
        isTrue(sendEmailDTO.getCc().size() <= 1_000, "you can provide at most 1000 CC recipients");
        validateEmails("Cc", sendEmailDTO.getCc());
        isTrue(sendEmailDTO.getBcc().size() <= 1_000, "you can provide at most 1000 BCC recipients");
        validateEmails("Bcc", sendEmailDTO.getBcc());
        isTrue(isBetween(sendEmailDTO.getSubject(), 1, 1_000),
                "subject must have length between 1 and 1000 characters");
        isTrue(isBetween(sendEmailDTO.getBody(), 1, 10_000),
                "body must have length between 1 and 10000 characters");
    }

    private boolean isBetween(String string, int rangeStart, int rangeEnd) {
        return string != null && string.length() >= rangeStart && string.length() <= rangeEnd;
    }

    private void checkIfEmailIsValid(String field, String email) {
        if (email.length() > EMAIL_MAXIMUM_LENGTH) { // https://stackoverflow.com/a/574698/457342
            throw new IllegalArgumentException(format(ENGLISH, "%s: %s should not be longer then %d characters",
                    field,
                    email,
                    EMAIL_MAXIMUM_LENGTH));
        }
        if (!EMAIL_REGEX.matcher(email).find()) {
            throw new IllegalArgumentException(format(ENGLISH, "%s: %s is not valid email address",
                    field,
                    email
            ));
        }
    }

    private void validateEmails(String field, List<EmailNameDTO> emails) {
        emails.stream()
                .map(EmailNameDTO::getEmail)
                .forEach(email -> checkIfEmailIsValid(field, email));
    }
}

