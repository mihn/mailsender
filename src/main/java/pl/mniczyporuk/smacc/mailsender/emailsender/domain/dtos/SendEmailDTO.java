package pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos;

import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

@Getter
@ToString
public class SendEmailDTO {

    private final EmailNameDTO from;
    private final List<EmailNameDTO> to;
    private final List<EmailNameDTO> cc;
    private final List<EmailNameDTO> bcc;
    private final String subject;
    private final String body;

    @ConstructorProperties({"from", "to", "cc", "bcc", "subject", "body"})
    public SendEmailDTO(EmailNameDTO from,
                        List<EmailNameDTO> to,
                        List<EmailNameDTO> cc,
                        List<EmailNameDTO> bcc,
                        String subject,
                        String body) {
        this.from = from;
        this.to = to != null ? unmodifiableList(to) : emptyList();
        this.cc = cc != null ? unmodifiableList(cc) : emptyList();
        this.bcc = bcc != null ? unmodifiableList(bcc) : emptyList();
        this.subject = subject;
        this.body = body;
    }
}
