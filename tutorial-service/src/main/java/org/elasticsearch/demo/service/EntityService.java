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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.demo.model.bean.Person;

import java.io.IOException;

public class EntityService {
    public String save(Person person) {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = null;
        try {
            IndexResponse indexResponse = ElasticsearchFactory.getClient()
                    .prepareIndex("world", "person")
                    .setSource(objectMapper.writeValueAsBytes(person))
                    .execute().actionGet();
            id = indexResponse.getId();
        } catch (JsonProcessingException e) {
            // We have an exception here. We should handle it...
            throw new RuntimeException("Can not save entity " + person);
        }
        return id;
    }

    public Person get(String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = null;
        try {
            GetResponse getResponse = ElasticsearchFactory.getClient()
                    .prepareGet("world", "person", id)
                    .execute().actionGet();
            if (getResponse.isExists()) {
                person = objectMapper.readValue(getResponse.getSourceAsBytes(), Person.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not read entity " + id);
        }

        return person;
    }

    public void delete(String id) {
        DeleteResponse deleteResponse = ElasticsearchFactory.getClient()
                .prepareDelete("world", "person", id)
                .execute().actionGet();
        if (deleteResponse.isNotFound()) throw new RuntimeException("Entity " + id + " was not existing");
    }
}
