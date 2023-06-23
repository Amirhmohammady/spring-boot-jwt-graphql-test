package com.javainuse.error_handeler;

/**
 * Created by Amir on 5/3/2023.
 */
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyGraphQLErrorHandler implements GraphQLErrorHandler {
    @Override
    public boolean errorsPresent(List<GraphQLError> errors) {
        return !CollectionUtils.isEmpty(errors);
    }

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {
        System.out.println("================MyGraphQLErrorHandler.processErrors================");
        errors.forEach(System.out::println);
        List<GraphQLError> clientErrors = filterClientErrors(errors);
        List<GraphQLError> unAuthorized = filterUnAuthorizedErrors(errors);
        if (unAuthorized.size()>0)throw new RuntimeException("Unauthorized request222");
        if (clientErrors.size() < errors.size()) {
            // Some errors were filtered out, so throw an exception
            throw new RuntimeException("Unauthorized request");
        }
        return clientErrors;
    }

    private List<GraphQLError> filterUnAuthorizedErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(error -> (error instanceof Throwable))
                .collect(Collectors.toList());
    }

    private List<GraphQLError> filterClientErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(error -> isClientError(error))
                .collect(Collectors.toList());
    }

    private boolean isClientError(GraphQLError error) {
        // Check if the error is a client error (i.e. caused by user input)
        return !(error instanceof Throwable);
    }
}