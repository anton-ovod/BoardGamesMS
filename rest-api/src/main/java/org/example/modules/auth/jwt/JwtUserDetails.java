package org.example.modules.auth.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class JwtUserDetails extends User {
    public final Long id;

    public JwtUserDetails(Long id,
                          String username,
                          String password,
                          GrantedAuthority authority) {
        super(username, password, Collections.singletonList(authority));
        this.id = id;
    }
}
