package org.example.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static java.util.Objects.nonNull;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        String sourceName = nonNull(name) ? name : resource.getResource().getFilename();
        return new PropertiesPropertySource(sourceName, propertiesFromYaml);
    }

    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException e) {
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException)
                throw (FileNotFoundException) e.getCause();
            throw e;
        }
    }
}
