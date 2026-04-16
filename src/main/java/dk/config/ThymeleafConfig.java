package dk.config;

import io.javalin.rendering.FileRenderer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;

public class ThymeleafConfig {

    public static TemplateEngine templateEngine() {

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);

        return engine;
    }

    public static FileRenderer renderer(TemplateEngine engine) {

        return (filePath, model, ctx) -> {

            Context thymeleafContext = new Context();

            thymeleafContext.setVariables(new java.util.HashMap<>(model));

            return engine.process(filePath, thymeleafContext);
        };
    }
}