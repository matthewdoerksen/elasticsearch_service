package com.doerksen.base_project;

import com.doerksen.base_project.resources.UserResource;
import com.doerksen.base_project.resources.impl.UserResourceImpl;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class BaseProject extends Application<BaseProjectConfiguration> {

    public static void main(String[] args) throws Exception {
        new BaseProject().run(args);
    }

    @Override
    public void run(BaseProjectConfiguration configuration, Environment environment) throws Exception {
        final UserResource userResource = new UserResourceImpl();
        environment.jersey().register(new UserResourceImpl());
    }
}
