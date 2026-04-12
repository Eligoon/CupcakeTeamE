package dk.controllers;

import dk.entities.User;
import dk.persistence.ConnectionPool;
import dk.services.AuthService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AuthController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/login", ctx -> ctx.render("login.html"));

        app.post("/login", ctx -> login(ctx, cp));

        app.get("/logout", ctx -> logout(ctx));
    }

    private static void login(Context ctx, ConnectionPool cp) {

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = AuthService.login(email, password, cp);

            ctx.sessionAttribute("currentUser", user);

            ctx.redirect("/orders");

        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("login.html");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }
}