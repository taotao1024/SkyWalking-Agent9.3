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

package org.apache.skywalking.apm.plugin.jackson;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;
import org.apache.skywalking.apm.plugin.jackson.comm.Constants;

import java.io.File;
import java.lang.reflect.Method;

public class ReadValueInterceptor implements InstanceMethodsAroundInterceptor {

    public static final String OPERATION_NAME_JACKSON = "Jackson/";

    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                             Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {

        AbstractSpan span = ContextManager.createLocalSpan(OPERATION_NAME_JACKSON +
                method.getDeclaringClass().getSimpleName() + "." + method.getName());
        span.setComponent(ComponentsDefine.JACKSON);

        if (allArguments[0] instanceof String) {
            span.tag(Constants.SPAN_TAG_KEY_LENGTH, Integer.toString(((String) allArguments[0]).length()));
        } else if (allArguments[0] instanceof byte[]) {
            span.tag(Constants.SPAN_TAG_KEY_LENGTH, Integer.toString(((byte[]) allArguments[0]).length));
        } else if (allArguments[0] instanceof File) {
            span.tag(Constants.SPAN_TAG_KEY_LENGTH, Long.toString(((File) allArguments[0]).length()));
        }
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
                              Class<?>[] argumentsTypes, Object ret) throws Throwable {
        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
                                      Class<?>[] argumentsTypes, Throwable t) {
        ContextManager.activeSpan().log(t);
    }
}