/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ispyb.ws.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 resources, and the {@link javax.ws.rs.ApplicationPath} defines the root path shared by all these resources.
 */
@ApplicationPath("rest")
public class RestApplication extends Application {
	
}
