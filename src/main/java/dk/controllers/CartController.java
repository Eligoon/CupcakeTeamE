package dk.controllers;

import dk.exceptions.DatabaseException;
import dk.persistence.ConnectionPool;
import dk.services.CartService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CartController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        // OPTIONAL: only keep if you truly need API later
        // otherwise REMOVE THIS CLASS COMPLETELY
        app.get("/api/cart/{orderId}", ctx -> getCart(ctx, cp));
    }

    private static void getCart(Context ctx, ConnectionPool cp) {

        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            ctx.json(CartService.getCart(orderId, cp));

        } catch (DatabaseException e) {
            ctx.status(500).json(
                    java.util.Map.of("error", e.getMessage())
            );
        }
    }
}