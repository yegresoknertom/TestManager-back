package org.testmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserListDTO {

    private List<UserDTO> users;

    private Pagination pagination;

}
