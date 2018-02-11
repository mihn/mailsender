package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.rest;

import org.springframework.web.bind.annotation.*;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.EmailSender;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO;
import pl.mniczyporuk.smacc.mailsender.infrastructure.rest.APIVersion;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping(value = "/api/send-email", produces = APIVersion.V1)
class SendEmailController {

    private final EmailSender emailSender;

    SendEmailController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostMapping
    @ResponseStatus(ACCEPTED)
    void sendEmail(@RequestBody SendEmailDTO sendEmailDTO) {
        emailSender.sendEmail(sendEmailDTO);
    }

}
