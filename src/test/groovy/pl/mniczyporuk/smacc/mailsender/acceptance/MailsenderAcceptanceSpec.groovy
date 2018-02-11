package pl.mniczyporuk.smacc.mailsender.acceptance

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.SendEmailResult
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import spock.mock.DetachedMockFactory

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTOTestBuilder.sendEmailDTO
import static pl.mniczyporuk.smacc.mailsender.infrastructure.rest.APIVersion.V1

class MailsenderAcceptanceSpec extends pl.mniczyporuk.smacc.mailsender.base.IntegrationSpec {

    @Autowired
    AmazonSimpleEmailService amazonSimpleEmailService

    def "should send email via sendgrid service"() {
        given:
          stubSendGridForReturnStatus(202)
          HttpEntity<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO> entity = createHttpEntityWithBodyAndHeaders(sendEmailDTO([:]), V1)
        when:
          def responseEntity = restTemplate.postForEntity('/api/send-email', entity, Void)
        then:
          responseEntity.getStatusCode() == HttpStatus.ACCEPTED
    }

    def "should send email via AWS service if fails though Sendgrid"() {
        given:
          stubSendGridForReturnStatus(503)

          HttpEntity<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO> entity = createHttpEntityWithBodyAndHeaders(sendEmailDTO([:]), V1)
        when:
          def responseEntity = restTemplate.postForEntity('/api/send-email', entity, Void)
        then:
          responseEntity.getStatusCode() == HttpStatus.ACCEPTED
          1 * amazonSimpleEmailService.sendEmail(_) >> new SendEmailResult().withMessageId(UUID.randomUUID().toString())
    }

    def "report error if email wasnt sent "() {
        given:
          stubSendGridForReturnStatus(503)

          HttpEntity<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO> entity = createHttpEntityWithBodyAndHeaders(sendEmailDTO([:]), V1)
        when:
          def responseEntity = restTemplate.postForEntity('/api/send-email', entity, Void)
        then:
          responseEntity.getStatusCode() == HttpStatus.BAD_GATEWAY
          1 * amazonSimpleEmailService.sendEmail(_) >> { args -> throw new RuntimeException("testing failure") }
    }


    private StubMapping stubSendGridForReturnStatus(Integer status) {
        stubFor(post(urlEqualTo('/v3/mail/send'))
                .withHeader("Authorization", equalTo("Bearer TestApiKey"))
                .willReturn(WireMock.aResponse().withStatus(status)))
    }

    private HttpEntity<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO> createHttpEntityWithBodyAndHeaders(pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO body, String accectHeaderValue) {
        HttpHeaders headers = new HttpHeaders()
        headers.put('Accept', [accectHeaderValue])
        HttpEntity<pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos.SendEmailDTO> entity = new HttpEntity<>(body, headers)
        entity
    }

    @TestConfiguration
    static class MailsenderSpecConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        AmazonSimpleEmailService amazonSimpleEmailService() {
            return detachedMockFactory.Mock(AmazonSimpleEmailService)
        }

        @Bean
        RestTemplateBuilder restTemplateBuilder(SecurityProperties securityProperties) {
            return new RestTemplateBuilder()
                    .basicAuthorization(securityProperties.user.name, securityProperties.user.password);
        }
    }
}
