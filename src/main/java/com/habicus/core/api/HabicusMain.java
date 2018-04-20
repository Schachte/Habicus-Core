package com.habicus.core.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabicusMain {
  @RequestMapping("/")
  public String index() {
    return "Welcome To Habicus!";
  }
}

