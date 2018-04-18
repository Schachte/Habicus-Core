package com.habicus.core.user;

public class StandardUser implements User {

  @Override
  public boolean updateEmail(String email) {
    return false;
  }

  @Override
  public boolean updatePassword(String password) {
    return false;
  }

  @Override
  public boolean updateAddress(String address) {
    return false;
  }
}
