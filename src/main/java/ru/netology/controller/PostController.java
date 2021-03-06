package ru.netology.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Controller;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

@Controller

public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXTORHTML = "text/html";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.all();
        final var gson = new Gson();
        Type type = new TypeToken<ConcurrentHashMap<Long, Post>>() {
        }.getType();
        response.getWriter().print(gson.toJson(data, type));
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var data = service.getById(id);
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);
        final var data = service.save(post);
        response.getWriter().print(gson.toJson(data));
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        response.setContentType(TEXTORHTML);
        final var isRemoved = service.removeById(id);
        if (isRemoved) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().print("???? ?????????????? ??????????????. ???????? ?? ?????????? id ???? ????????????????????");
        }
    }
}
