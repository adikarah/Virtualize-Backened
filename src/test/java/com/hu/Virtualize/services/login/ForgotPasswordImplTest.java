package com.hu.Virtualize.services.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;

class ForgotPasswordImplTest {

    @Mock
    private Environment env;

    @InjectMocks
    private SendMailImpl sendMail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void sendEmail() {
        sendMail.sendEmail("ForgotPassword","Your OTP to reset Password is qw124d","praveentripathi@gmail.com");
    }
}