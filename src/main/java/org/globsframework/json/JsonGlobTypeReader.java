package org.globsframework.json;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeBuilder;
import org.globsframework.metamodel.GlobTypeBuilderFactory;
import org.globsframework.model.Glob;

import javax.json.stream.JsonParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonGlobTypeReader extends AbstractJsonReader {
  public static final String NAME = "name";
  public static final String VERSION = "version";
  public static final String FIELD_NAME = "name";
  public static final String ANNOTATIONS = "annotations";
  public static final String FIELDS = "fields";

  public static final String FIELD_TYPE = "type";
  public static final String INT_TYPE = "int";
  public static final String STRING_TYPE = "string";
  public static final String BOOLEAN_TYPE = "boolean";
  public static final String DOUBLE_TYPE = "double";
  public static final String LONG_TYPE = "long";
  private final JsonGlobReader jsonGlobReader;

  public JsonGlobTypeReader(JsonParser jsonParser, JsonNameToGlobType nameToGlobType) {
    super(nameToGlobType, jsonParser);
    jsonGlobReader = new JsonGlobReader(jsonParser, nameToGlobType, true);
  }

  public GlobType readGlobType() {
    nextAndCheck(JsonParser.Event.START_OBJECT);
    //version
    nextAndCheck(JsonParser.Event.KEY_NAME);
    check(jsonParser.getString(), VERSION);
    nextAndCheck(JsonParser.Event.VALUE_NUMBER);
    int version = jsonParser.getInt();
    if (version == 1) {

      //type
      nextAndCheck(JsonParser.Event.KEY_NAME);
      check(jsonParser.getString(), NAME);
      nextAndCheck(JsonParser.Event.VALUE_STRING);
      String type = jsonParser.getString();
      GlobTypeBuilder builder = GlobTypeBuilderFactory.create(type);
      nextAndCheck(JsonParser.Event.KEY_NAME);
      if (jsonParser.getString().equals(ANNOTATIONS)) {
        nextAndCheck(JsonParser.Event.START_ARRAY);
        while (jsonParser.hasNext() && jsonParser.next() != JsonParser.Event.END_ARRAY) {
          builder.addAnnotation(jsonGlobReader.readGlob());
        }
        nextAndCheck(JsonParser.Event.KEY_NAME);
      }
      check(jsonParser.getString(), FIELDS);
      nextAndCheck(JsonParser.Event.START_ARRAY);
      while (jsonParser.hasNext() && jsonParser.next() == JsonParser.Event.START_OBJECT) {
        nextAndCheck(JsonParser.Event.KEY_NAME);
        check(jsonParser.getString(), FIELD_NAME);
        nextAndCheck(JsonParser.Event.VALUE_STRING);
        String name = jsonParser.getString();
        nextAndCheck(JsonParser.Event.KEY_NAME);
        check(jsonParser.getString(), FIELD_TYPE);
        nextAndCheck(JsonParser.Event.VALUE_STRING);
        String fieldType = jsonParser.getString();
        JsonParser.Event event = jsonParser.next();
        List<Glob> annotations = Collections.emptyList();
        if (event == JsonParser.Event.KEY_NAME) {
          check(jsonParser.getString(), ANNOTATIONS);
          nextAndCheck(JsonParser.Event.START_ARRAY);
          annotations = new ArrayList<>();
          while (jsonParser.hasNext() && jsonParser.next() != JsonParser.Event.END_ARRAY) {
            annotations.add(jsonGlobReader.readGlob());
          }
          nextAndCheck(JsonParser.Event.END_OBJECT);
        }

        switch (fieldType) {
          case INT_TYPE:
            builder.declareIntegerField(name, annotations);
            break;
          case STRING_TYPE:
            builder.declareStringField(name, annotations);
            break;
          case BOOLEAN_TYPE:
            builder.declareBooleanField(name, annotations);
            break;
          case DOUBLE_TYPE:
            builder.declareDoubleField(name, annotations);
            break;
          case LONG_TYPE:
            builder.declareLongField(name, annotations);
            break;
        }
      }

      //nextAndCheck(JsonParser.Event.END_OBJECT);
      return builder.get();
    }
    else {
      throw new RuntimeException("Version " + version + " not supported.");
    }
  }
}
