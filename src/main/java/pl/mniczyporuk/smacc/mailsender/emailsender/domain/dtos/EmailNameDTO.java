package pl.mniczyporuk.smacc.mailsender.emailsender.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class EmailNameDTO {
    private final String email;
    private final String name;
}
