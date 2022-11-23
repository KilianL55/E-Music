package edu.caensup.sio.emusic.pojo;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import lombok.Data;
import lombok.NonNull;

public class ActiveUser {
    @NonNull
    private Authentication authentication;

    public ActiveUser(@NonNull Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public boolean isConnected() {
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
}