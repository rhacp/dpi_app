package com.rhacp.dip_app.services.gui.scene;

import com.rhacp.dip_app.services.json.JsonWorkerService;
import com.rhacp.dip_app.utils.Constants;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class ContextMenuServiceImpl implements ContextMenuService {

    private Stage stage;

    private final JsonWorkerService jsonWorkerService;

    private final ConfigService configService;

    public ContextMenuServiceImpl(JsonWorkerService jsonWorkerService, ConfigService configService) {
        this.jsonWorkerService = jsonWorkerService;
        this.configService = configService;
    }

    @Override
    public void createContextMenu() {
        // The top-level container for everything
        stage = new Stage();

        // Set up stage
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        // Root container
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 10; -fx-spacing: 5; -fx-background-radius: 5;");

        // Add menu buttons
        vBox.getChildren().add(createMenuButton("Config", Color.WHITE));
        vBox.getChildren().add(createMenuButton("Import Settings", Color.WHITE));
        vBox.getChildren().add(createMenuButton("Export Settings", Color.WHITE));
        vBox.getChildren().add(createMenuButton("Exit", Color.WHITE));

        //Set up scene
        Scene scene = new Scene(vBox);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                stage.hide();
                log.info("Tray menu hidden due to focus loss.");
            }
        });
    }

    @Override
    public void showAtCoordinates(double x, double y) {
        stage.setX(x);
        stage.setY(y);
        stage.show();
        log.info("Tray menu show. Method: showAtCoordinates");
    }

    @Override
    public void hide() {
        stage.hide();
        log.info("Tray menu hide. Method: hide");
    }

    @Override
    public double getTotalHeight() {
        stage.getScene().getRoot().applyCss();
        stage.getScene().getRoot().layout();
        return stage.getScene().getRoot().prefHeight(-1);
    }

    private Button createMenuButton(String text, Color textColor) {
        // Create button
        Button btn = new Button(text);

        // Set up properties
        btn.setStyle("-fx-background-color: transparent; -fx-font-size: 15; -fx-alignment: CENTER_LEFT;");
        btn.setTextFill(textColor);
        btn.setMaxWidth(Double.MAX_VALUE);   // button fills the VBox width
        VBox.setVgrow(btn, Priority.ALWAYS); // optional: distribute vertical space

        // Hover effect
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: rgba(255,255,255,0.8); -fx-font-size: 15; -fx-background-radius: 5; -fx-alignment: CENTER_LEFT;");
            btn.setTextFill(Color.BLACK); // change text color on hover
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-font-size: 15; -fx-alignment: CENTER_LEFT;");
            btn.setTextFill(textColor); // revert text color
        });

        // Set action
        switch (text) {
            case "Config" -> btn.setOnAction(e -> this.configButton());
            case "Import Settings" -> btn.setOnAction(e -> this.importSettingsButton());
            case "Export Settings" -> btn.setOnAction(e -> this.exportSettingsButton());
            case "Exit" -> btn.setOnAction(e -> this.exitButton());
        }

        log.info("Button {} created. Method: createMenuButton", btn.getText());
        return btn;
    }

    private void exitButton() {
        Platform.exit();
        System.exit(0);
        stage.hide();
    }

    private void exportSettingsButton() {
        String json = jsonWorkerService.getJsonSettings();

        // Show FileChooser
        FileChooser fileChooser = createFileChooser("Export Settings", "JSON");
        File file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            log.warn("Export canceled by user. Method: exportSettingsButton");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
            log.info("Settings exported to {}. Method: exportSettingsButton", file.getAbsolutePath());
        } catch (IOException e) {
            RuntimeException exception = new RuntimeException("Conversion to JSON failed. Method: exportSettingsButton");
            log.error(exception.getMessage());
            throw exception;
        }
    }

    private void importSettingsButton() {
        if (checkIfGHubIsRunning()) {
            Alert alert = createAlert("G Hub Is Running",
                    "Import Not Allowed",
                    "Please close Logitech G Hub before importing settings.");

            alert.showAndWait();

            log.warn("Cannot import because G Hub is running. Method: importSettingsButton");
            return;
        }

        // Show FileChooser
        FileChooser fileChooser = createFileChooser("Import Settings", "JSON");
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            log.warn("Import canceled by user. Method: importSettingsButton");
            return;
        }

        String result = jsonWorkerService.readJsonFromFile(file);
        jsonWorkerService.exportJsonSettings(result);
    }

    private void configButton() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        configService.showAtCoordinates(screenBounds.getMaxX()/2 - configService.getTotalWidth()/2,
                screenBounds.getMaxY()/2 - configService.getTotalHeight()/2);
    }

    private boolean checkIfGHubIsRunning() {
        return ProcessHandle.allProcesses()
                .map(ph -> ph.info().command().orElse("").toLowerCase())
                .map(e -> e.substring(e.lastIndexOf("\\") + 1))
                .anyMatch(Constants.GHUB_PROCESSES::contains);
    }

    private Alert createAlert(String title, String header, String content) {
        // Create alert window
        Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);

        // Set up and show alert
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Edit alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        return alert;
    }

    private FileChooser createFileChooser(String title, String extension) {
        // Create and set up FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        // Only files with the extension .json are shown (and can be saved)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.toUpperCase(),
                "*." + extension.toLowerCase()));

        return fileChooser;
    }
}
