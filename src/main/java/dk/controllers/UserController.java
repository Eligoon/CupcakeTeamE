package dk.controllers;

import dk.persistence.ConnectionPool;
import dk.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/register", ctx -> ctx.render("register.html"));

        app.post("/register", ctx -> register(ctx, cp));
    }

    private static void register(Context ctx, ConnectionPool cp) {

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            UserService.registerUser(email, password, "customer", cp);

            ctx.redirect("/login");

        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("register.html");
        }
    }
}