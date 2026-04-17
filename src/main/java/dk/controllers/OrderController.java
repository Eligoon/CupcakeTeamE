package dk.controllers;

import dk.entities.CupcakeOrders;
import dk.entities.Order;
import dk.entities.User;
import dk.exceptions.DatabaseException;
import dk.persistence.ConnectionPool;
import dk.persistence.CupcakeOrdersMapper;
import dk.persistence.OrderMapper;
import dk.services.CartService;
import dk.services.OrderService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {

    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/orders", ctx -> showOrders(ctx, cp));

        app.get("/cart/{orderId}", ctx -> showCart(ctx, cp));

        app.post("/cart/pay", ctx -> payOrder(ctx, cp));

        app.post("/orders/create", ctx -> createOrder(ctx, cp));
    }

    // US-6 (admin/user orders list placeholder)
    private static void showOrders(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");

        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        try {
            List<Order> orders;

            if ("admin".equals(user.getRole())) {
                orders = OrderMapper.getAllOrders(cp);
            } else {
                orders = OrderMapper.getOrdersByUser(user.getUserId(), cp);
            }

            Map<Integer, Double> totals = new HashMap<>();

            for (Order order : orders) {
                double total = OrderMapper.calculateTotal(order.getOrderId(), cp);
                totals.put(order.getOrderId(), total);
            }


            ctx.attribute("orders", orders);
            ctx.attribute("totals", totals);

            if ("admin".equals(user.getRole())) {
                ctx.render("admin/orders.html");
            } else {
                ctx.render("orders.html");
            }

        } catch (Exception e) {
            ctx.result("Error loading orders: " + e.getMessage());
        }

    }

    // US-4
    private static void showCart(Context ctx, ConnectionPool cp) {

        int orderId = Integer.parseInt(ctx.pathParam("orderId"));

        try {
            ctx.attribute("cartItems", CartService.getCart(orderId, cp));
            ctx.attribute("total", CartService.calculateTotal(orderId, cp));

            ctx.render("checkout.html");

        } catch (Exception e) {
            ctx.status(500).result("Error loading cart: " + e.getMessage());
        }
    }

    // US-1 payment
    private static void payOrder(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");

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

    // create order (US-1)
    private static void createOrder(Context ctx, ConnectionPool cp) {

        User user = ctx.sessionAttribute("currentUser");

        try {
            int orderId = OrderService.createOrder(user.getUserId(), cp);

            ctx.redirect("/cart/" + orderId);

        } catch (Exception e) {
            ctx.result(e.getMessage());
        }
    }
}