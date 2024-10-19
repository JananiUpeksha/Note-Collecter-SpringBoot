package lk.ijse.notecollectorspring.dto.impl;

import lk.ijse.notecollectorspring.dto.UserStatus;
import lk.ijse.notecollectorspring.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDTO implements UserStatus {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profilePic;
    private Role role;
    private List<NoteDTO> notes;

}
