package com.javainuse.error_handeler;

import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Amir on 5/3/2023.
 */
@Component
public class MyDataFetcherExceptionHandler implements DataFetcherExceptionHandler {
    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        System.out.println("32e23r45235");
        DataFetcherExceptionHandlerResult result = onException(handlerParameters);
        return CompletableFuture.completedFuture(result);
    }
}
