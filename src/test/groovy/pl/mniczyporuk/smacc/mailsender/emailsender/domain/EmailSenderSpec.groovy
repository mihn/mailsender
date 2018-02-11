package pl.mniczyporuk.smacc.mailsender.emailsender.domain

import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTOTestBuilder
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.exceptions.EmailWasNotSent
import spock.lang.Specification

class EmailSenderSpec extends Specification {

    EmailClient firstEmailClient = Mock(EmailClient)
    EmailClient secondEmailClient = Mock(EmailClient)

    EmailSender emailSender = new EmailSenderConfiguration().emailSender([firstEmailClient, secondEmailClient])

    def "sends email through first client"() {
        given:
          SendEmailDTO sendEmailDTO = SendEmailDTOTestBuilder.sendEmailDTO([:])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          1 * firstEmailClient.sendEmail(sendEmailDTO) >> true
    }

    def "sends email through second client if first one fails to send"() {
        given:
          SendEmailDTO sendEmailDTO = SendEmailDTOTestBuilder.sendEmailDTO([:])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          1 * firstEmailClient.sendEmail(sendEmailDTO) >> false
          1 * secondEmailClient.sendEmail(sendEmailDTO) >> true
    }

    def "report error if all clients fail to send message"() {
        given:
          SendEmailDTO sendEmailDTO = SendEmailDTOTestBuilder.sendEmailDTO([:])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          1 * firstEmailClient.sendEmail(sendEmailDTO) >> false
          1 * secondEmailClient.sendEmail(sendEmailDTO) >> false
          thrown(pl.mniczyporuk.smacc.mailsender.emailsender.domain.exceptions.EmailWasNotSent)
    }
}
