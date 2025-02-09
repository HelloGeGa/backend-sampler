/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.sample.quarkus.coffee.module.mp.restclient.exception;

import javax.enterprise.inject.Vetoed;

import org.apache.commons.lang3.EnumUtils;

import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.dto.exception.enums.FaultType;

/**
 * Default message codes translator
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Vetoed
public class FaultTypeParser {

    /**
     * Parsing String enuam values into enum type
     * 
     * @param faultTypeString
     *            enum value string
     * @return enum for input string
     */
    public static Enum<?> parseFaultType(String faultTypeString) {
        Enum<?> fault = EnumUtils.getEnum(CoffeeFaultType.class, faultTypeString);
        if (fault != null) {
            return fault;
        }
        fault = EnumUtils.getEnum(FaultType.class, faultTypeString);
        if (fault != null) {
            return fault;
        }
        Logger.getLogger(FaultTypeParser.class).warn("FaultType not exists in enum for messages, faultType: [" + faultTypeString + "]");
        return CoffeeFaultType.OPERATION_FAILED;
    }
}
