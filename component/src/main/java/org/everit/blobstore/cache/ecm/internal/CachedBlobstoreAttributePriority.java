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

/**
 * Constants of Cached BlobStore attribute priority.
 */
public final class CachedBlobstoreAttributePriority {

  public static final int P1_SERVICE_DESCRIPTION = 1;

  public static final int P2_WRAPPED_BLOBSTORE = 2;

  public static final int P3_CACHE = 3;

  public static final int P4_DEFAULT_CHUNK_SIZE = 4;

  public static final int P5_TRANSACTION_MANAGER = 5;

  private CachedBlobstoreAttributePriority() {
  }
}
