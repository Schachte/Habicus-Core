package com.habicus.repository.DataContainers;

import com.habicus.core.entities.User;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Allows a container to hold a list of users and map to standard POJO
 */
@XmlRootElement(name="UserContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserContainer {

  public UserContainer() {}

  @XmlElement(name="user")
  private List<User> users;

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
}
