/*
 * Copyright 2023 open knowledge GmbH
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
package de.openknowledge.sample.address.application.v1;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import de.openknowledge.sample.address.domain.Address;

@Provider
@ApplicationScoped
@Produces({"application/vnd.de.openknowledge.sample.address.v1+json", "application/json"})
public class AddressMessageBodyWriter implements MessageBodyWriter<Address> {

    @Context
    private Providers providers;

    @Override
    public boolean isWriteable(Class<?> targetClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Address address, Class<?> targetClass, Type type, Annotation[] annotation, MediaType mediaType,
        MultivaluedMap<String, Object> headers, OutputStream out) throws IOException, WebApplicationException {
        
        MessageBodyWriter<Object> jsonWriter = providers.getMessageBodyWriter(Object.class, Object.class, annotation, APPLICATION_JSON_TYPE);
        jsonWriter.writeTo(new AddressV1(address), AddressV1.class, AddressV1.class, annotation, APPLICATION_JSON_TYPE, headers, out);
    }
}
