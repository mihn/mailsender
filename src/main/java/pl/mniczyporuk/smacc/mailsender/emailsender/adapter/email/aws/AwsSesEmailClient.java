package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.aws;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.EmailClient;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;

@Slf4j
class AwsSesEmailClient implements EmailClient {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final SendEmailDTOToAwsSesEmailClientConverter sendEmailDTOToAwsSesEmailClientConverter;

    AwsSesEmailClient(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.sendEmailDTOToAwsSesEmailClientConverter = new SendEmailDTOToAwsSesEmailClientConverter();
    }

    @Override
    public boolean sendEmail(SendEmailDTO sendEmailDTO) {
        log.debug("sending email via AWS SES {}", sendEmailDTO);
        try {
            SendEmailRequest sendEmailRequest =
                    sendEmailDTOToAwsSesEmailClientConverter.convertToSendMailRequest(sendEmailDTO);
            amazonSimpleEmailService.sendEmail(sendEmailRequest);
            log.debug("sent email via AWS SES");
            return true;
        } catch (Exception e) {
            log.error("caught exception when sending message through AWS SES", e);
            return false;
        }
    }
}