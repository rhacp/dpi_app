package com.rhacp.dip_app.services.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhacp.dip_app.exceptions.JsonFileReadException;
import com.rhacp.dip_app.models.transfer.card.Card;
import com.rhacp.dip_app.models.transfer.card.DpiTable;
import com.rhacp.dip_app.models.transfer.profile.Assignment;
import com.rhacp.dip_app.models.transfer.profile.Profile;
import com.rhacp.dip_app.repositories.logitech_repository.DB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class JsonWorkerServiceImpl implements JsonWorkerService {

    private final DB dbRepository;

    private final ObjectMapper objectMapper;

    public JsonWorkerServiceImpl(DB dbRepository, ObjectMapper objectMapper) {
        this.dbRepository = dbRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Integer getCurrentDpi(String profileName, String slotId) {
        String json = dbRepository.getJsonString();

        Profile profile = getProfileFromJson(json, objectMapper, profileName);
        DpiTable dpiTable = getDpiTableForProfile(json, profile, objectMapper, slotId);

        if (dpiTable == null) {
            return -1;
        }

        log.info("Current DPI retrieved. Method: getCurrentDpi");
        return dpiTable.getActiveDpi();
    }

    @Override
    public List<Integer> getCurrentDpiList(String profileName, String slotId) {
        String json = dbRepository.getJsonString();
        ObjectMapper mapper = new ObjectMapper();

        Profile profile = getProfileFromJson(json, mapper, profileName);
        DpiTable dpiTable = getDpiTableForProfile(json, profile, mapper, slotId);

        if (dpiTable == null) {
            return List.of(-1, -1, -1, -1, -1);
        }

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
            // handle this edge case
            throw new JsonFileReadException("Json processing failed. Method: importSettingsButton", e);
        }

        return json.toString();
    }

    @Override
    public HashMap<String, Boolean> checkJsonPropertiesUserConfig(String profileName, String slotId) {
        String json = dbRepository.getJsonString();
        HashMap<String, Boolean> map = new HashMap<>();

        Profile profile = getProfileFromJson(json, objectMapper, profileName);
        if (profile == null) {
            map.put("profileName", false);
            return map;
        }

        DpiTable dpiTable = getDpiTableForProfile(json, profile, objectMapper, slotId);
        if (dpiTable == null) {
            map.put("slotId", false);
        }

        return map;
    }

    private Profile getProfileFromJson(String json, ObjectMapper mapper, String profileName) {
        // Get Profiles
        Profile profileObj = null;
//        String nameToSearch = userConfigService.getUserConfig().getProfileName().equals(appProperties.getProfileName())
//                ? appProperties.getProfileName()
//                : userConfigService.getUserConfig().getProfileName();

        try {
            JsonNode root = mapper.readTree(json);
            JsonNode profiles = root.get("profiles");
            JsonNode profilesSecond = profiles.get("profiles");

            // Get Requested Profile
            if (profilesSecond.isArray()) {
                for (JsonNode profile : profilesSecond) {
                    String name = profile.get("name").asText();

                    if (name.equals(profileName)) {
                        profileObj = mapper.treeToValue(profile, Profile.class);
                        break;
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.error("Json processing error. Method: getProfileFromJson", e);
            return null;
        }

//        if (profileObj == null) {
//            // Notification for profile not found.
//            throw new RuntimeException("Profile not found. Method: getProfileFromJson");
//        }

        log.info("Profile retrieved. Method: getProfileFromJson");
        return profileObj;
    }

    private DpiTable getDpiTableForProfile(String json, Profile profileObj, ObjectMapper mapper, String slotId) {
        Card cardObj = null;
        JsonNode root;
//        String slotIdToSearch = userConfigService.getUserConfig().getSlotId().equals(appProperties.getSlotId())
//                ? appProperties.getSlotId()
//                : userConfigService.getUserConfig().getSlotId();

        // Get cardId identifier
        String cardIdNeeded = profileObj.getAssignments().stream()
                .filter(a -> a.getSlotId().equals(slotId))
                .map(Assignment::getCardId)
                .findFirst()
                .orElse(null);

        if (cardIdNeeded == null) {
            return null;
        }

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
            log.error("Json processing error. Method: getDpiTableForProfile", e);
            return null;
        }

//        if (cardObj == null) {
//            return null;
//        }

        log.info("DPI table retrieved. Method: getDpiTableForProfile");
        return cardObj.getMouseSettings().getDpiTable();
    }
}
