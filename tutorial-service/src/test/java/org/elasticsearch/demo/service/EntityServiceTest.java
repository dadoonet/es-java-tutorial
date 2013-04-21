/*
 * Licensed to Elasticsearch (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.demo.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.demo.model.bean.Address;
import org.elasticsearch.demo.model.bean.Person;
import org.elasticsearch.node.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class EntityServiceTest {

    @Test
    public void testNode() {
        Node node = ElasticsearchFactory.getNode();
        Assert.assertNotNull(node);
    }

    @Test
    public void testClient() {
        Client client = ElasticsearchFactory.getClient();
        Assert.assertNotNull(client);
    }

    @Test
    public void testCreatePerson() {
        EntityService service =  new EntityService();
        Person person = new Person();
        person.setName("Ritchie Cunningham");
        person.setGender("Male");
        person.setDateOfBirth(new Date());
        Address address = new Address();
        address.setCity("Milwaukee");
        address.setCountry("USA");
        address.setZipcode("XXXXX");
        person.setAddress(address);

        service.save(person);
    }

}
