package org.globsframework.json;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.*;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;

import javax.json.stream.JsonParser;

public class JsonGlobReader extends AbstractJsonReader {

  private boolean ignoreFirstStartObject;

  public JsonGlobReader(JsonParser parser, JsonNameToGlobType type, boolean ignoreFirstStartObject) {
    super(type, parser);
    this.ignoreFirstStartObject = ignoreFirstStartObject;
  }

  Glob readGlob() {
    if (!ignoreFirstStartObject) {
      nextAndCheck(JsonParser.Event.START_OBJECT);
    }
    //version
    nextAndCheck(JsonParser.Event.KEY_NAME);
    check(jsonParser.getString(), JsonGlobWriter.VERSION);
    nextAndCheck(JsonParser.Event.VALUE_NUMBER);
    int version = jsonParser.getInt();

    if (version == 1) {
      //type
      nextAndCheck(JsonParser.Event.KEY_NAME);
      check(jsonParser.getString(), JsonGlobWriter.TYPE);
      nextAndCheck(JsonParser.Event.VALUE_STRING);
      String typeName = jsonParser.getString();

      GlobType type = jsonNameToGlobType.getType(typeName);
      MutableGlob mutableGlob = type.instantiate();
      nextAndCheck(JsonParser.Event.KEY_NAME);
      check(jsonParser.getString(), JsonGlobWriter.FIELDS);
      nextAndCheck(JsonParser.Event.START_OBJECT);
      JsonFieldReaderFieldVisitor visitor = new JsonFieldReaderFieldVisitor(mutableGlob, jsonParser);
      while (jsonParser.hasNext() && jsonParser.next() == JsonParser.Event.KEY_NAME) {
        String fieldName = jsonParser.getString();
        type.getField(fieldName).safeVisit(visitor);
      }
      nextAndCheck(JsonParser.Event.END_OBJECT);
      return mutableGlob;
    }
    else {
      throw new RuntimeException("Version " + version + " not supported." );
    }
  }

  private static class JsonFieldReaderFieldVisitor implements FieldVisitor {
    private final MutableGlob mutableGlob;
    private final JsonParser jsonParser;

    public JsonFieldReaderFieldVisitor(MutableGlob mutableGlob, JsonParser jsonParser) {
      this.mutableGlob = mutableGlob;
      this.jsonParser = jsonParser;
    }

    public void visitInteger(IntegerField field) throws Exception {
      jsonParser.next();
      mutableGlob.set(field, jsonParser.getInt());
    }

    public void visitDouble(DoubleField field) throws Exception {
      jsonParser.next();
      mutableGlob.set(field, jsonParser.getBigDecimal().doubleValue());
    }

    public void visitString(StringField field) throws Exception {
      jsonParser.next();
      mutableGlob.set(field, jsonParser.getString());
    }

    public void visitBoolean(BooleanField field) throws Exception {
      JsonParser.Event event = jsonParser.next();
      mutableGlob.set(field, event == JsonParser.Event.VALUE_TRUE);
    }

    public void visitLong(LongField field) throws Exception {
      jsonParser.next();
      mutableGlob.set(field, jsonParser.getLong());
    }

    public void visitBlob(BlobField field) throws Exception {

    }
  }
}
