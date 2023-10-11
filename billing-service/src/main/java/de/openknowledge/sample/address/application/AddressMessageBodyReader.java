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
package de.openknowledge.sample.address.application;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import de.openknowledge.sample.address.domain.Address;

@Provider
@ApplicationScoped
@Consumes("application/vnd.de.openknowledge.sample.address.v2+json")
public class AddressMessageBodyReader implements MessageBodyReader<Address> {

    @Context
    private Providers providers;

    @Override
    public boolean isReadable(Class<?> targetClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Address readFrom(Class<Address> targetClass, Type type, Annotation[] annotations, MediaType mediaType,
        MultivaluedMap<String, String> headers, InputStream in) throws IOException, WebApplicationException {

        MessageBodyReader<Address> jsonReader = providers.getMessageBodyReader(Address.class, Address.class, annotations, APPLICATION_JSON_TYPE);
        return jsonReader.readFrom(Address.class, type, annotations, APPLICATION_JSON_TYPE, headers, in);
    }
}
