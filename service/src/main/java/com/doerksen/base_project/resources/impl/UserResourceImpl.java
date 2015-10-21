package com.doerksen.base_project.resources.impl;

import com.doerksen.base_project.dto.UserDto;
import com.doerksen.base_project.resources.UserResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserResourceImpl implements UserResource {

    // Dropwizard by default uses Logback behind the scenes, https://dropwizard.github.io/dropwizard/0.6.2/manual/core.html#logging
    private final Logger logger = LoggerFactory.getLogger(UserResourceImpl.class);

    public UserResourceImpl() {}

    public UserDto getUser(long id) {
        logger.warn("warning");     // this will be logged to console and file
        logger.error("error");      // also logged to console and file
        logger.info("info");        // logged only to console because of the log level override
        logger.debug("debug");      // not logged because of our console level override
        return new UserDto(1, "FirstName", "LastName", "fakeEmail@fakeMail.com");
    }
}
