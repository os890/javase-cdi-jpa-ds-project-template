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

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;
import org.os890.cdi.template.ConfigEntry;
import org.os890.cdi.template.ConfigRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Feature tests for {@link ConfigRepository} covering CRUD operations.
 *
 * <p>Boots a CDI SE container with full classpath scanning.
 * {@link TestEntityManagerProducer} specializes the production
 * {@link org.os890.cdi.template.EntityManagerProducer} to use an
 * in-memory HSQLDB database for isolation.</p>
 *
 * <p>Because the {@code @Transactional} annotation on the repository
 * wraps each method call in its own transaction, tests track entry
 * counts relative to the baseline rather than assuming an empty
 * database.</p>
 */
@EnableTestBeans
class ConfigRepositoryTest {

    @Inject
    private ConfigRepository configRepository;

    /**
     * Verifies that a saved {@link ConfigEntry} can be retrieved
     * and that its field values match what was persisted.
     */
    @Test
    void saveAndRetrieveByFieldValues() {
        ConfigEntry entry = new ConfigEntry("db.url", "jdbc:hsqldb:mem:test");
        configRepository.save(entry);

        List<ConfigEntry> all = configRepository.findAll();
        assertFalse(all.isEmpty(), "findAll should return at least one entry");

        ConfigEntry found = all.stream()
                .filter(e -> "db.url".equals(e.getEntryKey()))
                .findFirst()
                .orElse(null);

        assertNotNull(found, "Entry with key 'db.url' should be present");
        assertEquals("db.url", found.getEntryKey(), "Entry key should match");
        assertEquals("jdbc:hsqldb:mem:test", found.getValue(),
                "Entry value should match");
    }

    /**
     * Verifies that saving multiple entries increases the total count
     * returned by {@link ConfigRepository#findAll()} accordingly.
     */
    @Test
    void multipleEntries() {
        int baseline = configRepository.findAll().size();

        configRepository.save(new ConfigEntry("key.alpha", "value1"));
        configRepository.save(new ConfigEntry("key.beta", "value2"));
        configRepository.save(new ConfigEntry("key.gamma", "value3"));

        List<ConfigEntry> all = configRepository.findAll();
        assertEquals(baseline + 3, all.size(),
                "findAll should return three more entries than the baseline");

        assertTrue(all.stream().anyMatch(e -> "key.alpha".equals(e.getEntryKey())),
                "Entry with key 'key.alpha' should be present");
        assertTrue(all.stream().anyMatch(e -> "key.beta".equals(e.getEntryKey())),
                "Entry with key 'key.beta' should be present");
        assertTrue(all.stream().anyMatch(e -> "key.gamma".equals(e.getEntryKey())),
                "Entry with key 'key.gamma' should be present");
    }

    /**
     * Verifies that updating a previously saved {@link ConfigEntry}
     * persists the new value when re-read from the repository.
     */
    @Test
    void updateEntry() {
        int baseline = configRepository.findAll().size();

        ConfigEntry entry = new ConfigEntry("app.mode", "development");
        ConfigEntry saved = configRepository.save(entry);

        saved.setValue("production");
        configRepository.save(saved);

        List<ConfigEntry> all = configRepository.findAll();
        assertEquals(baseline + 1, all.size(),
                "Updating an entry should not create a duplicate");

        ConfigEntry updated = all.stream()
                .filter(e -> "app.mode".equals(e.getEntryKey()))
                .findFirst()
                .orElse(null);

        assertNotNull(updated, "Entry with key 'app.mode' should exist");
        assertEquals("production", updated.getValue(),
                "Value should reflect the update");
    }

    /**
     * Verifies that {@link ConfigRepository#count()} returns a value
     * consistent with the number of entries persisted.
     */
    @Test
    void countEntries() {
        long baseline = configRepository.count();

        configRepository.save(new ConfigEntry("count.one", "v1"));
        configRepository.save(new ConfigEntry("count.two", "v2"));

        assertEquals(baseline + 2, configRepository.count(),
                "count() should reflect the newly saved entries");
    }

    /**
     * Verifies that the {@code isTransient} flag becomes {@code false}
     * after an entity is persisted and the version is assigned.
     */
    @Test
    void savedEntryIsNotTransient() {
        ConfigEntry entry = new ConfigEntry("transient.key", "transient.value");
        assertTrue(entry.isTransient(),
                "New entry should be transient before save");

        ConfigEntry saved = configRepository.save(entry);
        assertFalse(saved.isTransient(),
                "Saved entry should no longer be transient");
    }
}
