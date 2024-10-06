package lk.ijse.notecollectorspring.controller;

import lk.ijse.notecollectorspring.customStatusCode.SelectedUserErrorStatus;
import lk.ijse.notecollectorspring.dto.UserStatus;
import lk.ijse.notecollectorspring.dto.impl.UserDTO;
import lk.ijse.notecollectorspring.exception.DataPersistException;
import lk.ijse.notecollectorspring.exception.UserNotFoundException;
import lk.ijse.notecollectorspring.service.UserService;
import lk.ijse.notecollectorspring.utill.AppUtill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;
    /*PostMan Content Type - multipart/form-data; boundary=<calculated when request is sent> */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUser(@RequestPart("firstName") String firstName,/*ena request eke part ekak thiynwa firstname kiyla eywa me firstNameta assign karanna*/
                             @RequestPart("lastName")String lastName,
                                   @RequestPart("email")String email,
                                   @RequestPart("password")String password,
                                   @RequestPart("profilePic") MultipartFile profilePic)
    {
        //convert profile pic into Base64
        System.out.println("RAW pro pic " + profilePic);
        String base64ProfilePic = ""; //appUtill eken return krnne string ekak eka allagnna thma empty string eka
        try{
            byte[] bytesProPic = profilePic.getBytes();
            base64ProfilePic = AppUtill.profilePicToBase64(bytesProPic); //argument eka widihata pass krnne [] eka eka apputill eken return krnne string nisa assign krnwa string ekakata
            //generate user id
            String usersId = AppUtill.generateUserId();
            //build the object
            var buildUserDt0 = new UserDTO();
            buildUserDt0.setUserId(usersId);
            buildUserDt0.setFirstName(firstName);
            buildUserDt0.setLastName(lastName);
            buildUserDt0.setEmail(email);
            buildUserDt0.setPassword(password);
            buildUserDt0.setProfilePic(base64ProfilePic);
            userService.saveUser(buildUserDt0);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)/*http://localhost:8080/api/v1/user/USER-7bc4d5d5-e938-4bca-af52-74a9849bae0b*/
    public UserStatus getSelectedUser(@PathVariable("userId") String userId){
        String regexForUserID = "^USER-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserID);
        var regexMatcher = regexPattern.matcher(userId);

        if(!regexMatcher.matches()){
            return new SelectedUserErrorStatus(1,"User ID is not valid");
        }
        return userService.getUser(userId);
    }

    /*@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) http://localhost:8080/api/v1/user?userId=123
    public UserDTO getSelectedUser(@RequestParam("userId") String userId) {
        return userService.getUser(userId);
    }*/

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId){
        String regexForUserID = "^USER-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserID);
        var regexMatcher = regexPattern.matcher(userId);
        try {
            if(!regexMatcher.matches()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UserNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUser(@RequestPart("firstName") String firstName,
                           @RequestPart("lastName") String lastName,
                           @RequestPart("email") String email,
                           @RequestPart("password") String password,
                           @RequestPart("profilePic") MultipartFile profilePic,
                           @PathVariable("userId") String userId) {
        System.out.println("RAW pro pic " + profilePic);
        String base64ProfilePic = "";

        try {
            byte[] bytesProPic = profilePic.getBytes();
            base64ProfilePic = AppUtill.profilePicToBase64(bytesProPic);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var buildUserDTO = new UserDTO();
        buildUserDTO.setUserId(userId);
        buildUserDTO.setFirstName(firstName);
        buildUserDTO.setLastName(lastName);
        buildUserDTO.setEmail(email);
        buildUserDTO.setPassword(password);
        buildUserDTO.setProfilePic(base64ProfilePic);
        userService.updateUser(userId, buildUserDTO);
    }

}
