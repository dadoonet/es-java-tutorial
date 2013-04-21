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

package org.elasticsearch.demo.model.bean;

import java.util.Date;

public class Person {
    private String name = null;
    private Date dateOfBirth = null;
    private String gender = null;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Returns a string representation of the object. 
     */
	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.getClass().getName() + "-");
        sb.append("  name=" + name);
        sb.append("  dateOfBirth=" + dateOfBirth);
        sb.append("  gender=" + gender);
        sb.append("  address=[");
        if (address != null) {
            sb.append(address.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
