package com.rhacp.dip_app.ui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class is normally the entry point for a JavaFX application.
 * However, it is not managed by Spring Boot, so you cannot directly
 * use Spring annotations (e.g. @Component, @Service) or expect
 * dependency injection to work here.
 * <p>
 * When calling Application.launch(OverlayFxApp.class, args),
 * the JavaFX runtime (not Spring) creates an instance of this class
 * using reflection. Because JavaFX instantiates the object itself,
 * Spring never has a chance to manage it as a bean.
 * <p>
 * As a result, OverlayFxApp is not a Spring-managed bean, and
 * dependency injection (e.g. @Autowired or constructor injection)
 * will not work. To access Spring beans from this class, you would
 * need to obtain them manually from the Spring ApplicationContext.
 * <p>
 * Since this project is designed to be "Spring-first," the recommended
 * approach is to bootstrap JavaFX indirectly from Spring (e.g. via
 * CommandLineRunner), rather than using Application.launch().
 */
public class OverlayFxApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    }
}
