package org.testmanager.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String login;

    private String password;

}
