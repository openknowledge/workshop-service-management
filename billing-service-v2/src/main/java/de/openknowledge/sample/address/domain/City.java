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
package de.openknowledge.sample.address.domain;

import static org.apache.commons.lang3.Validate.notNull;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

public class City {

    private ZipCode zipCode;
    private CityName name;

    @JsonbCreator
    public City(@JsonbProperty("zipCode") ZipCode zipCode, @JsonbProperty("name") CityName name) {
        this.zipCode = notNull(zipCode, "zip code may not be null");
        this.name = notNull(name, "name may not be null");
    }

    @JsonbTypeAdapter(ZipCode.Adapter.class)
    public ZipCode getZipCode() {
        return zipCode;
    }

    @JsonbTypeAdapter(CityName.Adapter.class)
    public CityName getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return zipCode.hashCode() ^ name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof City)) {
            return false;
        }

        City city = (City) object;

        return zipCode.equals(city.zipCode) && name.equals(city.getName());
    }

    @Override
    public String toString() {
        return zipCode + " " + name;
    }
}
