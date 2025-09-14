package com.rhacp.dip_app.services.gui.scene;

import com.rhacp.dip_app.services.gui.component.TopBarService;
import com.rhacp.dip_app.utils.AppProperties;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    private final AppProperties appProperties;

    private final TopBarService topBarService;

    private Stage stage;

    private final List<TextField> textFieldList = new ArrayList<>();

    private final List<Label> labelList = new ArrayList<>();

    public ConfigServiceImpl(AppProperties appProperties, TopBarService topBarService) {
        this.appProperties = appProperties;
        this.topBarService = topBarService;
    }

    @Override
    public void createConfig() {
        // The top-level container for everything
        stage = new Stage();

        // Set up stage
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        // Root container
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #181818; -fx-padding: 20; -fx-spacing: 5; -fx-background-radius: 10;");
        root.setMinHeight(400);
        root.setMinWidth(800);

        // Create top bar
        HBox topBarOverlay = topBarService.createTopBar(stage);
        Region separator = topBarService.createSeparator();
        root.getChildren().add(topBarOverlay);
        root.getChildren().add(separator);

        // Set up config horizontal items
        root.getChildren().add(createConfigHItem("G Hub DB file path (settings.db): ", "Path", appProperties.getJsonPathOne()));
        root.getChildren().add(createConfigHItem("Profile name to be tracked in G Hub: ", "Name", appProperties.getProfileName()));
        root.getChildren().add(createConfigHItem("SlotId (example: g502wireless_mouse_settings): ", "Mouse Model", appProperties.getSlotId()));
        root.getChildren().add(createConfigHItem("Button DPI up: ", "Button Up", appProperties.getDpiUp()));
        root.getChildren().add(createConfigHItem("Button DPI down: ", "Button Down", appProperties.getDpiDown()));

        // Set up buttons
        HBox hButtonBox = new HBox(20);
        hButtonBox.setAlignment(Pos.CENTER);
        hButtonBox.setPadding(new Insets(15, 0, 20, 0 ));

        hButtonBox.getChildren().addAll(createConfigButton("Save"),
                createConfigButton("Cancel"));
        root.getChildren().add(hButtonBox);

        //Set up scene
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
    }

    @Override
    public void showAtCoordinates(double x, double y) {
        stage.setX(x);
        stage.setY(y);
        stage.show();

        Platform.runLater(() -> {
            textFieldList.get(0).deselect();
            textFieldList.get(0).positionCaret(textFieldList.get(0).getText().length());
        });

        log.info("Tray menu show. Method: showAtCoordinates");
    }

    @Override
    public void hideConfig() {
        stage.hide();
        log.info("Configuration panel hide. Method: hideConfig");
    }

    @Override
    public double getTotalHeight() {
        stage.getScene().getRoot().applyCss();
        stage.getScene().getRoot().layout();
        return stage.getScene().getRoot().prefHeight(-1);
    }

    @Override
    public double getTotalWidth() {
        stage.getScene().getRoot().applyCss();
        stage.getScene().getRoot().layout();
        return stage.getScene().getRoot().prefWidth(-1);
    }

    private Label createItemLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#A9B7C6"));
        HBox.setMargin(label, new Insets(0, 20, 0, 0));
        labelList.add(label);

        return label;
    }

    private TextField createItemTextField(String prompt, String initial) {
        TextField textField = new TextField(initial);
        textField.setPromptText(prompt);
        textField.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        textField.setStyle("-fx-background-radius: 7; -fx-background-color: #181818; -fx-text-fill: #A9B7C6; -fx-highlight-fill: #3a5874;");
//        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.setMinWidth(450);
        textFieldList.add(textField);

        return textField;
    }

    private HBox createConfigHItem(String labelText, String prompt, String initial) {
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-spacing: 5; -fx-background-radius: 5;");
        VBox.setMargin(hBox, new Insets(0, 0, 10, 0));

        // Spacer
        Region separator = new Region();
        HBox.setHgrow(separator, Priority.ALWAYS);

        hBox.getChildren().addAll(createItemLabel(labelText),separator, createItemTextField(prompt, initial));

        return hBox;
    }

    private Button createConfigButton(String text) {
        // Create button
        Button btn = new Button(text);

        // Set up properties
        btn.setStyle("-fx-background-color: #333333; -fx-font-size: 15; -fx-font-weight: bold; -fx-background-radius: 15; -fx-alignment: center;");
        btn.setTextFill(Color.web("#A9B7C6"));
        btn.setMinWidth(100);

        // Hover effect
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #A9B7C6; -fx-font-size: 15; -fx-font-weight: bold; -fx-background-radius: 15; -fx-alignment: center;");
            btn.setTextFill(Color.BLACK);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: #333333; -fx-font-size: 15; -fx-font-weight: bold; -fx-background-radius: 15; -fx-alignment: center;");
            btn.setTextFill(Color.web("#A9B7C6"));
        });

        // Set action
        switch (text) {
            case "Save" -> btn.setOnAction(e -> saveButton());
            case "Cancel" -> btn.setOnAction(e -> cancelButton());
        }

        log.info("Button {} created. Method: createConfigButton", btn.getText());
        return btn;
    }

    private void saveButton() {

    }

    private void cancelButton() {
        hideConfig();
    }
}
