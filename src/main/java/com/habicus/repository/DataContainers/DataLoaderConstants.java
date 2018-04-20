package com.habicus.repository.DataContainers;

import com.habicus.core.data.UserRepository;
import com.habicus.core.entities.User;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class DataLoaderConstants {

  @Autowired
  private UserRepository userRepo;

  public Map<String, JpaRepository> reposByName = new HashMap<>();

  public DataLoaderConstants() {
    reposByName.put(User.class.getSimpleName(), userRepo);
  }

  public JpaRepository getRepo(Object className) {
    return reposByName.get(className);
  }
}
