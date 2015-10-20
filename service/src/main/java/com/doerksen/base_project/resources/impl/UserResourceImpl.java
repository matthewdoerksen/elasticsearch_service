package com.doerksen.base_project.resources.impl;

import com.doerksen.base_project.dto.UserDto;
import com.doerksen.base_project.resources.UserResource;

public class UserResourceImpl implements UserResource {

    public UserResourceImpl() {}

    public UserDto getUser(long id) {
        return new UserDto(1, "Test", "User", "fake@fake.com");
    }
}
