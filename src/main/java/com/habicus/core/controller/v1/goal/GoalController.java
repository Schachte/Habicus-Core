package com.habicus.core.controller.v1.goal;

import com.habicus.core.model.Goal;
import com.habicus.core.service.Goal.GoalService;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/goal")
public class GoalController {

  private static final Logger LOGGER = Logger.getLogger(GoalController.class.getName());

  private GoalService goalService;

  @Autowired
  public void setGoalService(GoalService goalService) {
    this.goalService = goalService;
  }

  /**
   * Retrieves all goals that are associated with a particular {@link com.habicus.core.model.User}
   * given an input ID by the client
   *
   * @param id
   * @return
   */
  @RequestMapping(
    value = "/{id}",
    method = RequestMethod.GET,
    produces = {MediaType.APPLICATION_JSON}
  )
  public List<Goal> retrieveUserGoals(@PathVariable(value = "id") Long id) {
    LOGGER.info(String.format("Retrieving goals associated with user {}", id));
    return goalService.retrieveGoalsByUserId(id);
  }
}
