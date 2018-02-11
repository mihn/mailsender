package pl.mniczyporuk.smacc.mailsender.emailsender.domain

import spock.lang.Specification

import static pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTOTestBuilder.emailNameDTO
import static pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTOTestBuilder.sendEmailDTO

class EmailSenderValidationSpec extends Specification {

    EmailClient firstEmailClient = Mock(EmailClient)

    EmailSender emailSender = new EmailSenderConfiguration().emailSender([firstEmailClient])

    def "report error if email has no  sender address"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([from: null])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "sender must be provided"
    }


    def "report error if sender email is too long"() {
        def emailTooLong = emailWithLenght(255)
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([from: emailNameDTO([email: emailTooLong])])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "From: ${emailTooLong} should not be longer then 254 characters"
    }

    def "report error if email has no valid sender address"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([from: emailNameDTO([email: "test@"])])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "From: test@ is not valid email address"
    }

    def "report error if email has no  recipient addresses"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([to: null])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "you must provide between 1 and 1000 recipients"
    }

    def "report error if email has more then 1000 recepient addresses in ToField"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([to: generateEmailNameDTOColection(1001)])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "you must provide between 1 and 1000 recipients"
    }

    def "report error if recipients email is too long"() {
        def emailTooLong = emailWithLenght(255)
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([to: [emailNameDTO([email: emailTooLong])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "To: ${emailTooLong} should not be longer then 254 characters"
    }

    def "report error if email has no valid recipient addresses"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([to: [emailNameDTO([email: "test@"])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "To: test@ is not valid email address"
    }

    def "report error if email has more then 1000 carbon copy recipients"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([cc: generateEmailNameDTOColection(1001)])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "you can provide at most 1000 CC recipients"
    }

    def "report error if carbon copy recipient email is too long"() {
        def emailTooLong = emailWithLenght(255)
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([cc: [emailNameDTO([email: emailTooLong])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "Cc: ${emailTooLong} should not be longer then 254 characters"
    }

    def "report error if email has no valid carbon copy recipient addresses"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([cc: [emailNameDTO([email: "test@"])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "Cc: test@ is not valid email address"
    }

    def "report error if email has more then 1000 blind carbon copy recipients"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([bcc: generateEmailNameDTOColection(1001)])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "you can provide at most 1000 BCC recipients"
    }

    def "report error if blind carbon copy recipient email is too long"() {
        def emailTooLong = emailWithLenght(255)
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([bcc: [emailNameDTO([email: emailTooLong])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "Bcc: ${emailTooLong} should not be longer then 254 characters"
    }

    def "report error if email has no valid blind carbon copy recipient addresses"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([bcc: [emailNameDTO([email: "test@"])]])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "Bcc: test@ is not valid email address"
    }

    def "report error if subject is null"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([subject: null])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "subject must have length between 1 and 1000 characters"
    }

    def "report error if subject is empty"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([subject: ""])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "subject must have length between 1 and 1000 characters"
    }

    def "report error if subject is over 1000 characters"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([subject: generateString(1001)])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "subject must have length between 1 and 1000 characters"
    }

    def "report error if body is null"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([body: null])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "body must have length between 1 and 10000 characters"
    }

    def "report error if body is empty"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([body: ""])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "body must have length between 1 and 10000 characters"
    }

    def "report error if body is over 10000 characters"() {
        given:
          pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO sendEmailDTO = sendEmailDTO([body: generateString(10001)])
        when:
          emailSender.sendEmail(sendEmailDTO)
        then:
          IllegalArgumentException e = thrown(IllegalArgumentException)
          e.message == "body must have length between 1 and 10000 characters"
    }

    private Collection<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTO> generateEmailNameDTOColection(int count) {
        Collection<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.EmailNameDTO> elements = []
        (1..count).each { elements.add(emailNameDTO([:])) }
        elements
    }

    private String generateString(int length) {
        StringBuilder sb = new StringBuilder()
        (1..length).each { sb.append(" ") }
        sb.toString()
    }

    private String emailWithLenght(int length) {
        StringBuilder sb = new StringBuilder()
        sb.append("john@")
        (1..length - 8).each { sb.append("a") }
        sb.append(".com")
        sb.toString()
    }
}