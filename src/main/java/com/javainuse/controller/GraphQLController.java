package com.javainuse.controller;

import com.javainuse.model.News;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 5/3/2023.
 */
@Controller
public class GraphQLController {
    @QueryMapping
    @Secured("ROLE_ADMIN")
    public List<News> getNews() {
        List<News> result = new ArrayList<>();
        result.add(new News(1, "title1", "desvription1"));
        result.add(new News(2, "title2", "desvription2"));
        result.add(new News(3, "title3", "desvription3"));
        return result;
    }
}
