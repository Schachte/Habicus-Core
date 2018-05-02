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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class GoalsPK implements Serializable {

  private int goalId;
  private int usersUserId;
  private long dueDate;

  @Column(name = "goal_id")
  @Id
  public int getGoalId() {
    return goalId;
  }

  public void setGoalId(int goalId) {
    this.goalId = goalId;
  }

  @Column(name = "users_user_id")
  @Id
  public int getUsersUserId() {
    return usersUserId;
  }

  public void setUsersUserId(int usersUserId) {
    this.usersUserId = usersUserId;
  }

  @Column(name = "due_date")
  @Id
  public long getDueDate() {
    return dueDate;
  }

  public void setDueDate(long dueDate) {
    this.dueDate = dueDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GoalsPK goalPK = (GoalsPK) o;
    return goalId == goalPK.goalId
        && usersUserId == goalPK.usersUserId
        && Objects.equals(dueDate, goalPK.dueDate);
  }

  @Override
  public int hashCode() {

    return Objects.hash(goalId, usersUserId, dueDate);
  }
}
