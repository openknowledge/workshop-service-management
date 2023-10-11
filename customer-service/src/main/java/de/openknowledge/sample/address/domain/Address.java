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

public class Address {
    private Recipient recipient;
    private AddressLine addressLine1;
    private AddressLine addressLine2 = AddressLine.EMPTY;
    private City location;

    @JsonbCreator
    public Address(@JsonbProperty("recipient") Recipient recipient) {
        this.recipient = notNull(recipient, "recipient may not be null");
    }

    public Address(Recipient recipient, AddressLine addressLine1, City location) {
        this(recipient);
        setAddressLine1(addressLine1);
        setLocation(location);
    }

    @JsonbTypeAdapter(Recipient.Adapter.class)
    public Recipient getRecipient() {
        return recipient;
    }

    @JsonbTypeAdapter(AddressLine.Adapter.class)
    public AddressLine getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(AddressLine addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @JsonbTypeAdapter(AddressLine.Adapter.class)
    public AddressLine getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(AddressLine addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public City getLocation() {
        return location;
    }

    public void setLocation(@JsonbProperty("location") City location) {
        this.location = location;
    }

    @JsonbProperty("street")
    public void setStreet(Street street) {
        this.addressLine1 = new AddressLine(street.toString());
    }

    @JsonbProperty("city")
    public void setCity(String city) {
        this.location = new City(getZipCode(city), getCityName(city));
    }

    private ZipCode getZipCode(String addressLine) {
        String firstSegment = addressLine.substring(0, addressLine.indexOf(' '));
        String lastSegment = addressLine.substring(addressLine.lastIndexOf(' ') + 1);
        if (containsDigit(firstSegment)) {
            return new ZipCode(firstSegment);
        } else if (containsDigit(lastSegment)) {
            return new ZipCode(addressLine.substring(firstSegment.length()));
        } else {
            throw new IllegalStateException("Could not determine zip code");
        }
    }

    private CityName getCityName(String addressLine) {
        String firstSegment = addressLine.substring(0, addressLine.indexOf(' '));
        String lastSegment = addressLine.substring(addressLine.lastIndexOf(' ') + 1);
        if (containsDigit(firstSegment)) {
            return new CityName(addressLine.substring(firstSegment.length()));
        } else if (containsDigit(lastSegment)) {
            return new CityName(addressLine.substring(0, firstSegment.length()));
        } else {
            throw new IllegalStateException("Could not determine city addressLine");
        }
    }

    private boolean containsDigit(String addressLine) {
        return addressLine.contains("0")
                || addressLine.contains("1")
                || addressLine.contains("2")
                || addressLine.contains("3")
                || addressLine.contains("4")
                || addressLine.contains("5")
                || addressLine.contains("6")
                || addressLine.contains("7")
                || addressLine.contains("8")
                || addressLine.contains("9");
    }
}
