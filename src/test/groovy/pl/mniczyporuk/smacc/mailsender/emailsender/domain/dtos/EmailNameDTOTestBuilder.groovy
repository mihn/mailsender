package pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos

class EmailNameDTOTestBuilder {

    private static Map DEFAULTS = [name : null,
                                   email: "john@example.com"]

    static EmailNameDTO emailNameDTO(Map args) {
        def allArgs = DEFAULTS + args
        new EmailNameDTO(allArgs.email, allArgs.name)
    }
}
