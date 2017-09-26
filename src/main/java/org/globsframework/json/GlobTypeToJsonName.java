package org.globsframework.json;

import org.globsframework.metamodel.GlobType;

interface GlobTypeToJsonName {
  default String getName(GlobType type) {
    return type.getName();
  }
}
