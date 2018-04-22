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
package com.habicus.test.data;

import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.model.Goal;
import com.habicus.core.model.User;
import com.habicus.test.data.DataContainers.Container;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Handles the internal registration of loading test dao classes to be auto-loaded into the
 * database. This is the only class that should be modified to enable registering a new dao table
 * into the dev env.
 *
 * <p>How To Setup Custom Objects for Storage for Development Environment Database:
 *
 * <ol>
 *   <li>Create a {@link JpaRepository} for your datatype inside of {@link com.habicus.core.dao}
 *   <li>Create a {@link com.habicus.core.model} entity for the datatype
 *   <li>Ensure an associated {@link Container} exists in {@link
 *       com.habicus.test.data.DataContainers}
 * </ol>
 */
@Component
public class DataLoaderRegistrar {

  private static final Logger LOGGER = Logger.getLogger(DataLoaderRegistrar.class.getName());

  /** Auto wired all {@link JpaRepository} references here to support storing test dao in DB */
  @Autowired public UserRepository userRepo;

  @Autowired public GoalRepository goalRepo;

  /**
   * All registered model can go here with an association to their repository as the value Examples
   * can be seen in {@link com.habicus.core.dao GoalRepository} as well as {@link
   * com.habicus.core.model.Goal}
   */
  private Map<String, JpaRepository> reposByName;

  public DataLoaderRegistrar() {}

  @PostConstruct
  public void registerRepos() {
    reposByName = new HashMap<>();

    // Register all repositories here
    reposByName.put(User.class.getSimpleName(), userRepo);
    reposByName.put(Goal.class.getSimpleName(), goalRepo);

    LOGGER.info("Finished loading data repositories for dev environment");
  }

  /**
   * Returns the requested repository that is stored in the
   *
   * @param className
   * @return {@link JpaRepository} that is associated with a particular class name
   */
  public JpaRepository getRepo(String className) {
    return reposByName.get(className);
  }

  /**
   * Method returns a {@link Map} which has a {@link String} as the key and an associated {@link
   * JpaRepository} as the value, which represents a CRUD repository for all supported dao types in
   * our system
   */
  public Map<String, JpaRepository> retrieveAllDataRepositories() {
    return reposByName;
  }
}
