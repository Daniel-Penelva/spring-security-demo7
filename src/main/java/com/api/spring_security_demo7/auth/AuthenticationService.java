package com.api.spring_security_demo7.auth;

import com.api.spring_security_demo7.auth.request.AuthenticationRequest;
import com.api.spring_security_demo7.auth.request.RefreshRequest;
import com.api.spring_security_demo7.auth.request.RegistrationRequest;
import com.api.spring_security_demo7.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register(RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest req);
}
