package dk.controllers;

import dk.exceptions.DatabaseException;
import dk.persistence.ConnectionPool;
import dk.services.CartService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CartController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/api/cart/{orderId}", ctx -> getCart(ctx, cp));
    }

    private static void getCart(Context ctx, ConnectionPool cp) {

        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            ctx.json(CartService.getCart(orderId, cp));
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching cart: " + e.getMessage());
        }
    }
}