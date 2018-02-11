package pl.mniczyporuk.smacc.mailsender.emailsender.adapter.email.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mniczyporuk.smacc.mailsender.emailsender.domain.EmailClient;

@Configuration
@ConditionalOnProperty(prefix = "mailsender.aws.ses", value = "accessKey")
class AwsSesEmailClientConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "mailsender.aws.ses")
    AwsSesEmailClientProperties awsSesEmailClientProperties() {
        return new AwsSesEmailClientProperties();
    }

    @Bean
    EmailClient awsSesEmailClient(AmazonSimpleEmailService amazonSimpleEmailService) {
        return new AwsSesEmailClient(amazonSimpleEmailService);
    }

    @Bean
    AmazonSimpleEmailService getAmazonSimpleEmailService() {
        AwsSesEmailClientProperties properties = awsSesEmailClientProperties();
        BasicAWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(),
                properties.getSecretKey());
        ClientConfiguration configuration = new ClientConfiguration()
                .withSocketTimeout(properties.getSocketTimeout())
                .withRequestTimeout(properties.getRequestTimeout())
                .withConnectionTimeout(properties.getConnectionTimeout());
        AmazonSimpleEmailServiceClientBuilder clientBuilder = AmazonSimpleEmailServiceClientBuilder.
                standard()
                .withRegion(properties.getRegion())
                .withClientConfiguration(configuration);
        clientBuilder.setCredentials(new AWSStaticCredentialsProvider(credentials));
        return clientBuilder.build();
    }
}
