package com.habicus.core.user;

public abstract class UserDecorator implements User {
  User user;

  public UserDecorator(User user) {
    this.user = user;
  }
}
