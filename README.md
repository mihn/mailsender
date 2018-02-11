# MAIL SENDER
## Description
Main goal of this application is to send emails (just text, no attachments) via various APIs.
By default there are two implementations (SendGrid, AWS SES) - if first fails to send an email, second one tries to deliver the message.
If both fail then error is raised on endpoint with proper status code.
### Using API Directly vs using provided client
I decided to use SendGrid via REST API directly, because their client was very simplistic.
On the other hand I decided to use AWS SES client, because their API is awful and client hid the complexity nicely. 
## API description
There is a single endpoint: POST to `/api/send-email`
 
It requires HTTP header `Accept: application/smacc.mailsender.v1+json`

Sample request:
```json 
{
  "from": {  // required
     "name":"John Example", // optional
     "email":"john@example.org" // required
  },  
  "to": [  // required - up to 1000 recipients in To field 
    {
      "name":"John Example", // optional
      "email":"john@example.org" - required
      }],
  "cc": [  // optional - up to 1000 recipients in CC field
   {
      "name":"John Example", // optional
      "email":"john@example.org" - required
      }], 
  "bcc": [  // optional - up to 1000 recipients in BCC field
    {
      "name":"John Example", // optional
      "email":"john@example.org" - required
      }], 
  "subject": "Test Subject",// required - up to 1000 characters
  "body": "Test Body"//// required - up to 10000 characters
}
```
## Security
There is a security setup - HTTP Basic - it can be configured in `application.yml`.
 
## Requirements

1. openjdk version "1.8.0_151"
 
## Installation
1. Build the package the file using `mvnw package` and upload resulting `mailsender-0.0.1-SNAPSHOT.jar` file to the server.
2. You have to create `application.yml` file in same directory as `mailsender-0.0.1-SNAPSHOT.jar` on the server
```yaml
spring.profiles.active: prod
mailsender:
  sendgrid.apiKey: //your SendGrid apikey> - optional - if not provided sendgrid email client wont be configured 
  aws.ses:
    accessKey: //SES accessKey -  optional - if not provided AWS SES email client wont be configured
    secretKey: // SES secretKey - required if accessKey is provided
security.user:
  name: // user for authorization - optinal - default one is 'user'
  password: // pasword for authorization - optional - if not provided default one will be generated and printed in console 
```
3. You can start this application in `screen` using `./mailsender-0.0.1-SNAPSHOT.jar` (might need to do `chmod 500` on it) or run it as [systemd script][1] 
## Improvements 

1. Going reactive with RXJava/Spring Webflux to improve throughput
2. Implementing circuit breaker (Hystrix/resilience4j) around calls to outside services - to fail much faster in case of frequent failures.
3. Metrics around Controllers, and outsides systems (to monitor for problems or failures - even circuits breakers statistics for improvements)
4. Its fine to log to console in dev environment, but in production logs should be pulled by infrastructure to outside log aggregator (i.e. ELK or Graylog)

[1]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-systemd-service
  