package org.example.modules.auth;

import org.example.dtos.AuthRequestDto;
import org.example.dtos.UserDto;
import org.example.models.User;
import org.example.modules.auth.jwt.JwtTokenService;
import org.example.modules.auth.jwt.JwtUserDetailsService;
import org.example.modules.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUserDetailsService jwtUserDetailsService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto authRequestDto) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequestDto.getEmail(), authRequestDto.getPassword()));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authRequestDto.getEmail());
        String jwtToken = jwtTokenService.generateToken(userDetails);
        return jwtToken != null ?
                ResponseEntity.ok(jwtToken) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        User createdUser = userService.create(user);
        return createdUser != null ?
                ResponseEntity.status(HttpStatus.CREATED).body(new UserDto(createdUser)) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
