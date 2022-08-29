/*
 * Copyright 2018 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infai.ses.senergy.operators.acwindowmonitor;

import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.FlexInput;
import org.infai.ses.senergy.operators.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AcWindowMonitor extends BaseOperator {

    //final static String windowOpen = "Door/Window Open";
    final static String windowClosed = "Door/Window Closed";

    private final Map<String, Boolean> map;

    public AcWindowMonitor(){
        map = new HashMap<>();
    }

    @Override
    public void run(Message message) {
        FlexInput windowInput = message.getFlexInput("window");
        FlexInput acModeInput  = message.getFlexInput("acmode");
        FlexInput acPowerStateInput  = message.getFlexInput("acpowerstate");
        Boolean acPowerState;
        try {
            acPowerState = acPowerStateInput.getValue(Boolean.class);
        } catch (NoValueException e) {
            e.printStackTrace();
            return;
        }
        if (!acPowerState) {
            message.output("ok", true);
            return;
        }

        String acMode;
        try {
            acMode = acModeInput.getString();
        } catch (NoValueException e) {
            e.printStackTrace();
            return;
        }

        Set<Map.Entry<String, Object>> entries = windowInput.getFilterIdValueMap(Object.class).entrySet();
        for (Map.Entry<String, Object> entr: entries) {
            Object val = entr.getValue();
            boolean b;
            if (val == null) {
                b = false;
            } else if (val instanceof Boolean) {
                b = (Boolean) val;
            } else  if (val instanceof String){
               b = val.equals(windowClosed);
            } else if (val instanceof Integer){
                b = val.equals(23);
            } else {
                System.err.println("Cannot evaluate window state, skipping message. State was " + val + " with class " + val.getClass().getName());
                return;
            }
            map.put(entr.getKey(), b);
        }

        boolean allWindowsClosed = !map.containsValue(Boolean.FALSE);
        if (allWindowsClosed) {
            message.output("ok", true);
            return;
        }
        if (acMode.equalsIgnoreCase("COOL") || acMode.equalsIgnoreCase("HEAT")|| acMode.equalsIgnoreCase("AUTO") || acMode.equalsIgnoreCase("dry")) {
            message.output("ok", false);
            return;
        }
        message.output("ok", true);
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("window");
        message.addFlexInput("acmode");
        message.addFlexInput("acpowerstate");
        return message;
    }
}
