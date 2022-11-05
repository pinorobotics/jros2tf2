/*
 * Copyright 2021 jrostf2 project
 * 
 * Website: https://github.com/pinorobotics/jros2tf2
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
package pinorobotics.jros2tf2.tf2_msgs;

import pinorobotics.jros2actionlib.actionlib_msgs.Action2Definition;
import pinorobotics.jros2actionlib.actionlib_msgs.Action2GetResultRequestMessage;
import pinorobotics.jros2actionlib.actionlib_msgs.Action2GoalMessage;
import pinorobotics.jrosactionlib.msgs.ActionResultMessage;
import pinorobotics.jrostf2.tf2_msgs.LookupTransformGoalMessage;

/**
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class LookupTransformActionDefinition
        implements Action2Definition<LookupTransformGoalMessage, LookupTransformResultMessage> {

    @Override
    public Class<? extends Action2GoalMessage<LookupTransformGoalMessage>> getActionGoalMessage() {
        return LookupTransformActionGoalMessage.class;
    }

    @Override
    public Class<? extends ActionResultMessage<LookupTransformResultMessage>>
            getActionResultMessage() {
        return LookupTransformActionResultMessage.class;
    }

    @Override
    public Class<? extends Action2GetResultRequestMessage> getActionResultRequestMessage() {
        return LookupTransformActionGetResultRequestMessage.class;
    }
}
