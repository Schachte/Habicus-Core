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
package com.habicus.core.controller.v1.goal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.habicus.core.exception.API.InvalidRequestException;
import com.habicus.core.exception.NoGoalsFoundException;
import com.habicus.core.model.Goal;
import com.habicus.core.service.Goal.GoalService;
import com.habicus.core.service.User.UserService;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@RequestMapping("/api/v1")
public class GoalController {

  private static final Logger LOGGER = Logger.getLogger(GoalController.class.getName());

  private GoalService goalService;
  private UserService userService;

  @Autowired
  public void setGoalService(GoalService goalService) {
    this.goalService = goalService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  /**
   * Creates a user goal and then persists it for that specific user Grabs the current security
   * subject to derive the proper user
   *
   * @return {@link ResponseEntity} of the {@link com.habicus.core.model.User} {@link Goal}
   */
  @PostMapping("/goal/")
  public ResponseEntity<ImmutableMap<String, String>> createUserGoal(
      @RequestBody @Valid Goal goal, Errors errors) {

    Optional<Goal> createdGoal = goalService.addNewGoal(goal);

    if (errors.hasErrors() || !createdGoal.isPresent()) {
      LOGGER.info("Goal was null, unable to be created and saved successfully");
      throw new InvalidRequestException(
          String.format("Error creating goal: " + goal.toString()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    LOGGER.info("Goal created and saved successfully");

    ImmutableMap<String, String> responseData =
        new Builder<String, String>()
            .put("message", "Goal saved and created successfully!")
            .put("object", createdGoal.get().toString())
            .put("status", String.valueOf(HttpStatus.CREATED))
            .build();

    return new ResponseEntity<>(responseData, HttpStatus.CREATED);
  }

  /**
   * Allows retrieval by a single userId for getting goals
   *
   * @param userId
   * @return {@link ResponseEntity} representing the retrieved user goals
   * @throws NoGoalsFoundException
   */
  @GetMapping("/goal/{userId}")
  public ResponseEntity<List<Goal>> getGoalsByUserId(@PathVariable("userId") int userId)
      throws NoGoalsFoundException {

    Optional<List<Goal>> goalList = goalService.retrieveGoalsByUserId(userId);
    if (!goalList.isPresent()) {
      throw new InvalidRequestException(
          String.format("No goals found for requesting user: " + userId), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(goalList.get(), HttpStatus.OK);
  }
}
