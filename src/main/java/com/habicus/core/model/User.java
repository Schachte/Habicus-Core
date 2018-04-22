/*
 _   _       _     _
| | | | __ _| |__ (_) ___ _   _ ___
| |_| |/ _` | '_ \| |/ __| | | / __|
|  _  | (_| | |_) | | (__| |_| \__ \
|_| |_|\__,_|_.__/|_|\___|\__,_|___/

 * This file is part of the Habicus Core Platform (https://github.com/Habicus/Habicus-Core).
 * Copyright (c) 2018 Habicus Core
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.habicus.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/** User entity that will reference */
@Entity
@XmlRootElement(name = "user")
public class User {

  private Long id;

  private String username;

  private String password;

  private String gender;

  private String phone;

  private String email;

  private List<Goal> assignedUserGoals;

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  @Column(name = "user_id")
  public Long getId() {
    return this.id;
  }

  @XmlElement(name = "username")
  public String getUserName() {
    return username;
  }

  public void setUserName(String username) {
    this.username = username;
  }

  @ElementCollection(targetClass = Goal.class)
  @XmlElementWrapper(name = "goals")
  @XmlElement(name = "goal")
  @OneToMany(cascade = CascadeType.ALL)
  @JsonIgnore
  public List<Goal> getAssignedUserGoals() {
    return assignedUserGoals;
  }

  public void setAssignedUserGoals(List<Goal> assignedUserGoals) {
    this.assignedUserGoals = assignedUserGoals;
    for (Goal g : this.assignedUserGoals) {
      g.setUser(this);
    }
  }

  @XmlElement(name = "password")
  public String getEncryptedPassword() {
    return password;
  }

  public void setEncryptedPassword(String encryptedPassword) {
    this.password = encryptedPassword;
  }

  @XmlElement(name = "gender")
  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @XmlElement(name = "phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @XmlElement(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
