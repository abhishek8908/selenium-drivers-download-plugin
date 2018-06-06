package com.adsingh.driver;

import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;

public interface IDriver {

    public IDriver getDriver() throws IOException, ConfigurationException;

    public void setDriverInSystemProperty();

}

