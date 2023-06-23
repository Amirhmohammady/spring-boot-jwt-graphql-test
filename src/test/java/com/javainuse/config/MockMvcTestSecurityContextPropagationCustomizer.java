package com.javainuse.config;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;

//@Component
//@Profile("test")
public class MockMvcTestSecurityContextPropagationCustomizer implements MockMvcBuilderCustomizer {

    @Override
    public void customize(ConfigurableMockMvcBuilder<?> builder) {
        builder.alwaysDo(result -> {
            System.out.println("resetting SecurityContextHolder to TestSecurityContextHolder");
            SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        });
    }
}