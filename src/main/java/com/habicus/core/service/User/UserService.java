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
package com.habicus.core.service.User;

import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.model.User;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class UserService {

  private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

  @Autowired private UserRepository userRepo;
  /**
   * Allows the input requesting user id to be validated
   *
   * @param userId
   * @return
   */
  public User validateUserById(String userId) {
    return this.userRepo
        .findById(Long.parseLong(userId))
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  /** Used to validate existence of a particular {@link User} by it's associated {@link Long id} */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  private class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
      super(String.format("Unable to find the user by the id: {}", userId));
    }
  }
}
