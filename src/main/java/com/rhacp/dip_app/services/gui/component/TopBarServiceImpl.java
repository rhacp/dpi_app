package com.rhacp.dip_app.services.gui.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TopBarServiceImpl implements TopBarService {

    @Override
    public HBox createTopBar(Stage stage) {
        // Top bar
        HBox topBar = new HBox();

        // Title
        Label topBarLabel = new Label("Configuration");
        topBarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        topBarLabel.setTextFill(Color.web("#1E90FF"));

        // Spacer
        Region topBarSpacer = new Region();
        HBox.setHgrow(topBarSpacer, Priority.ALWAYS);

        // Close icon
        Label closeLabel = new Label("\u2716"); // ✖
        closeLabel.setTextFill(Color.web("#A9B7C6"));
        closeLabel.setStyle("-fx-background-color: transparent; -fx-font-size: 20; fx-font-weight: bold; ");
        closeLabel.setOnMouseClicked(e -> stage.close());
        closeLabel.setMinWidth(30);

        closeLabel.setOnMouseEntered(e -> {
            closeLabel.setTextFill(Color.web("#1E90FF"));
        });

        closeLabel.setOnMouseExited(e -> {
            closeLabel.setTextFill(Color.web("#A9B7C6"));
        });

        topBar.getChildren().addAll(topBarLabel,topBarSpacer, closeLabel);

        return topBar;
    }

    @Override
    public Region createSeparator() {
        Region separator = new Region();
        separator.setPrefHeight(2);
        separator.setStyle("-fx-background-color: #A9B7C6;");
        VBox.setMargin(separator, new Insets(10, 0, 30, 0));

        return separator;
    }
}
