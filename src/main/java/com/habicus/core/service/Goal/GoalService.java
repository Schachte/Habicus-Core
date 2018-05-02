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
import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.exception.NoGoalsFoundException;
import com.habicus.core.model.Goal;
import com.habicus.core.service.User.UserService;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

  private static final Logger LOGGER = Logger.getLogger(GoalService.class.getName());

  // Repository definitions
  @Autowired private GoalRepository goalRepository;
  @Autowired private UserRepository userRepository;

  // Service definitions
  @Autowired private UserService userService;

  /**
   * Allows retrieval of a list of goals that can be deserialized into JSON to the client
   *
   * @param userId
   * @return Returns an array list of {@link Goal}
   */
  public Optional<List<Goal>> retrieveGoalsByUserId(int userId) throws NoGoalsFoundException {
    // TODO: Need to actually do validation on this input userId with the req. token
    Optional<List<Goal>> userGoals = goalRepository.getGoalsByUsersUserId(userId);
    return userGoals;
  }

  /**
   * Allows the creation of a new {@link Goal} user the requesting user token
   *
   * @param goal
   * @return
   * @throws NotFoundException
   */
  public Optional<Goal> addNewGoal(Goal goal) {
    // TODO: Need to actually do validation on this input userId with the req. token

    goal =
        Optional.of(goal)
            .map(Goal::retrieveInstance)
            .orElseThrow(() -> new NoGoalsFoundException("Invalid Goal Object"));

    goal = assignUserToGoal(goal);
    goalRepository.save(goal);
    LOGGER.info(String.format("Goal object saved: ", goal.toString()));
    return Optional.of(goal);
  }

  /** Assigns a particular requesting {@link com.habicus.core.model.User} to the new goal object */
  private Goal assignUserToGoal(Goal goal) {
    // TODO: Need to actually do validation on this input userId with the req. token
    // TODO: This is where we need to get the user object from the user-repository and attach

    int dummyUserId = 1;
    goal.setUsersUserId(dummyUserId);
    return goal;
  }
}
