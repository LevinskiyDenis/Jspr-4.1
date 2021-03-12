package ru.netology.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private final String GET = "GET";
    private final String POST = "POST";
    private final String DEL = "DELETE";

    private final String PATHPOSTS = "/api/posts/";
    private final String PATHPOSTBYID = "/api/posts/\\d+";

    @Override
    public void init() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = applicationContext.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {

            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && Pattern.matches(PATHPOSTS, path)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && Pattern.matches(PATHPOSTBYID, path)) {
                // easy way
                final var id = MainServlet.parseId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && Pattern.matches(PATHPOSTS, path)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DEL) && Pattern.matches(PATHPOSTBYID, path)) {
                // easy way
                final var id = MainServlet.parseId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public static long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

