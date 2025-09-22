package com.rhacp.dip_app.services.gui.scene;

import com.rhacp.dip_app.models.DPI;
import com.rhacp.dip_app.services.dpi.DPIService;
import com.rhacp.dip_app.services.user_config.UserConfigService;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class OverlayServiceImpl implements OverlayService {

    private final UserConfigService userConfigService;

    private final DPIService dpiService;

    private Stage stage = null;

    private final AtomicInteger current = new AtomicInteger(0);

    private final List<Label> labelList = new ArrayList<>();

    public OverlayServiceImpl(UserConfigService userConfigService, DPIService dpiService) {
        this.userConfigService = userConfigService;
        this.dpiService = dpiService;
    }

    @Override
    public void createOverlay() {
        // check with userConfigService.checkExistingConfig(); if false, ask the user to change it

        if (!userConfigService.checkUserConfig()) {
            return;
        }

        DPI dpi = dpiService.getDPIObject();
//        try {
//            dpi = dpiService.getDPIObject();
//        } catch (RuntimeException e) {
//            log.error("Could not create overlay scene. Method: createOverlay", e);
//            // show notification
//            return;
//        }

        // Top-level container. Dummy, you cannot see and not showing in windows bar.
        stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.setOpacity(0);
        stage.setWidth(0);
        stage.setHeight(0);
        stage.show();

        // Stage transparent, on top, not resizable and stop the close event
        Stage overlay = new Stage();
        overlay.initOwner(stage);
        overlay.initStyle(StageStyle.TRANSPARENT);
        overlay.setAlwaysOnTop(true);
        overlay.setResizable(false);

        // Root container
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.TOP_RIGHT);
        hBox.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-padding: 10; -fx-background-radius: 10;");
        hBox.setMouseTransparent(true); //click-through

        // Current DPI and DPI levels
        createDpiList(hBox);
        current.set(dpi.getProfileDPI().indexOf(dpi.getCurrentDPI()));
        labelList.get(current.get()).setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1E90FF");

        // Scene -> Scene (overlay) -> Stage -> HBox
        Scene scene = new Scene(hBox);
        overlay.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        showOverlay(overlay);
    }

//    @Scheduled(fixedRate = 60000, initialDelay = 10000)
    @Override
    public void updateOverlay() {
        //check if current user config is vaild. if not, ask user to update it
        // Get DPI Object
        DPI receivedDpi = dpiService.getDPIObject();
//        try {
//            receivedDpi = dpiService.getDPIObject();
//        } catch (RuntimeException e) {
//            log.error("Could not update overlay. Method: updateOverlay", e);
//            // show notification
//            return;
//        }

        int receivedCurrentIndex = receivedDpi.getProfileDPI().indexOf(receivedDpi.getCurrentDPI());
        List<Integer> currentDpiList = labelList.stream()
                .map(element -> Integer.valueOf(element.getText()))
                .toList();

        updateCurrentDpi(receivedCurrentIndex);
        updateDpiList(currentDpiList, receivedDpi.getProfileDPI());
        log.info("Overlay updated. Method: updateOverlay");
    }

    @Override
    public void buttonUpPressed() {
        Platform.runLater(() -> {
            if (current.get() != 4) {
                labelList.get(current.get()).setStyle("-fx-font-size: 20; -fx-text-fill: white");
                labelList.get(current.get() + 1).setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1E90FF");
                current.addAndGet(1);
            }
        });

        log.info("Dpi up. Method: buttonUpPressed");
    }

    @Override
    public void buttonDownPressed() {
        Platform.runLater(() -> {
            if (current.get() != 0) {
                labelList.get(current.get()).setStyle("-fx-font-size: 20; -fx-text-fill: white");
                labelList.get(current.get() - 1).setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1E90FF");
                current.addAndGet(-1);
            }
        });

        log.info("Dpi down. Method: buttonDownPressed");
    }

    @Override
    public void deleteOverlay() {

    }

    @Override
    public void showOverlay(Stage overlay) {
        overlay.show();

        // Place stage on screen at coordinates
        Platform.runLater(() -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            overlay.setX(screenBounds.getMaxX() - overlay.getWidth() - 10);
            overlay.setY(10);
        });
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    private void updateCurrentDpi(Integer receivedCurrentIndex) {
        if (current.get() != receivedCurrentIndex) {
            Platform.runLater(() -> {
                labelList.get(current.get()).setStyle("-fx-font-size: 20; -fx-text-fill: white");
                current.set(receivedCurrentIndex);
                labelList.get(current.get()).setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1E90FF");
            });
            log.info("Update current DPI to : {}. Method: updateCurrentDpi", labelList.get(receivedCurrentIndex).getText());
        }
    }

    private void updateDpiList(List<Integer> currentDpiList, List<Integer> receivedDpiList) {
        if (!currentDpiList.equals(receivedDpiList)){
            Platform.runLater(() -> {
                for (int i = 0; i < 5; i++) {
                    if (!Integer.valueOf(labelList.get(i).getText()).equals(receivedDpiList.get(i))) {
                        labelList.get(i).setText(receivedDpiList.get(i).toString());
                    }
                }
            });
            log.info("Update current DPI list. Method: updateDpiList");
        }
    }

    private void createDpiList(HBox hBox) {
        for (int i = 0; i < 5; i++) {
            Label label = new Label(String.valueOf(dpiService.getDPIObject().getProfileDPI().get(i)));
            label.setStyle("-fx-font-size: 20; -fx-text-fill: white");
            label.setMouseTransparent(true);
            labelList.add(label);
            hBox.getChildren().add(label);
        }
    }
}