package org.globsframework.json;

import org.globsframework.metamodel.GlobType;

interface JsonNameToGlobType {
  GlobType getType(String type);
}
