package dk.ek;

import dk.config.SessionConfig;
import dk.config.ThymeleafConfig;
import dk.controllers.*;
import dk.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.TemplateEngine;

public class Main {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool =
            ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {

        TemplateEngine engine = ThymeleafConfig.templateEngine();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.jetty.modifyServletContextHandler(handler ->
                    handler.setSessionHandler(SessionConfig.sessionConfig())
            );
            config.fileRenderer(new JavalinThymeleaf(engine));
        }).start(7070);

        // ROUTES
        AuthController.addRoutes(app, connectionPool);
        CartController.addRoutes(app, connectionPool);
        OrderController.addRoutes(app, connectionPool);
        UserController.addRoutes(app, connectionPool);
        HomeController.addRoutes(app, connectionPool);
    }
}