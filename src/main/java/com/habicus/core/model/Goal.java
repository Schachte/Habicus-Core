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

import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(GoalsPK.class)
public class Goal implements Cloneable {

  private int usersUserId;
  private int taskUnitCount;
  private int goalId;
  private String title;
  private String description;
  private String goalInterval;
  private Double pledgeAmount;

  // UTC Time
  private long dueDate;

  // TODO: Abstract out these properties elsewhere
  // https://github.com/Habicus/Habicus-Core-Web/issues/57
  private String labelColor;
  private String goalComplete;

  @Id
  @Column(name = "goal_id", unique = true)
  public int getGoalId() {
    return goalId;
  }

  public void setGoalId(int goalId) {
    this.goalId = goalId;
  }

  @Basic
  @NotNull
  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @NotNull
  @Column(name = "goal_interval")
  public String getGoalInterval() {
    return goalInterval;
  }

  public void setGoalInterval(String goalInterval) {
    this.goalInterval = goalInterval;
  }

  @Id
  @Column(name = "users_user_id")
  public int getUsersUserId() {
    return usersUserId;
  }

  public void setUsersUserId(int usersUserId) {
    this.usersUserId = usersUserId;
  }

  @Basic
  @NotNull
  @Column(name = "task_unit_count")
  public int getTaskUnitCount() {
    return taskUnitCount;
  }

  public void setTaskUnitCount(int taskUnitCount) {
    this.taskUnitCount = taskUnitCount;
  }

  @Id
  @NotNull
  @Column(name = "due_date")
  public long getDueDate() {
    return dueDate;
  }

  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  @Basic
  @NotNull
  @Column(name = "label_color")
  public String getLabelColor() {
    return labelColor;
  }

  public void setLabelColor(String labelColor) {
    this.labelColor = labelColor;
  }

  @Basic
  @NotNull
  @Column(name = "pledge_amount")
  public Double getPledgeAmount() {
    return pledgeAmount;
  }

  public void setPledgeAmount(Double pledgeAmount) {
    this.pledgeAmount = pledgeAmount;
  }

  @Basic
  @Column(name = "goal_complete")
  public String getGoalComplete() {
    return goalComplete;
  }

  public void setGoalComplete(String goalComplete) {
    this.goalComplete = goalComplete;
  }

  /**
   * TODO: Revisit if this is a good way to validate a current instance. Used in: {@link
   * com.habicus.core.service.Goal.GoalService#addNewGoal(java.security.Principal,
   * com.habicus.core.model.Goal)}
   */
  public Goal retrieveInstance() {
    return this;
  }

  @Override
  public String toString() {
    return "Goal{"
        + "usersUserId="
        + usersUserId
        + ", taskUnitCount="
        + taskUnitCount
        + ", goalId="
        + goalId
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + ", goalInterval='"
        + goalInterval
        + '\''
        + ", pledgeAmount="
        + pledgeAmount
        + ", dueDate="
        + dueDate
        + ", labelColor='"
        + labelColor
        + '\''
        + ", goalComplete='"
        + goalComplete
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Goal goals = (Goal) o;
    return goalId == goals.goalId
        && usersUserId == goals.usersUserId
        && taskUnitCount == goals.taskUnitCount
        && Objects.equals(title, goals.title)
        && Objects.equals(description, goals.description)
        && Objects.equals(goalInterval, goals.goalInterval)
        && Objects.equals(dueDate, goals.dueDate)
        && Objects.equals(labelColor, goals.labelColor)
        && Objects.equals(pledgeAmount, goals.pledgeAmount)
        && Objects.equals(goalComplete, goals.goalComplete);
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public int hashCode() {

    return Objects.hash(
        goalId,
        title,
        description,
        goalInterval,
        usersUserId,
        taskUnitCount,
        dueDate,
        labelColor,
        pledgeAmount,
        goalComplete);
  }
}
