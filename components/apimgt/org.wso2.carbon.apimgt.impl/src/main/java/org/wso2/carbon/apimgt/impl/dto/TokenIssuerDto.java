/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.apimgt.impl.dto;

import java.util.HashMap;
import java.util.Map;

public class TokenIssuerDto {
    private String issuer;
    private boolean disableDefaultClaimMapping = false;
    private Map<String,ClaimMappingDto> claimConfigurations = new HashMap<>();
    private JWKSConfigurationDTO jwksConfigurationDTO = new JWKSConfigurationDTO();

    public TokenIssuerDto(String issuer) {

        this.issuer = issuer;
    }

    public String getIssuer() {

        return issuer;
    }

    public void setIssuer(String issuer) {

        this.issuer = issuer;
    }

    public Map<String,ClaimMappingDto> getClaimConfigurations() {

        return claimConfigurations;
    }
    public void addClaimMapping(ClaimMappingDto claimMappingDto) {
        claimConfigurations.put(claimMappingDto.getRemoteClaim(),claimMappingDto);
    }

    public JWKSConfigurationDTO getJwksConfigurationDTO() {

        return jwksConfigurationDTO;
    }

    public boolean isDisableDefaultClaimMapping() {

        return disableDefaultClaimMapping;
    }

    public void setDisableDefaultClaimMapping(boolean disableDefaultClaimMapping) {

        this.disableDefaultClaimMapping = disableDefaultClaimMapping;
    }

    public void setJwksConfigurationDTO(JWKSConfigurationDTO jwksConfigurationDTO) {

        this.jwksConfigurationDTO = jwksConfigurationDTO;
    }

    public void addClaimMappings(ClaimMappingDto[] claimMappingDto) {

        for (ClaimMappingDto mappingDto : claimMappingDto) {
            addClaimMapping(mappingDto);
        }
    }
}
