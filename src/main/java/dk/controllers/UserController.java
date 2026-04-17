package dk.controllers;

import dk.persistence.ConnectionPool;
import dk.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/register", ctx -> ctx.render("register.html"));

        app.post("/register", ctx -> register(ctx, cp));

        app.get("/logout", ctx-> logout(ctx));
    }

    private static void register(Context ctx, ConnectionPool cp) {

        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String firstName = ctx.formParam("firstName");
        String lastName = ctx.formParam("lastName");

        try {
            UserService.registerUser(email, password, "customer", firstName, lastName, cp);

            ctx.redirect("/login");

        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("register.html");
        }
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }
}