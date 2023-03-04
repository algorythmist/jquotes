package com.tecacet.jquotes.token;


import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class EnvironmentTokenSupplier implements TokenSupplier {

    private final String tokenVariableName;

    @Override
    public String getToken() {
        Map<String,String> env = System.getenv();
        return env.get(tokenVariableName);
    }
}
