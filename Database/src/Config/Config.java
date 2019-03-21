package Config;

import javax.xml.bind.PropertyException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Config {
    private Properties properties = new Properties();
    private String path = System.getProperty("user.home") + File.separator  + "trader_bobs_config.properties";

    void generateConfiguration(String ip, String username, String password) throws IOException {
        properties.setProperty("ip", ip);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        properties.store(new FileOutputStream(path), null);
    }

    public String getConfig(String key) throws IOException {
        properties.load(new FileInputStream(path));
        return properties.getProperty(key);
    }
}
