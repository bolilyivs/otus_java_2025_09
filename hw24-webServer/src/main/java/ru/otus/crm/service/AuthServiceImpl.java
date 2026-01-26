package ru.otus.crm.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final DBServiceClient dbServiceClient;

    @Override
    public boolean authenticate(String login, String password) {
        return dbServiceClient.findByLoginAndPassword(login, password).isPresent();
    }
}
