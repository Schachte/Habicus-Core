package com.habicus.repository.DataContainers;


import com.habicus.core.entities.Goal;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Allows a container to hold a list of users and map to standard POJO */
@XmlRootElement(name = "GoalContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class GoalContainer implements Container {

  public GoalContainer() {}

  @XmlElement(name = "goal")
  private List<Goal> goals;

  public List<Goal> getGoals() {
    return goals;
  }

  public void setUsers(List<Goal> goals) {
    this.goals = goals;
  }

  @Override
  public List<Goal> getAll() {
    return goals;
  }

}
