/*
 * Copyright 2019 open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.openknowledge.sample.address.application;

import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.STRING;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import de.openknowledge.sample.address.domain.Address;
import de.openknowledge.sample.address.domain.AddressValidationService;
import de.openknowledge.sample.address.domain.AddressRepository;
import de.openknowledge.sample.address.domain.CustomerNumber;

/**
 * RESTFul endpoint for delivery addresses
 */
@ApplicationScoped
@Path("/delivery-addresses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressesResource {

    private static final Logger LOG = Logger.getLogger(AddressesResource.class.getSimpleName());

    @Inject
    private AddressValidationService addressValidationService;
    @Inject
    private AddressRepository addressRepository;

    @GET
    @Path("/{customerNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Address getAddress(
        @Parameter(
            description = "The business identifier of a customer ",
            required = true,
            example = "0815",
            schema = @Schema(type = STRING))
        @PathParam("customerNumber") CustomerNumber number) {
        
        LOG.info("RESTful call 'GET address'");
        return addressRepository.find(number).orElseThrow(NotFoundException::new);
    }

    @POST
    @Path("/{customerNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setAddress(
        @Parameter(
            description = "The business identifier of a customer ",
            required = true,
            example = "0815",
            schema = @Schema(type = STRING))
        @PathParam("customerNumber") CustomerNumber customerNumber,
        Address address,
        @Context UriInfo uri) {
        
        LOG.info("RESTful call 'POST address'");
        addressValidationService.validate(address);
        addressRepository.update(customerNumber, address);
        return Response.ok().build();
    }
}
