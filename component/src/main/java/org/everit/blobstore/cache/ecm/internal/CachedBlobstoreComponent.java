/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.blobstore.cache.ecm.internal;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.everit.blobstore.Blobstore;
import org.everit.blobstore.cache.CachedBlobstore;
import org.everit.blobstore.cache.ecm.CachedBlobstoreConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link Blobstore} interface based on {@link CachedBlobstore}.
 */
@ExtendComponent
@Component(componentId = CachedBlobstoreConstants.SERVICE_FACTORYPID_CACHE_BLOBSTORE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Blobstore Cache Wrapper",
    description = "Wraps a Blobstore and caches its content. "
        + "The Blobstore implementation is org.everit.blobstore.cache.CachedBlobstore.")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = CachedBlobstoreConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = CachedBlobstoreAttributePriority.P1_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualServices(@ManualService(Blobstore.class))
public class CachedBlobstoreComponent {

  private Map<List<Byte>, byte[]> cache;

  private int defaultChunkSize;

  private ServiceRegistration<Blobstore> serviceRegistration;

  private TransactionManager transactionManager;

  private Blobstore wrappedBlobstore;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<CachedBlobstoreComponent> componentContext) {
    CachedBlobstore cachedBlobstore =
        new CachedBlobstore(wrappedBlobstore, cache, defaultChunkSize, transactionManager);

    Dictionary<String, Object> serviceProperties =
        new Hashtable<String, Object>(componentContext.getProperties());
    serviceRegistration =
        componentContext.registerService(Blobstore.class, cachedBlobstore, serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = CachedBlobstoreConstants.ATTR_CACHE_TARGET, defaultValue = "",
      attributePriority = CachedBlobstoreAttributePriority.P3_CACHE,
      label = "Cache (Map) OSGi filter",
      description = "OSGi Service filter expression for Map instance.")
  public void setCache(final Map<List<Byte>, byte[]> cache) {
    this.cache = cache;
  }

  @IntegerAttribute(attributeId = CachedBlobstoreConstants.ATTR_DEFAULT_CHUNK_SIZE,
      defaultValue = CachedBlobstoreConstants.DEFAULT_CHUNK_SIZE,
      priority = CachedBlobstoreAttributePriority.P4_DEFAULT_CHUNK_SIZE,
      label = "Default chunk size",
      description = "The default chunk size that the Blobstore will use to store the data in "
          + "cache.")
  public void setDefaultChunkSize(final int defaultChunkSize) {
    this.defaultChunkSize = defaultChunkSize;
  }

  @ServiceRef(attributeId = CachedBlobstoreConstants.ATTR_TRANSACTION_MANAGER_TARGET,
      defaultValue = "",
      attributePriority = CachedBlobstoreAttributePriority.P5_TRANSACTION_MANAGER,
      label = "Transaction Manager OSGi filter",
      description = "OSGi Service filter expression for TransactionManager instance.")
  public void setTransactionManager(final TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @ServiceRef(attributeId = CachedBlobstoreConstants.ATTR_WRAPPED_BLOBSTORE_TARGET,
      defaultValue = "", attributePriority = CachedBlobstoreAttributePriority.P2_WRAPPED_BLOBSTORE,
      label = "Wrapped Blobstore OSGi filter",
      description = "OSGi Service filter expression for wrapped Blobstore instance.")
  public void setWrappedBlobstore(final Blobstore wrappedBlobstore) {
    this.wrappedBlobstore = wrappedBlobstore;
  }

}
