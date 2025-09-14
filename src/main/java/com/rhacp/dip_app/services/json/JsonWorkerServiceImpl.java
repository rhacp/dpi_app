package com.rhacp.dip_app.services.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhacp.dip_app.models.transfer.card.Card;
import com.rhacp.dip_app.models.transfer.card.DpiTable;
import com.rhacp.dip_app.models.transfer.profile.Assignment;
import com.rhacp.dip_app.models.transfer.profile.Profile;
import com.rhacp.dip_app.repositories.logitech_repository.DB;
import com.rhacp.dip_app.utils.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class JsonWorkerServiceImpl implements JsonWorkerService {

    private final AppProperties appProperties;

    private final DB dbRepository;

    private final ObjectMapper objectMapper;

    public JsonWorkerServiceImpl(AppProperties appProperties, DB dbRepository, ObjectMapper objectMapper) {
        this.appProperties = appProperties;
        this.dbRepository = dbRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Integer getCurrentDpi() {
        String json = dbRepository.getJsonString();

        Profile profile = getProfileFromJson(json, objectMapper);
        DpiTable dpiTable = getDpiTableForProfile(json, profile, objectMapper);

        log.info("Current DPI retrieved. Method: getCurrentDpi");
        return dpiTable.getActiveDpi();
    }

    @Override
    public List<Integer> getCurrentDpiList() {
        String json = dbRepository.getJsonString();
        ObjectMapper mapper = new ObjectMapper();

        Profile profile = getProfileFromJson(json, mapper);
        DpiTable dpiTable = getDpiTableForProfile(json, profile, mapper);

        log.info("DPI list retrieved. Method: getCurrentDpiList");
        return dpiTable.getLevels();
    }

    @Override
    public String getJsonSettings() {
        return dbRepository.getJsonString();
    }

    @Override
    public void exportJsonSettings(String json) {
        dbRepository.setJsonString(json);
    }

    @Override
    public String readJsonFromFile(File file) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            log.info("Settings imported to db. Method: importSettingsButton");
        } catch (IOException e) {
            RuntimeException exception = new RuntimeException("Json processing failed. Method: importSettingsButton");
            log.error(exception.getMessage());
            throw exception;
        }

        return json.toString();
    }

    private Profile getProfileFromJson(String json, ObjectMapper mapper) {
        // Get Profiles
        Profile profileObj = null;
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode profiles = root.get("profiles");
            JsonNode profilesSecond = profiles.get("profiles");

            // Get Requested Profile
            if (profilesSecond.isArray()) {
                for (JsonNode profile : profilesSecond) {
                    String name = profile.get("name").asText();

                    if (name.equals(appProperties.getProfileName())) {
                        profileObj = mapper.treeToValue(profile, Profile.class);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            RuntimeException exception = new RuntimeException("Json processing error. Method: getProfileFromJson");
            log.error(exception.getMessage());
            throw exception;
        }

        log.info("Profile retrieved. Method: getProfileFromJson");
        return profileObj;
    }

    private DpiTable getDpiTableForProfile(String json, Profile profileObj, ObjectMapper mapper) {
        Card cardObj = null;
        JsonNode root = null;

        // Get cardId identifier
        String cardIdNeeded = profileObj.getAssignments().stream()
                .filter(a -> a.getSlotId().equals(appProperties.getSlotId()))
                .map(Assignment::getCardId)
                .findFirst()
                .orElse(null);

        // Get DPI Object
        try {
            root = mapper.readTree(json);
            JsonNode cardsOne = root.get("cards");
            JsonNode cardsTwo = cardsOne.get("cards");

            if (cardsTwo.isArray()) {
                for (JsonNode card : cardsTwo) {
                    String cardIdSource = card.get("id").asText();
                    if (cardIdNeeded.equals(cardIdSource)) {
                        cardObj = mapper.treeToValue(card, Card.class);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            RuntimeException exception = new RuntimeException("Json processing error. Method: getDpiTableForProfile");
            log.error(exception.getMessage());
            throw exception;
        }

        log.info("DPI table retrieved. Method: getDpiTableForProfile");

        return cardObj.getMouseSettings().getDpiTable();
    }
}
