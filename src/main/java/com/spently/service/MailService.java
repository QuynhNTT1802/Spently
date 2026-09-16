package com.spently.service;

public interface MailService {
    void sendForgotPasswordMail(String toMail, String username, String rawPassword, String token);
}