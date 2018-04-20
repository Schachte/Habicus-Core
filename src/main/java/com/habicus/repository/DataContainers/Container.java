package com.habicus.repository.DataContainers;

import java.util.List;

public interface Container<T> {
  public List<T> getAll();
}
