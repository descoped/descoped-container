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

import javax.enterprise.inject.Vetoed;

/**
 * An event that is fired before the core is stopped.  Components may listen to this event to start up on boot.
 */
@Vetoed
public class PreStopContainer {

    private ServerContainer container;

    public PreStopContainer() {
        throw new UnsupportedOperationException();
    }

    public PreStopContainer(ServerContainer container) {
        this.container = container;
    }

    public ServerContainer container() {
        return container;
    }

    public void setContainer(ServerContainer container) {
        this.container = container;
    }

}
