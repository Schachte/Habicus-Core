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

import static com.habicus.core.configuration.CoreConstants.APPLICATION_DEVELOPMENT_PROFILE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.exception.NoGoalsFoundException;
import com.habicus.core.exception.StandardUserException;
import com.habicus.core.model.Goal;
import com.habicus.core.service.Security.SecurityService;
import com.habicus.core.service.User.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("testerUser1")
@TestPropertySource(locations = "classpath:" + APPLICATION_DEVELOPMENT_PROFILE)
public class GoalServiceTest {

  // Constants
  private final int DUMMY_GOAL_ID = 1;

  private final int DUMMY_USER_ID = 5;

  private final int DUMMY_USER_ID_2 = 3;

  // Setup mocks
  @Mock private Principal principal;
  @Mock private Goal goal;
  @Mock private SecurityService securityService;
  @Mock private UserService userService;
  @Mock private GoalRepository goalRepository;

  @InjectMocks private GoalService goalService = spy(new GoalService());

  @Test
  public void retrieveGoalsByUserId() {
    Goal testGoal = new Goal();
    testGoal.setLabelColor("yellow");
    testGoal.setGoalId(DUMMY_GOAL_ID);

    List<Goal> goalList = new ArrayList<>();
    goalList.add(testGoal);

    doReturn(DUMMY_USER_ID).when(userService).verifyAndRetrieveUser(principal);
    doReturn(Optional.of(goalList)).when(goalRepository).getGoalsByUsersUserId(DUMMY_USER_ID);

    Optional<List<Goal>> retrievedUserGoals = goalService.retrieveGoalsByUserId(principal);
    assertEquals(true, retrievedUserGoals.isPresent());
    assertEquals(goalList.get(0), retrievedUserGoals.get().get(0));
  }

  @Test(expected = NoGoalsFoundException.class)
  public void addNewGoalFails() {
    Optional<Goal> savedGoal = goalService.addNewGoal(principal, null);
  }

  @Test
  public void addNewGoalSucceeds() {
    GoalService spyGoalService = spy(goalService);

    Goal testGoal = new Goal();
    testGoal.setLabelColor("yellow");
    testGoal.setGoalId(DUMMY_GOAL_ID);
    doReturn(testGoal).when(spyGoalService).assignUserToGoal(principal, testGoal);
    doReturn(null).when(goalRepository).save(testGoal);
    Optional<Goal> savedGoal = spyGoalService.addNewGoal(principal, testGoal);

    assertEquals(true, savedGoal.isPresent());
    assertEquals("yellow", savedGoal.get().getLabelColor());
  }

  @Test
  public void removeGoalIsSuccessful() {
    GoalService spyGoalService = spy(goalService);
    Goal testGoal = new Goal();
    testGoal.setLabelColor("yellow");
    testGoal.setGoalId(DUMMY_GOAL_ID);

    doReturn(Optional.of(testGoal)).when(spyGoalService).goalExists(DUMMY_GOAL_ID);
    doReturn(true).when(spyGoalService).requesterOwnsGoal(principal, testGoal);
    doNothing().when(goalRepository).delete(testGoal);

    Goal deletedGoal = spyGoalService.removeGoal(principal, DUMMY_GOAL_ID);
    assertEquals(true, deletedGoal != null);
    assertEquals("yellow", deletedGoal.getLabelColor());
    assertEquals(DUMMY_GOAL_ID, deletedGoal.getGoalId());
  }

  @Test(expected = StandardUserException.class)
  public void removeGoalIsUnsuccessful_ReqNotGoalOwner() {
    GoalService spyGoalService = spy(goalService);
    doReturn(Optional.of(goal)).when(spyGoalService).goalExists(DUMMY_GOAL_ID);
    doReturn(false).when(spyGoalService).requesterOwnsGoal(principal, goal);
    spyGoalService.removeGoal(principal, DUMMY_GOAL_ID);
  }

  @Test(expected = StandardUserException.class)
  public void removeGoalIsUnsuccessful_GoalIsNull() {
    GoalService spyGoalService = spy(goalService);
    doReturn(Optional.ofNullable(null)).when(spyGoalService).goalExists(DUMMY_GOAL_ID);
    spyGoalService.removeGoal(principal, DUMMY_GOAL_ID);
  }

  @Test
  public void updateExistingGoalIsValid() throws CloneNotSupportedException {
    // Mock new goal service to mock class methods
    GoalService spyGoalService = spy(goalService);

    // Test goal to use setters/getters
    // "Previously Existing" Goal
    Goal oldGoal = new Goal();
    oldGoal.setGoalId(DUMMY_GOAL_ID);
    oldGoal.setLabelColor("pink");

    assertEquals("pink", oldGoal.getLabelColor());
    assertEquals(DUMMY_GOAL_ID, oldGoal.getGoalId());

    // Modifying previous goal object
    Goal newGoal = (Goal) oldGoal.clone();
    newGoal.setTitle("new title");
    newGoal.setDescription("new description");
    newGoal.setDueDate(1231445435);
    newGoal.setLabelColor("green");
    newGoal.setGoalInterval("daily");
    newGoal.setPledgeAmount(1.50);
    newGoal.setTaskUnitCount(5);

    doReturn(Optional.of(oldGoal)).when(spyGoalService).goalExists(DUMMY_GOAL_ID);
    doReturn(true).when(spyGoalService).requesterOwnsGoal(principal, oldGoal);
    doReturn(Optional.of(oldGoal)).when(goalRepository).getGoalByGoalId(DUMMY_GOAL_ID);
    doReturn(null).when(goalRepository).save(oldGoal);

    Optional<Goal> updatedGoal = spyGoalService.updateExistingGoal(principal, newGoal);
    assertEquals(true, updatedGoal.isPresent());
    assertEquals("new title", updatedGoal.get().getTitle());
    assertEquals("new description", updatedGoal.get().getDescription());
    assertEquals(1231445435, updatedGoal.get().getDueDate());
    assertEquals("green", updatedGoal.get().getLabelColor());
    assertEquals(Optional.of(1.50).get(), updatedGoal.get().getPledgeAmount());
    assertEquals(5, updatedGoal.get().getTaskUnitCount());
  }

  @Test(expected = NoGoalsFoundException.class)
  public void
      updateExistingGoalInputGoalIsInvalid_AndThrows_NoGoalsFoundException_NotInRepository() {
    when(goalRepository.getGoalByGoalId(any(int.class)))
        .thenThrow(new NoGoalsFoundException("Requested goal doesn't exist"));
    goalService.updateExistingGoal(principal, goal);
  }

  @Test(expected = NoGoalsFoundException.class)
  public void
      updateExistingGoalInputGoalIsInvalid_AndThrows_NoGoalsFoundException_Null_or_Invalid() {
    // Mock new goal service to mock class methods
    GoalService spyGoalService = spy(goalService);
    Goal testGoal = new Goal();
    goal.setGoalId(DUMMY_GOAL_ID);
    spyGoalService.updateExistingGoal(principal, testGoal);
  }

  @Test(expected = StandardUserException.class)
  public void updateExistingGoalInputGoal_NotOwnedByRequester_AndThrows_StandardUserException() {
    // Mock new goal service to mock class methods
    GoalService spyGoalService = spy(goalService);

    // Test goal to use setters/getters
    Goal testGoal = new Goal();
    testGoal.setGoalId(DUMMY_GOAL_ID);

    doReturn(Optional.of(testGoal)).when(spyGoalService).goalExists(DUMMY_GOAL_ID);
    doReturn(false).when(spyGoalService).requesterOwnsGoal(principal, testGoal);
    doReturn(Optional.of(testGoal)).when(goalRepository).getGoalByGoalId(DUMMY_GOAL_ID);

    spyGoalService.updateExistingGoal(principal, testGoal);
  }

  @Test
  public void goalExists() {
    doReturn(Optional.of(goal)).when(goalRepository).getGoalByGoalId(DUMMY_GOAL_ID);
    Optional<Goal> existingGoal = goalService.goalExists(DUMMY_GOAL_ID);
    assertEquals(true, existingGoal.isPresent());
  }

  @Test
  public void goalDoesNotExist() {
    doReturn(Optional.ofNullable(null)).when(goalRepository).getGoalByGoalId(DUMMY_GOAL_ID);
    Optional<Goal> existingGoal = goalService.goalExists(DUMMY_GOAL_ID);
    assertEquals(false, existingGoal.isPresent());
  }

  @Test
  public void verifiedUserIsAssignedToUserGoal() {
    Goal testGoal = new Goal();
    doReturn(DUMMY_USER_ID).when(userService).verifyAndRetrieveUser(any(Principal.class));
    Goal assignedGoal = goalService.assignUserToGoal(principal, testGoal);
    assertEquals(DUMMY_USER_ID, assignedGoal.getUsersUserId());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void unverifiedUserFailsToAssignToUserGoal() {
    when(securityService.getUserIdByPrincipal(any(Principal.class)))
        .thenThrow(new UsernameNotFoundException("User does not exist!"));
    goalService.requesterOwnsGoal(principal, goal);
  }

  @Test
  public void testRequesterOwnsGoalIsFalseNoException() {
    doReturn(DUMMY_USER_ID_2).when(securityService).getUserIdByPrincipal(any(Principal.class));
    when(goal.getUsersUserId()).thenReturn(DUMMY_USER_ID);
    boolean userOwnsGoal = goalService.requesterOwnsGoal(principal, goal);
    assertEquals(false, userOwnsGoal);
  }

  @Test
  public void testRequesterOwnsGoalIsTrue() {
    doReturn(DUMMY_USER_ID).when(securityService).getUserIdByPrincipal(any(Principal.class));
    when(goal.getUsersUserId()).thenReturn(DUMMY_USER_ID);
    boolean userOwnsGoal = goalService.requesterOwnsGoal(principal, goal);
    assertEquals(true, userOwnsGoal);
  }

  @Test(expected = StandardUserException.class)
  public void testRequesterDoesNotOwnGoal_And_ThrowsStandardUserException() {
    goalService.requesterOwnsGoal(principal, null);
  }
}
