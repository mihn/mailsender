package pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos

import static EmailNameDTOTestBuilder.emailNameDTO

class SendEmailDTOTestBuilder {

    private static def DEFAULTS = [from   : emailNameDTO([:]),
                                   to     : [emailNameDTO([:])],
                                   cc     : [],
                                   bcc    : [],
                                   subject: "test subject",
                                   body   : 'test body']

    static SendEmailDTO sendEmailDTO(Map args) {
        def allArgs = DEFAULTS + args
        new SendEmailDTO(
                allArgs.from,
                allArgs.to,
                allArgs.cc,
                allArgs.bcc,
                allArgs.subject,
                allArgs.body)
    }

}
