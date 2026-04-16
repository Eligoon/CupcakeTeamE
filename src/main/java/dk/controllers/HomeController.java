package dk.controllers;

import dk.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class HomeController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/", HomeController::showIndex);
    }

    private static void showIndex(Context ctx) {

        // optional: redirect logged-in users or load data here
        ctx.render("index.html");
    }
}