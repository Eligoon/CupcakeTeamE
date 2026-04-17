package dk.controllers;

import dk.entities.User;
import dk.exceptions.DatabaseException;
import dk.persistence.ConnectionPool;
import dk.services.CartService;
import dk.services.OrderService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CartController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/api/cart/{orderId}", ctx -> getCart(ctx, cp));
        app.post("/cart/add", ctx -> addToCart(ctx, cp));
    }

    private static void getCart(Context ctx, ConnectionPool cp) {

        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            ctx.json(CartService.getCart(orderId, cp));
        } catch (DatabaseException e) {
            ctx.status(500).result("Error fetching cart: " + e.getMessage());
        }
    }

    private static void addToCart(Context ctx, ConnectionPool cp) {
        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        int cupcakeId = Integer.parseInt(ctx.formParam("cupcakeId"));
        int quantity = Integer.parseInt(ctx.formParam("quantity"));


        try {
            int orderId = OrderService.createOrder(user.getUserId(), cp);

            OrderService.addCupcakeToOrder(orderId,cupcakeId,quantity, cp);

            ctx.redirect("/cart/" + orderId);

        } catch (Exception e) {
            ctx.result("Error loading cart: " + e.getMessage());
        }
    }
}