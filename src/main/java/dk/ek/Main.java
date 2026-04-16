package dk.ek;

import dk.config.SessionConfig;
import dk.config.ThymeleafConfig;
import dk.controllers.AuthController;
import dk.controllers.CartController;
import dk.controllers.OrderController;
import dk.controllers.UserController;
import dk.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "cupcake";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin javApp = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            config.staticFiles.add("/templates");
        }).start(7070);


        // Routing
        AuthController.addRoutes(javApp, connectionPool);
        CartController.addRoutes(javApp, connectionPool);
        OrderController.addRoutes(javApp, connectionPool);
        UserController.addRoutes(javApp, connectionPool);

    }
}