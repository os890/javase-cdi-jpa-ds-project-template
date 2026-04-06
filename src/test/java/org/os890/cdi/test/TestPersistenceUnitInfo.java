/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.os890.cdi.test;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

import javax.sql.DataSource;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static jakarta.persistence.spi.PersistenceUnitTransactionType.RESOURCE_LOCAL;

/**
 * Programmatic {@link PersistenceUnitInfo} for the test persistence unit.
 *
 * <p>Configures Hibernate against an in-memory HSQLDB database using
 * {@code create-drop} DDL so each test run starts with a clean schema.</p>
 */
public class TestPersistenceUnitInfo implements PersistenceUnitInfo {

    private final Properties properties = new Properties();

    /**
     * Initialises HSQLDB JDBC connection properties and Hibernate DDL settings.
     */
    public TestPersistenceUnitInfo() {
        properties.put("jakarta.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
        properties.put("jakarta.persistence.jdbc.url", "jdbc:hsqldb:mem:testdb");
        properties.put("jakarta.persistence.jdbc.user", "sa");
        properties.put("jakarta.persistence.jdbc.password", "");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
    }

    /**
     * Returns the name of the persistence unit.
     *
     * @return the persistence unit name {@code "testPU"}
     */
    @Override
    public String getPersistenceUnitName() {
        return "testPU";
    }

    /**
     * Returns the fully qualified class name of the persistence provider.
     *
     * @return the Hibernate persistence provider class name
     */
    @Override
    public String getPersistenceProviderClassName() {
        return HibernatePersistenceProvider.class.getName();
    }

    /**
     * Returns the scope annotation name for JPA 3.2.
     *
     * <p>JPA 3.2 method -- not yet in the interface with Hibernate 6.6 / jakarta.persistence 3.1.</p>
     *
     * @return {@code null} as no scope annotation is configured
     */
    // JPA 3.2 method — not yet in the interface with Hibernate 6.6 / jakarta.persistence 3.1
    public String getScopeAnnotationName() {
        return null;
    }

    /**
     * Returns the qualifier annotation names for JPA 3.2.
     *
     * <p>JPA 3.2 method -- not yet in the interface with Hibernate 6.6 / jakarta.persistence 3.1.</p>
     *
     * @return an empty list
     */
    // JPA 3.2 method — not yet in the interface with Hibernate 6.6 / jakarta.persistence 3.1
    public List<String> getQualifierAnnotationNames() {
        return emptyList();
    }

    /**
     * Returns the transaction type for this persistence unit.
     *
     * @return {@link PersistenceUnitTransactionType#RESOURCE_LOCAL}
     */
    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return RESOURCE_LOCAL;
    }

    /**
     * Returns the JTA data source.
     *
     * @return {@code null} as no JTA data source is used
     */
    @Override
    public DataSource getJtaDataSource() {
        return null;
    }

    /**
     * Returns the non-JTA data source.
     *
     * @return {@code null} as connection properties are specified via persistence properties
     */
    @Override
    public DataSource getNonJtaDataSource() {
        return null;
    }

    /**
     * Returns the list of mapping file names.
     *
     * @return a list containing {@code "META-INF/entities.xml"}
     */
    @Override
    public List<String> getMappingFileNames() {
        return asList("META-INF/entities.xml");
    }

    /**
     * Returns the list of JAR file URLs to scan for managed classes.
     *
     * @return an empty list
     */
    @Override
    public List<URL> getJarFileUrls() {
        return emptyList();
    }

    /**
     * Returns the root URL of the persistence unit.
     *
     * @return {@code null} as the root URL is not configured
     */
    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    /**
     * Returns the list of managed class names.
     *
     * @return an empty list; entities are declared in the mapping file
     */
    @Override
    public List<String> getManagedClassNames() {
        return emptyList();
    }

    /**
     * Returns whether unlisted classes should be excluded.
     *
     * @return {@code true} to exclude classes not listed in the mapping file
     */
    @Override
    public boolean excludeUnlistedClasses() {
        return true;
    }

    /**
     * Returns the shared cache mode for the persistence unit.
     *
     * @return {@link SharedCacheMode#UNSPECIFIED}
     */
    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.UNSPECIFIED;
    }

    /**
     * Returns the validation mode for the persistence unit.
     *
     * @return {@link ValidationMode#AUTO}
     */
    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    /**
     * Returns the persistence unit properties including JDBC and Hibernate settings.
     *
     * @return the configured properties
     */
    @Override
    public Properties getProperties() {
        return properties;
    }

    /**
     * Returns the persistence XML schema version.
     *
     * @return {@code "3.2"}
     */
    @Override
    public String getPersistenceXMLSchemaVersion() {
        return "3.2";
    }

    /**
     * Returns the class loader for this persistence unit.
     *
     * @return the class loader of this class
     */
    @Override
    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    /**
     * Adds a class transformer to the persistence unit.
     *
     * <p>This implementation is a no-op as class transformation is not required for tests.</p>
     *
     * @param transformer the class transformer to add
     */
    @Override
    public void addTransformer(ClassTransformer transformer) {
    }

    /**
     * Returns a new temporary class loader.
     *
     * @return {@code null} as no temporary class loader is needed
     */
    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }
}
