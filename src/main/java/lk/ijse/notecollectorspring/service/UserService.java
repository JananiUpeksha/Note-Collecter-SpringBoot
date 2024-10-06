package lk.ijse.notecollectorspring.service;

import lk.ijse.notecollectorspring.dto.UserStatus;
import lk.ijse.notecollectorspring.dto.impl.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserStatus getUser(String userId);

    void deleteUser(String userID);
    void updateUser(String userId, UserDTO userDTO);
}
