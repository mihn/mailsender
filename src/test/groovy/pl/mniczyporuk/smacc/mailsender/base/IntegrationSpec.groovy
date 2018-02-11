package pl.mniczyporuk.smacc.mailsender.base

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import pl.mniczyporuk.smacc.mailsender.MailsenderApplication
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ContextConfiguration
@SpringBootTest(
        classes = [MailsenderApplication],
        webEnvironment = RANDOM_PORT
)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
abstract class IntegrationSpec extends Specification {

    @Autowired
    protected TestRestTemplate restTemplate
}