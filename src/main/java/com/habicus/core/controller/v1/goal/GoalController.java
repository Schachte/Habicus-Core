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
import com.habicus.core.exception.API.InvalidRequestException;
import com.habicus.core.exception.NoGoalsFoundException;
import com.habicus.core.exception.StandardGoalException;
import com.habicus.core.exception.StandardUserException;
import com.habicus.core.model.Goal;
import com.habicus.core.service.Goal.GoalService;
import com.habicus.core.service.User.UserService;
import com.habicus.core.util.ControllerUtil;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * Enable deletion of a particular {@link Goal} record by a requesting user
   *
   * @param goalId
   * @param principal
   * @return
   */
  @DeleteMapping("/goal/{goalId}")
  public ResponseEntity<ImmutableMap<String, String>> createUserGoal(
      @PathVariable("goalId") int goalId, Principal principal) {
    Goal deletedGoal;
    ImmutableMap<String, String> responseData;
    try {
      deletedGoal = goalService.removeGoal(principal, goalId);
      responseData =
          ControllerUtil.createResponse(
              "Deleted Goal Successfully!", deletedGoal.toString(), HttpStatus.OK);
    } catch (Exception e) {
      responseData =
          ControllerUtil.createResponse(
              "Failure to remove specified goal.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(responseData, HttpStatus.CREATED);
  }

  /**
   * Creates a user goal and then persists it for that specific user Grabs the current security
   * subject to derive the proper user
   *
   * @return {@link ResponseEntity} of the {@link com.habicus.core.model.User} {@link Goal}
   */
  @PostMapping("/goal")
  public ResponseEntity<ImmutableMap> createUserGoal(
      @RequestBody @Valid Goal goal, BindingResult bindResult, Principal principal) {
    ImmutableMap<String, String> responseData;

    if (bindResult.hasErrors()) {
      // TODO: Create error response class to log the bindResult errors
      LOGGER.info("Creation failure during object binding: " + goal.toString());

      responseData =
          ControllerUtil.createResponse(
              "Failure to create new Goal", goal.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
      return new ResponseEntity<>(responseData, HttpStatus.CREATED);
    }

    Optional<Goal> createdGoal = goalService.addNewGoal(principal, goal);
    LOGGER.info("Goal created and saved successfully");

    responseData =
        ControllerUtil.createResponse(
            "Goal saved and created successfully!",
            createdGoal.get().toString(),
            HttpStatus.CREATED);
    return new ResponseEntity<>(responseData, HttpStatus.CREATED);
  }

  @PutMapping("/goal/{goalId}")
  public ResponseEntity<ImmutableMap<String, String>> updateUserGoal(
      @PathVariable("goalId") int goalId,
      @Valid @RequestBody Goal goal,
      BindingResult bindResult,
      Principal principal)
      throws NoGoalsFoundException {

    ImmutableMap<String, String> responseData;
    if (bindResult.hasErrors()) {
      // TODO: Create error response class to log the bindResult errors
      LOGGER.info("Error binding input goal object: " + goal.toString());

      responseData =
          ControllerUtil.createResponse(
              "Failure to update goal", goal.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
      return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    Optional<Goal> updatedGoal = goalService.updateExistingGoal(principal, goal);
    LOGGER.info("Goal updated successfully");

    try {
      responseData =
          ControllerUtil.createResponse(
              "Goal updated successfully", updatedGoal.get().toString(), HttpStatus.OK);
      return new ResponseEntity<>(responseData, HttpStatus.OK);
    } catch (StandardUserException | StandardGoalException e) {
      responseData =
          ControllerUtil.createResponse(
              "Permissions Exception Updating Goal", e.toString(), HttpStatus.OK);
      return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
  }

  /**
   * Allows retrieval by a single userId for getting goals Allows retrieval of user goals based on
   * sec. token
   *
   * @return
   * @throws NoGoalsFoundException
   */
  @ExceptionHandler(NoGoalsFoundException.class)
  @GetMapping("/goals")
  public ResponseEntity<List<Goal>> retrieveUserGoals(Principal principal)
      throws NoGoalsFoundException {

    LOGGER.info("Current querying for all goals on user: " + principal.getName());
    Optional<List<Goal>> goalList = goalService.retrieveGoalsByUserId(principal);

    if (!goalList.isPresent()) {
      throw new InvalidRequestException(
          "No goals found for requesting user: " + principal.getName(), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(goalList.get(), HttpStatus.OK);
  }
}
