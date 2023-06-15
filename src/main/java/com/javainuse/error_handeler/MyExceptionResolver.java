package com.javainuse.error_handeler;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;


/**
 * Created by Amir on 5/3/2023.
 */
@Component
public class MyExceptionResolver extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        System.out.println("asdfew342345");
        System.out.println(ex.getClass());
        if (ex instanceof AccessDeniedException) {
            System.out.println("awer11111111");
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message("asasasasas")//ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else
            System.out.println("awer22222222");
            return null;
    }
}
