package com.empty.ecosim.model.configuration.userSetup;

import com.empty.ecosim.model.configuration.ConfigurationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class UserSetupManager {

    public static final UserSetupManager INSTANCE = new UserSetupManager();
    private static UserSetupSpecification userSetup;

    private UserSetupManager() {
        loadUserSetupSpecification();
    }

    private void loadUserSetupSpecification() {
        File userSetupFile = ConfigurationManager.INSTANCE.getResourceFileMapping().get(ConfigurationManager.ResourceType.USER_SETUP).toFile();
        ObjectMapper objectMapper = new YAMLMapper();

        try {
            userSetup = objectMapper.readValue(userSetupFile, UserSetupSpecification.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

   public UserSetupSpecification get() {
        return userSetup;
   }

   public void reload() {
       loadUserSetupSpecification();
   }
}
