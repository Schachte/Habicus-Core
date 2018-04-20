package com.habicus.playground;

import com.habicus.core.entities.Goal;
import com.habicus.core.entities.User;
import com.habicus.repository.DataContainers.GoalContainer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JaxBTestCenter {
  public static void main(String[] args) throws JAXBException {
    User user = new User();

    Goal goal1 = new Goal();
    goal1.setTitle("g1");

    Goal goal2 = new Goal();
    goal2.setTitle("g2");
    List<Goal> goalList = new ArrayList<>();

    goalList.add(goal1);
    goalList.add(goal2);

    user.setAssignedUserGoals(goalList);
    user.setGender("male");
    user.setEmail("email");

    try {
      JAXBContext context = JAXBContext.newInstance(User.class);

      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(user, System.out);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }
}
