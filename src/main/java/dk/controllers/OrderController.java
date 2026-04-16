package dk.controllers;

import dk.entities.User;
import dk.persistence.ConnectionPool;
import dk.services.CartService;
import dk.services.OrderService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/orders", ctx -> showOrders(ctx));
        app.get("/cart/{orderId}", ctx -> showCart(ctx, cp));

        app.post("/orders/create", ctx -> createOrder(ctx, cp));
        app.post("/cart/pay", ctx -> payOrder(ctx, cp));
    }

    //  ORDERS PAGE
    private static void showOrders(Context ctx) {

        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        ctx.render("orders.html");
    }

    //  CART PAGE
    private static void showCart(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            ctx.attribute("cartItems", CartService.getCart(orderId, cp));
            ctx.attribute("total", CartService.calculateTotal(orderId, cp));
            ctx.attribute("orderId", orderId);

            ctx.render("checkout.html");

        } catch (Exception e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    //  CREATE ORDER
    private static void createOrder(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        try {
            int orderId = OrderService.createOrder(user.getUserId(), cp);
            ctx.redirect("/cart/" + orderId);

        } catch (Exception e) {
            ctx.result(e.getMessage());
        }
    }

    //  PAY ORDER
    private static void payOrder(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        double total = Double.parseDouble(ctx.formParam("total"));

        try {
            OrderService.payOrder(orderId, user.getUserId(), total, cp);
            ctx.redirect("/orders");

        } catch (Exception e) {
            ctx.attribute("message", e.getMessage());
            ctx.redirect("/cart/" + orderId);
        }
    }
}