package dk.controllers;

import dk.entities.User;
import dk.persistence.BottomMapper;
import dk.persistence.ConnectionPool;
import dk.persistence.CupcakeMapper;
import dk.persistence.ToppingMapper;
import dk.services.AuthService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class HomeController {


    public static void addRoutes(Javalin app, ConnectionPool cp) {

        app.get("/", ctx -> {
            try {
                ctx.attribute("bottoms", BottomMapper.getAllBottoms(cp));
                ctx.attribute("toppings", ToppingMapper.getAllToppings(cp));
                ctx.attribute("cupcakes", CupcakeMapper.getAllCupcakes(cp));

                ctx.render("index.html");
            } catch (Exception e) {
                ctx.result("Error loading homepage: " + e.getMessage());
            }
        });
    }
}
