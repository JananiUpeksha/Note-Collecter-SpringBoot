package lk.ijse.notecollectorspring.service;

import lk.ijse.notecollectorspring.dto.UserStatus;
import lk.ijse.notecollectorspring.dto.impl.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserStatus getUser(String userId);

    void deleteUser(String userID);
    void updateUser(String userId, UserDTO userDTO);
    UserDetailsService userDetailsService();
    /*The userDetailsService() method returns a UserDetailsService implementation,
     which retrieves user data based on the username (email in this case).*/
}
