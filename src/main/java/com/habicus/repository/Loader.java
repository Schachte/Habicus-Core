/*
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

package com.habicus.repository;

import static java.lang.Class.*;

import com.habicus.core.configuration.CoreConstants;
import com.habicus.repository.DataContainers.Container;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.*;
import org.springframework.stereotype.Component;

/**
 * Launches at application boot-time to inject any user-defined test data and stores into test
 * database automatically.
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger LOGGER = Logger.getLogger(Loader.class.getName());

  private static final String CONTAINER_PATH = "com.habicus.repository.DataContainers.";

  @Autowired private DataLoaderRegistrar loaderConstants;

  /**
   * Strips off any dangling file extensions to enable class cast into file ingestor
   *
   * @param fileName
   * @return
   */
  private static String normalizeFileName(String fileName) {
    return fileName.split(".xml")[0];
  }

  /**
   * Takes input file from disk and parsed out contents by marshaling XML -> POJO
   *
   * @param inputFile
   * @param classReference
   * @return
   * @throws JAXBException
   */
  private Container parseFile(Resource inputFile, Class<?> classReference) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(classReference);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Container) jaxbUnmarshaller.unmarshal(inputFile.getFile());

    } catch (IOException | JAXBException exception) {
      LOGGER.log(Level.SEVERE, "Unable to unmarshal data!");
      System.exit(CoreConstants.SYSTEM_EXIT_ERROR);
      return null;
    }
  }

  /**
   * Read in file from static {@link resources} directory
   *
   * @param fileResource
   * @return
   * @throws IOException
   * @throws JAXBException
   * @throws ClassNotFoundException
   */
  private Container ingestFromFile(Resource fileResource) {
    if (fileResource == null) {
      LOGGER.log(Level.SEVERE, "File resource is invalid! Process cannot continue.");
      System.exit(CoreConstants.SYSTEM_EXIT_ERROR);
    }

    try {
      return parseFile(
          fileResource, forName(CONTAINER_PATH + normalizeFileName(fileResource.getFilename())));
    } catch (ClassNotFoundException e) {
      LOGGER.log(Level.SEVERE, "Unable to find the specified data container class");
      System.exit(CoreConstants.SYSTEM_EXIT_ERROR);
      return null;
    }
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    LOGGER.log(Level.INFO, "Preparing to load data into database");

    try {
      loadTestContainers();
    } catch (IOException e) {
      LOGGER.log(Level.INFO, "Unable to store user data");
      e.printStackTrace();
    }
  }

  /**
   * Loads up any test data from resources and stores into embedded DB for test env
   * Containers referring to the test environment data structure, which can be understood more here
   * {@link Container}
   *
   * @throws IOException
   */
  private void loadTestContainers() throws IOException {
    LOGGER.log(Level.INFO, "User data table being added to database");
    Resource[] resources = retrieveTestDataFiles();

    /**
     * Main Persistence Logic:
     *
     * <p>Retrieve data from each file in the resources directory and put into a list Iterate
     * through each item of the list, cast it to a Container object The getAll operation will
     * retrieve all sub-elements associated with this data type Put all subelements into a list and
     * save each individual element into the database
     */
    Arrays.stream(resources)
        .flatMap(r -> (ingestFromFile(r)).getAll().stream())
        .forEach(
            dataType -> {
              loaderConstants.getRepo(dataType.toString()).save(dataType);
              LOGGER.log(Level.INFO, "Saved: " + dataType);
            });
  }

  /**
   * Pulls out all test {@link resources} files from resources dir
   *
   * @return
   * @throws IOException
   */
  private Resource[] retrieveTestDataFiles() throws IOException {
    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    return resolver.getResources("classpath*:/testDatabase/*Container.xml");
  }
}
