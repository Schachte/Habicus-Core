package com.habicus.repository;

import static java.lang.Class.*;

import com.habicus.core.data.GoalRepository;
import com.habicus.core.data.UserRepository;
import com.habicus.repository.DataContainers.Container;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Launches at application boot-time to inject any user-defined test data and stores into test
 * database automatically
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger LOGGER = Logger.getLogger(Loader.class.getName());

  Map<String, JpaRepository> reposByName = new HashMap<>();

  @Autowired private UserRepository userRepo;

  @Autowired private GoalRepository goalRepo;

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
      exception.printStackTrace();
    }
    return null;
  }

  /**
   * Read in file from static resources directory
   *
   * @param fileResource
   * @return
   * @throws IOException
   * @throws JAXBException
   * @throws ClassNotFoundException
   */
  private Container ingestFromFile(Resource fileResource) {
    if (fileResource == null) {
      throw new NullPointerException("File Resource Is Invalid");
    }

    try {
      return parseFile(
          fileResource,
          forName(
              "com.habicus.repository.DataContainers."
                  + normalizeFileName(fileResource.getFilename())));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    LOGGER.log(Level.INFO, "Preparing to load data into database");

    /** BEGIN REPOSITORY MAP HERE */
    reposByName.put("User", userRepo);
    reposByName.put("Goal", goalRepo);

    try {
      loadTestContainers();
    } catch (IOException | JAXBException e) {
      LOGGER.log(Level.INFO, "Unable to store user data");
      e.printStackTrace();
    }
  }

  /**
   * Loads up any test data from resources and stores into embedded DB for test env
   *
   * @throws IOException
   * @throws JAXBException
   */
  private void loadTestContainers() throws IOException, JAXBException {
    LOGGER.log(Level.INFO, "User data table being added to database");
    Resource[] resources = retrieveTestDataFiles();

    List<? extends Container> containers =
        Arrays.stream(resources).map(this::ingestFromFile).collect(Collectors.toList());

    containers
        .stream()
        .map(Container.class::cast)
        .map(Container::getAll)
        .flatMap(Collection::stream)
        .collect(Collectors.toList())
        .forEach(
            dataType -> {
              reposByName.get(dataType.toString()).save(dataType);
              LOGGER.log(Level.INFO, "Saved: " + dataType);
            });
  }

  /**
   * Pulls out all test container files from static resources dir
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
