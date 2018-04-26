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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.model.Goal;
import com.habicus.core.model.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GoalServiceTest {

  private UserRepository userRepo;
  private GoalRepository goalRepo;
  private GoalService goalService;
  private User user;

  @Before
  public void setUp() throws Exception {
    goalRepo = mock(GoalRepository.class);
    userRepo = mock(UserRepository.class);
    goalService = new GoalService(goalRepo);
    user =
        new User(
            new Long(1), "testerUser", "testerPass", "male", "4804518823", "testeremail@gmail.com");
    userRepo.save(user);
  }

  @Test
  public void retrieveGoalsByUserId() {
    List<Goal> goalList = new ArrayList<>();

    Goal goal1 = new Goal(this.user, "testerDescription", "accountability", "interval");
    Goal goal2 = new Goal(this.user, "testerDescription2", "accountability2", "interval");
    Goal goal3 = new Goal(this.user, "testerDescription3", "accountability3", "interval");
    Goal goal4 = new Goal(this.user, "testerDescription4", "accountability4", "interval");

    goalList.add(goal1);
    goalList.add(goal2);
    goalList.add(goal3);
    goalList.add(goal4);

    when(goalRepo.findGoalsByUserId(new Long(1))).thenReturn(goalList);
    assertEquals(goalService.retrieveGoalsByUserId(new Long(1)), goalList);
  }
}
