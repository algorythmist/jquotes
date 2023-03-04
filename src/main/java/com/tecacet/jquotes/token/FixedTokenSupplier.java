package com.tecacet.jquotes.token;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FixedTokenSupplier implements TokenSupplier {

    private final String token;

    @Override
    public String getToken() {
        return token;
    }
}
