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
package com.habicus.core.security;

import static com.habicus.core.security.SecurityConstants.EXPIRATION_TIME;
import static com.habicus.core.security.SecurityConstants.HEADER_STRING;
import static com.habicus.core.security.SecurityConstants.SECRET;
import static com.habicus.core.security.SecurityConstants.TOKEN_PREFIX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habicus.core.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;
  private static final Logger LOGGER = Logger.getLogger(JWTAuthenticationFilter.class.getName());

  JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {
      User user = new ObjectMapper().readValue(req.getInputStream(), User.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null));
    } catch (IOException e) {
      LOGGER.warning("Problem attempting authentication for request.");
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
    LOGGER.info("Successful authentication, generating token");
    String token =
        Jwts.builder()
            .setSubject(
                ((org.springframework.security.core.userdetails.User) auth.getPrincipal())
                    .getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
            .compact();
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }
}
