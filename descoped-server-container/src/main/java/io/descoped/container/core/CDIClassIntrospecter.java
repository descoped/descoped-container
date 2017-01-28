/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 *     distributed with this work for additional information
 *     regarding copyright ownership.  The ASF licenses this file
 *     to you under the Apache License, Version 2.0 (the
 *     "License"); you may not use this file except in compliance
 *     with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing,
 *     software distributed under the License is distributed on an
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *     KIND, either express or implied.  See the License for the
 *     specific language governing permissions and limitations
 *     under the License.
 */

package io.descoped.container.core;

import io.undertow.servlet.api.ClassIntrospecter;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.DefaultClassIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;

@Vetoed
public class CDIClassIntrospecter implements ClassIntrospecter {

    public static final ClassIntrospecter INSTANCE = new CDIClassIntrospecter();
    private static final Logger log = LoggerFactory.getLogger(CDIClassIntrospecter.class);

    private CDIClassIntrospecter() {
    }

    @Override
    public <T> InstanceFactory<T> createInstanceFactory(Class<T> clazz) throws NoSuchMethodException {
        log.trace("createInstanceFactory: {}", clazz);
        Instance<T> inst = CDI.current().select(clazz);
        if (inst.isUnsatisfied() || inst.isAmbiguous()) {
            return DefaultClassIntrospector.INSTANCE.createInstanceFactory(clazz);
        } else {
            return new CdiInstanceFactory<>(inst);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------

    private static class CdiInstanceFactory<T> implements InstanceFactory<T> {
        private static final Logger log = LoggerFactory.getLogger(CdiInstanceFactory.class);

        private final Instance<T> inst;

        CdiInstanceFactory(Instance<T> inst) {
            this.inst = inst;
        }

        @Override
        public InstanceHandle<T> createInstance() throws InstantiationException {
            log.trace("createInstance: ", inst);
            return new CdiInstanceHandler<>(inst);
        }

    }

    // ---------------------------------------------------------------------------------------------------------------

    private static class CdiInstanceHandler<T> implements InstanceHandle<T> {
        private static final Logger log = LoggerFactory.getLogger(CdiInstanceHandler.class);

        private final Instance<T> inst;
        private T found = null;

        CdiInstanceHandler(Instance<T> inst) {
            this.inst = inst;
        }

        @Override
        public T getInstance() {
            if (this.found == null) {
                this.found = inst.get();
            }
            log.trace("getInstance [ {} ] >> {}", inst, found);
            return this.found;
        }

        @Override
        public void release() {
            inst.destroy(this.found);
        }
    }

}
