package com.habicus.repository;

import com.habicus.core.data.UserRepository;
import com.habicus.repository.DataContainers.UserContainer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Launches at application boot-time to inject any user-defined test data and stores
 * into test database automatically
 */
@Component
public class Loader implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  private UserRepository userRepo;

  private static final Logger LOGGER = Logger.getLogger(Loader.class.getName());

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    LOGGER.log(Level.INFO, "Preparing to load data into database");

    try {
      loadAndStoreUserData();
    } catch (IOException | JAXBException e) {
      LOGGER.log(Level.INFO, "Unable to store user data");
      e.printStackTrace();
    }
  }

  /**
   * Loads up any test data from resources and stores into embedded DB for test env
   * @throws IOException
   * @throws JAXBException
   */
  private void loadAndStoreUserData() throws IOException, JAXBException {
    LOGGER.log(Level.INFO, "User data table being added to database");
    UserContainer userData = (UserContainer) ingestFromFile("testDatabase/users.xml");
    userData.getUsers().stream().forEach(userRepo::save);
  }

  /**
   * Read in file from static resources dir
   * @param fileName
   * @return
   * @throws IOException
   * @throws JAXBException
   */
  private Object ingestFromFile(String fileName) throws IOException, JAXBException {
   Resource resource =  new ClassPathResource(fileName);
   return parseFile(resource.getFile());
  }

  /**
   * Takes input file from disk and parsed out contents by marshaling XML -> POJO
   * @param inputFile
   * @return
   * @throws JAXBException
   */
  private Object parseFile(File inputFile) throws JAXBException {
    JAXBContext jaxbContext = JAXBContext.newInstance(UserContainer.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return jaxbUnmarshaller.unmarshal(inputFile);
  }
}
