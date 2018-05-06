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
package com.habicus.core.service.Security;

import com.habicus.core.dao.repository.GoalRepository;
import com.habicus.core.dao.repository.UserRepository;
import com.habicus.core.model.User;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  // Repository definitions
  @Autowired private GoalRepository goalRepository;
  @Autowired private UserRepository userRepository;

  /**
   * Returns the proper {@link User#userId} based on requesting {@link Principal}
   *
   * @param principal
   * @return {@link User#userId}
   */
  public int getUserIdByPrincipal(Principal principal) {
    String subjectUser = principal.getName();
    Optional<User> requestedUser = userRepository.findUserByUsername(subjectUser);
    if (requestedUser.isPresent()) {
      return requestedUser.get().getUserId();
    }
    throw new UsernameNotFoundException(
        "Associated user for: " + principal.getName() + " is invalid");
  }
}
