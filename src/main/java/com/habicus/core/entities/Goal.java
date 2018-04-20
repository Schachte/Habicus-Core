package com.habicus.core.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Goal entity that will reference */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "goal")
public class Goal {

  @Id
  @GeneratedValue
  private Long id;

  private String title;

  private String description;

  @XmlElement(name="accountable")
  private String accountability;

  private String interval;

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
