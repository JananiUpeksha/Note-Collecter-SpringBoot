package lk.ijse.notecollectorspring.service;

import lk.ijse.notecollectorspring.dao.UserDAO;
import lk.ijse.notecollectorspring.dto.impl.UserDTO;
import lk.ijse.notecollectorspring.entity.impl.UserEntity;
import lk.ijse.notecollectorspring.exception.UserNotFoundException;
import lk.ijse.notecollectorspring.secure.JWTAuthResponse;
import lk.ijse.notecollectorspring.secure.SignIn;
import lk.ijse.notecollectorspring.utill.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    //constructor injection mekedi withri final krnna pluwn
    private final UserDAO userDAO;
    //autowired dmmama eka field injection ekak
    private final Mapping mapping; //methnin ena dto eka dao ekta ywanna entity ekak krnna
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
       //already rejisted user knekwa application eke me welawe principle user krnna one
        //eywa principle user krnna one structure eka hdnwa ewa auth providersla krnwa
        //auth con signin eka hdla meka hdnne
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        var user = userDAO.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //user naththn exception ekak denwa innwa nm eywa base krn token ekak hdla pass krnwa
        var generatedToken = jwtService.generateToken(user);
        return JWTAuthResponse.builder().token(generatedToken).build();

    }

    @Override
    public JWTAuthResponse signUp(UserDTO userDTO) {
        //apu user save krnna one , e apu user wenuwen token ekak issue krnna one
        //save user
        UserEntity savedUser = userDAO.save(mapping.toUserEntity(userDTO));
        //generate token using JWTService
        var generatedToken = jwtService.generateToken(savedUser);
        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //userta against thma refresh krnne
        //extract user name
        var userName = jwtService.extractUserName(accessToken);
        //isella uery krla blnwa ehema user knek innwd kiyla db eke
        var findUser = userDAO.findByEmail(userName)
        //innwa nm eya wenuwen refresh token ekak generate krnwa
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var refreshToken = jwtService.refreshToken(findUser);
        return JWTAuthResponse.builder().token(refreshToken).build();
    }
}
