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

import ispyb.ws.rest.mx.AutoprocintegrationRestWebService;
import ispyb.ws.rest.mx.CrystalRestWebService;
import ispyb.ws.rest.mx.EnergyScanRestWebService;
import ispyb.ws.rest.mx.ImageWebService;
import ispyb.ws.rest.mx.PhasingRestWebService;
import ispyb.ws.rest.mx.ProteinRestWebService;
import ispyb.ws.rest.mx.SampleRestWebService;
import ispyb.ws.rest.mx.WorkflowRestWebService;
import ispyb.ws.rest.mx.XFEFluorescenceSpectrumRestWebService;
import ispyb.ws.rest.proposal.DewarRestWebService;
import ispyb.ws.rest.proposal.ProposalRestWebService;
import ispyb.ws.rest.proposal.SessionRestWebService;
import ispyb.ws.rest.proposal.ShippingRestWebService;
import ispyb.ws.rest.saxs.BufferRestWebService;
import ispyb.ws.rest.saxs.DataCollectionRestWebService;
import ispyb.ws.rest.saxs.ExperimentRestWebService;
import ispyb.ws.rest.saxs.FrameRestWebService;
import ispyb.ws.rest.saxs.MacromoleculeRestWebService;
import ispyb.ws.rest.saxs.MeasurementRestWebService;
import ispyb.ws.rest.saxs.ModelingRestWebService;
import ispyb.ws.rest.saxs.SaxsRestWebService;
import ispyb.ws.rest.saxs.SpecimenRestWebService;
import ispyb.ws.rest.saxs.SaxsStatsRestWebService;
import ispyb.ws.rest.saxs.StockSolutionRestWebService;
import ispyb.ws.rest.saxs.SubtractionRestWebService;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A class extending {@link javax.ws.rs.core.Application} is the portable way to define JAX-RS 2.0 resources, and the {@link javax.ws.rs.ApplicationPath} defines the root path shared by all these resources.
 */
@ApplicationPath("rest")
public class RestApplication extends Application {
	public RestApplication() {
//        BeanConfig beanConfig = new BeanConfig();
//        beanConfig.setVersion("1.0.2");
//        beanConfig.setSchemes(new String[]{"http"});
//        beanConfig.setHost("localhost:8002");
//        beanConfig.setBasePath("/api");
//        beanConfig.setResourcePackage("io.swagger.resources");
//        beanConfig.setScan(true);
    }

    
//    @Override
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> resources = new HashSet<Class<?>> ();
//        /** MX **/
//        resources.add(AutoprocintegrationRestWebService.class);
//        resources.add(CrystalRestWebService.class);
//        resources.add(DataCollectionRestWebService.class);
//        resources.add(EnergyScanRestWebService.class);
//        resources.add(ImageWebService.class);
//        resources.add(PhasingRestWebService.class);
//        resources.add(ProteinRestWebService.class);
//        resources.add(SampleRestWebService.class);
//        resources.add(WorkflowRestWebService.class);
//        resources.add(XFEFluorescenceSpectrumRestWebService.class);
//        
//        /** SAXS **/
//        resources.add(BufferRestWebService.class);
//        resources.add(DataCollectionRestWebService.class);
//        resources.add(ExperimentRestWebService.class);
//        resources.add(FrameRestWebService.class);
//        resources.add(MacromoleculeRestWebService.class);
//        resources.add(MeasurementRestWebService.class);
//        resources.add(ModelingRestWebService.class);
//        resources.add(SaxsRestWebService.class);
//        resources.add(SpecimenRestWebService.class);
//        resources.add(StatsRestWebService.class);
//        resources.add(StockSolutionRestWebService.class);
//        resources.add(SubtractionRestWebService.class);
//        
//        /** PROPOSAL **/
//        resources.add(DewarRestWebService.class);
//        resources.add(ProposalRestWebService.class);
//        resources.add(SessionRestWebService.class);
//        resources.add(ShippingRestWebService.class);
//        
//        
//        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
//        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
//        return resources;
//    }
}
