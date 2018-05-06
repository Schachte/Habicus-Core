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

import com.google.common.annotations.VisibleForTesting;
import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.exception.NoGoalsFoundException;
import com.habicus.core.exception.StandardUserException;
import com.habicus.core.model.Goal;
import com.habicus.core.service.Security.SecurityService;
import com.habicus.core.service.User.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
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
  @Autowired private SecurityService secService;

  /**
   * Allows retrieval of a list of goals that can be de-serialized into JSON to the client
   *
   * @param principal
   * @return Returns an array list of {@link Goal}
   */
  public Optional<List<Goal>> retrieveGoalsByUserId(Principal principal)
      throws NoGoalsFoundException {
    int userId = userService.verifyAndRetrieveUser(principal);
    Optional<List<Goal>> userGoals = goalRepository.getGoalsByUsersUserId(userId);
    return userGoals;
  }

  /**
   * Allows the creation of a new {@link Goal} user the requesting user token
   *
   * @param goal
   * @return {@link Optional<Goal>} representing response data after inserted into repository
   */
  public Optional<Goal> addNewGoal(Principal principal, Goal goal) {
    goal =
        Optional.ofNullable(goal)
            .map(Goal::retrieveInstance)
            .orElseThrow(() -> new NoGoalsFoundException("Invalid Goal Object"));

    goal = assignUserToGoal(principal, goal);
    goalRepository.save(goal);
    LOGGER.info("Goal object saved: " + goal.toString());
    return Optional.of(goal);
  }

  /**
   * Allows a user to remove a specific {@link Goal} in interest
   *
   * @param principal
   * @param goalId
   * @return {@link Optional<Goal>} removed goal
   */
  public Goal removeGoal(Principal principal, int goalId) {
    Optional<Goal> requestedGoal = goalExists(goalId);
    if (requestedGoal.isPresent() && requesterOwnsGoal(principal, requestedGoal.get())) {
      goalRepository.delete(requestedGoal.get());
      return requestedGoal.get();
    }
    throw new StandardUserException(
        "Unable to remove the requested goal, either the user doesn't own the goal or the id is invalid");
  }

  /**
   * Allows a user to update an existing goal object with new data. This is assumed that an entirely
   * new (valid) goal object is passed in
   *
   * @param principal
   * @param goal
   * @return
   */
  public Optional<Goal> updateExistingGoal(Principal principal, Goal goal) {
    if (goalExists(goal.getGoalId()).isPresent()) {

      Goal existingGoal = goalRepository.getGoalByGoalId(goal.getGoalId()).get();

      if (requesterOwnsGoal(principal, existingGoal)) {
        existingGoal.setTitle(goal.getTitle());
        existingGoal.setDescription(goal.getDescription());
        existingGoal.setDueDate(goal.getDueDate());
        existingGoal.setLabelColor(goal.getLabelColor());
        existingGoal.setGoalInterval(goal.getGoalInterval());
        existingGoal.setPledgeAmount(goal.getPledgeAmount());
        existingGoal.setTaskUnitCount(goal.getTaskUnitCount());
        goalRepository.save(existingGoal);
      } else {
        throw new StandardUserException("Permissions not granted on current goal.");
      }
      return Optional.of(existingGoal);
    }
    throw new NoGoalsFoundException("Input Goal Is Null or Invalid!");
  }

  /**
   * Checks to see if a particular goal exists or not
   *
   * @param goalId
   * @return {@link Goal} will be returned if exists, null otherwise
   */
  @VisibleForTesting
  Optional<Goal> goalExists(int goalId) {
    return goalRepository.getGoalByGoalId(goalId);
  }

  /** Assigns a particular requesting {@link com.habicus.core.model.User} to the new goal object */
  @VisibleForTesting
  Goal assignUserToGoal(Principal principal, Goal goal) {
    int userId = userService.verifyAndRetrieveUser(principal);
    goal.setUsersUserId(userId);
    return goal;
  }

  /**
   * Verifies that a requesting subject owns a particular goal that is being requested
   *
   * @param principal
   * @param goal
   * @return
   */
  @VisibleForTesting
  Boolean requesterOwnsGoal(Principal principal, Goal goal) {
    Optional<Goal> optionalGoal = Optional.ofNullable(goal);
    if (optionalGoal.isPresent()) {
      return secService.getUserIdByPrincipal(principal) == optionalGoal.get().getUsersUserId();
    }
    throw new StandardUserException("Requesting user does not have access to goal");
  }
}
