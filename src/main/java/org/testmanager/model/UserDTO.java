package org.testmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserDTO {

    @NotNull
    @Size(min=1, max = 100)
    private String login;

    @NotNull
    @Size(min=1, max = 100)
    private String password;

}
