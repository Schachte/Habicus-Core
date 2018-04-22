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
package com.habicus.core.service.Goal;

import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.model.Goal;
import java.util.List;
import javax.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

  @Autowired private GoalRepository goalRepository;

  public List<Goal> retrieveGoalsByUserId(Long id) {
    List<Goal> goals = goalRepository.findGoalsByUserId(id);
    return goals;
  }

  /**
   * Allows a {@link com.habicus.core.model.User} to persist a {@link Goal} object into the DB
   *
   * @param goal
   * @return
   */
  public Goal saveUserGoal(Goal goal) {
    if (goal != null) return goalRepository.save(goal);
    else throw new NotFoundException("Unable to save an empty goal");
  }
}
