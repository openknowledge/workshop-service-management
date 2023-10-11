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
package de.openknowledge.sample.address.application.v1;


import static org.apache.commons.lang3.Validate.notNull;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

import de.openknowledge.sample.address.domain.Address;
import de.openknowledge.sample.address.domain.Recipient;

public class AddressV1 {
    private Recipient recipient;
    private AddressLine addressLine1;
    private AddressLine addressLine2 = AddressLine.EMPTY;
    private Location location;

    @JsonbCreator
    public AddressV1(@JsonbProperty("recipient") Recipient recipient) {
        this.recipient = notNull(recipient, "recipient may not be null");
    }

    public AddressV1(Recipient recipient, AddressLine addressLine1, Location location) {
        this(recipient);
        setAddressLine1(addressLine1);
        setLocation(location);
    }

    public AddressV1(Address address) {
        this(address.getRecipient());
        setAddressLine1(new AddressLine(address.getAddressLine1().toString()));
        setAddressLine2(new AddressLine(address.getAddressLine2().toString()));
        setLocation(new Location(address.getLocation().getZipCode(), address.getLocation().getCityName()));
    }

    @JsonbTypeAdapter(Recipient.Adapter.class)
    public Recipient getRecipient() {
        return recipient;
    }

    @JsonbTypeAdapter(AddressLine.Adapter.class)
    public AddressLine getAddressLine1() {
        return addressLine1;
    }

    public Street getStreet() {
        return new Street(addressLine1);
    }

    public void setStreet(Street street) {
        addressLine1 = street.getAddressLine();
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

    public City getCity() {
        return location.getCity();
    }

    public void setCity(City city) {
        this.location = new Location(city);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Address toAddress() {
        Address address = new Address(recipient);
        address.setAddressLine1(new de.openknowledge.sample.address.domain.AddressLine(addressLine1.toString()));
        address.setAddressLine2(new de.openknowledge.sample.address.domain.AddressLine(addressLine2.toString()));
        address.setLocation(new de.openknowledge.sample.address.domain.Location(location.getZipCode(), location.getCityName()));
        return address;
    }
}
