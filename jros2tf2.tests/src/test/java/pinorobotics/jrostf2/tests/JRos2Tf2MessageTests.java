/*
 * Copyright 2022 jrostf2 project
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
package pinorobotics.jrostf2.tests;

import id.jros2messages.Ros2MessageSerializationUtils;
import id.jros2messages.geometry_msgs.TransformStampedMessage;
import id.jros2messages.std_msgs.HeaderMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.TransformMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.tests.MessageTests;
import java.util.stream.Stream;
import pinorobotics.jros2tf2.tf2_msgs.TFMessage;

/**
 * @author aeon_flux aeon_flux@eclipso.ch
 */
public class JRos2Tf2MessageTests extends MessageTests {

    public JRos2Tf2MessageTests() {
        super(new Ros2MessageSerializationUtils());
    }

    static Stream<TestCase> dataProvider() {
        return Stream.of(
                /*
                 *
                 ros2 topic pub -r 10 helloRos "tf2_msgs/TFMessage" '
                 transforms:
                   - header:
                       stamp: {sec: 123}
                       frame_id: "aaa"
                     child_frame_id: "joint1"
                     transform:
                       translation: {x: 1, y: 2, z: 3}
                       rotation: {x: 4,y: 5,z: 6,w: 7}
                 '
                *
                */
                new TestCase(
                        "TFMessage",
                        new TFMessage()
                                .withTransforms(
                                        new TransformStampedMessage()
                                                .withHeader(
                                                        new HeaderMessage()
                                                                .withStamp(new Time(123, 0))
                                                                .withFrameId("aaa"))
                                                .withChildFrameId(new StringMessage("joint1"))
                                                .withTransform(
                                                        new TransformMessage()
                                                                .withTranslation(
                                                                        new Vector3Message(1, 2, 3))
                                                                .withRotation(
                                                                        new QuaternionMessage(
                                                                                4, 5, 6, 7))))));
    }
}
