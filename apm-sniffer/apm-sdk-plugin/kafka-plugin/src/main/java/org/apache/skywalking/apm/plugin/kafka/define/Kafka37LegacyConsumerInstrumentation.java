/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.plugin.kafka.define;

import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import static org.apache.skywalking.apm.agent.core.plugin.match.NameMatch.byName;

/**
 * For Kafka 3.7.x change
 *
 * <pre>
 *  1. The method named pollForFetchs was removed from KafkaConsumer to <code>AsyncKafkaConsumer</code> and <code>LegacyKafkaConsumer</code>
 *  2. Because of the enhance class was changed, so we should create new Instrumentation to intercept the method
 * </pre>
 */
public class Kafka37LegacyConsumerInstrumentation extends KafkaConsumerInstrumentation {

    private static final String ENHANCE_CLASS_37_LEGACY = "org.apache.kafka.clients.consumer.internals.LegacyKafkaConsumer";

    @Override
    protected ClassMatch enhanceClass() {
        return byName(ENHANCE_CLASS_37_LEGACY);
    }
}