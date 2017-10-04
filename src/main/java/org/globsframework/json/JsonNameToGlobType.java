package org.globsframework.json;

import org.globsframework.metamodel.GlobType;

public interface JsonNameToGlobType {
    GlobType getType(String type);
}
