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
package com.habicus.core.dao.repository;

import com.habicus.core.model.Goal;
import com.habicus.core.model.GoalsPK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, GoalsPK> {

  /**
   * Allows retrieval of all {@link Goal} that are associated with a particular {@link
   * com.habicus.core.model.User}
   *
   * @param userId
   * @return
   */
  Optional<List<Goal>> getGoalsByUsersUserId(int userId);

  /**
   * Get a singular {@link Goal} that is associated with a goal id that is stored as metadata
   *
   * @param goalId
   * @return {@link Optional<Goal>} user goal list
   */
  Optional<Goal> getGoalsByGoalId(int goalId);

  /**
   * Retrieves a SINGLE goal by the associated {@link Goal#goalId}
   *
   * @param goalId
   * @return {@link Goal}
   */
  Optional<Goal> getGoalByGoalId(int goalId);
}
