/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.wso2.carbon.apimgt.rest.api.admin.v1.utils.mappings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIDefinition;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Scope;
import org.wso2.carbon.apimgt.impl.definitions.OASParserUtil;
import org.wso2.carbon.apimgt.impl.dto.KeyManagerConfigurationsDto;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.rest.api.admin.v1.dto.KeyManagerConfigurationDTO;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.rest.api.admin.v1.dto.SettingsDTO;
import org.wso2.carbon.apimgt.rest.api.admin.v1.dto.SettingsKeyManagerConfigurationDTO;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsMappingUtil {

    private static final Log log = LogFactory.getLog(SettingsMappingUtil.class);

    /**
     * This method feeds data into the settingsDTO
     *
     * @param isUserAvailable check if user is logged in
     * @return SettingsDTO
     * @throws APIManagementException
     */
    public SettingsDTO fromSettingstoDTO(Boolean isUserAvailable) throws APIManagementException {
        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.setScopes(GetScopeList());
        settingsDTO.setAnalyticsEnabled(APIUtil.isAnalyticsEnabled());
        settingsDTO.setKeyManagerConfiguration(getSettingsKeyManagerConfigurationDTOList());
        return settingsDTO;
    }

    private List<SettingsKeyManagerConfigurationDTO> getSettingsKeyManagerConfigurationDTOList() {
        List<SettingsKeyManagerConfigurationDTO> list = new ArrayList<>();
        Map<String, KeyManagerConfigurationsDto.KeyManagerConfigurationDto> keyManagerConfigurations =
                APIUtil.getKeyManagerConfigurations();
        keyManagerConfigurations.forEach((keyManagerName, keyManagerConfiguration) -> {
            list.add(fromKeyManagerConfigurationToSettingsKeyManagerConfigurationDTO(keyManagerName,
                    keyManagerConfiguration.getConnectionConfigurationDtoList()));

        });
        return list;
    }

    private List<String> GetScopeList() throws APIManagementException {
        String definition = null;
        try {
            definition = IOUtils
                    .toString(RestApiUtil.class.getResourceAsStream("/admin-api.yaml"), "UTF-8");
        } catch (IOException e) {
            log.error("Error while reading the swagger definition", e);
        }
        APIDefinition oasParser = OASParserUtil.getOASParser(definition);
        Set<Scope> scopeSet = oasParser.getScopes(definition);
        List<String> scopeList = new ArrayList<>();
        for (Scope entry : scopeSet) {
            scopeList.add(entry.getKey());
        }
        return scopeList;
    }

    private static SettingsKeyManagerConfigurationDTO fromKeyManagerConfigurationToSettingsKeyManagerConfigurationDTO(
            String keyManagerName,
            List<KeyManagerConfigurationsDto.ConfigurationDto> connectionConfigurationDtoList) {

        SettingsKeyManagerConfigurationDTO settingsKeyManagerConfigurationDTO =
                new SettingsKeyManagerConfigurationDTO();
        settingsKeyManagerConfigurationDTO.setType(keyManagerName);
        if (connectionConfigurationDtoList != null) {
            for (KeyManagerConfigurationsDto.ConfigurationDto configurationDto : connectionConfigurationDtoList) {
                KeyManagerConfigurationDTO keyManagerConfigurationDTO = new KeyManagerConfigurationDTO();
                keyManagerConfigurationDTO.setName(configurationDto.getName());
                keyManagerConfigurationDTO.setLabel(configurationDto.getLabel());
                keyManagerConfigurationDTO.setType(configurationDto.getType());
                keyManagerConfigurationDTO.setRequired(configurationDto.isRequired());
                keyManagerConfigurationDTO.setMask(configurationDto.isMask());
                keyManagerConfigurationDTO.setMultiple(configurationDto.isMultiple());
                keyManagerConfigurationDTO.setTooltip(configurationDto.getTooltip());
                keyManagerConfigurationDTO.setDefault(configurationDto.getDefaultValue());
                keyManagerConfigurationDTO.setValues(configurationDto.getValues());
                settingsKeyManagerConfigurationDTO.getConfigurations().add(keyManagerConfigurationDTO);
            }
        }
        return settingsKeyManagerConfigurationDTO;
    }
}