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

package org.os890.cdi.template;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * CDI producer for transaction-scoped {@link EntityManager} instances.
 *
 * <p>Obtains an {@link EntityManagerFactory} for the {@code prodPU}
 * persistence unit via DeltaSpike's {@link PersistenceUnitName} injection,
 * then exposes one {@link EntityManager} per DeltaSpike transaction.</p>
 */
@ApplicationScoped
public class EntityManagerProducer {

    @Inject
    @PersistenceUnitName("prodPU")
    private EntityManagerFactory entityManagerFactory;

    /**
     * Creates and returns an {@link EntityManager} for the current transaction.
     *
     * @return a new entity manager backed by the production persistence unit
     */
    @Produces
    @Default //needed for weld in wildfly8 - see WELD-1620
    @TransactionScoped
    protected EntityManager exposeEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Closes the entity manager when the transaction ends.
     *
     * @param entityManager the entity manager to close
     */
    protected void onTransactionEnd(@Disposes @Default /*needed for weld in wildfly8 - see WELD-1620*/ EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
