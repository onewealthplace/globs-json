package org.globsframework.json;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.KeyField;
import org.globsframework.metamodel.fields.BooleanField;
import org.globsframework.metamodel.fields.DoubleField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.metamodel.fields.StringField;

public class JsonGlobType {
  public static GlobType TYPE;

  @KeyField
  public static IntegerField ID;

  public static StringField NAME;

  public static IntegerField COUNT;

  public static DoubleField VALUE;

  public static BooleanField IS_PRESENT;

  static {
    GlobTypeLoaderFactory.createAndLoad(JsonGlobType.class);
  }
}
