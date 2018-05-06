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
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements UserDetailsService {

  @Autowired UserRepository userRepository;

  /**
   * Allows searching for a user by their associated email (PK)
   *
   * @param email
   * @return
   */
  public User retrieveUserByEmail(String email) {
    return Stream.of(userRepository.findByEmail(email))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("User does not exist!"));
  }

  public User FindByUserName(String userName) {
    return new User();
  }

  public void save(User user) {
    userRepository.save(user);
  }

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findUserByUsername(username);

    User returnedUser = user.orElseThrow(() -> new UsernameNotFoundException(username));

    return new org.springframework.security.core.userdetails.User(
        returnedUser.getUsername(), returnedUser.getPassword(), Collections.emptyList());
  }

  /**
   * Verifies that the user exists off the current auth token and returns the {@link User#userId}
   *
   * @param principal
   * @return
   */
  public int verifyAndRetrieveUser(Principal principal) {
    String principalName = principal.getName();
    Optional<User> user = userRepository.findUserByUsername(principalName);

    if (user.isPresent() && user.get().getUsername().equals(principalName)) {
      return user.get().getUserId();
    }

    throw new UsernameNotFoundException("User does not exist!");
  }
}
