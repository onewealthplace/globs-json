package org.globsframework.json;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.fields.*;
import org.globsframework.model.Glob;
import org.globsframework.utils.Strings;

import javax.json.stream.JsonGenerator;

public class JsonGlobWriter {
  public static final String VERSION = "version";
  public static final String TYPE = "type";
  public static final String FIELDS = "fields";
  private GlobTypeToJsonName globTypeToJsonName;
  private JsonGenerator jsonGenerator;
  private jSonFieldValueVisitorConverter visitor;

  public JsonGlobWriter(GlobTypeToJsonName globTypeToJsonName, JsonGenerator jsonGenerator) {
    this.globTypeToJsonName = globTypeToJsonName;
    this.jsonGenerator = jsonGenerator;
    visitor = new jSonFieldValueVisitorConverter(jsonGenerator);
  }

  public void write(Glob glob) {
    jsonGenerator.writeStartObject();
    jsonGenerator.write(VERSION, 1);
    jsonGenerator.write(TYPE,
                        globTypeToJsonName.getName(glob.getType()));
    jsonGenerator.writeStartObject(FIELDS);
    for (Field field : glob.getType().getFields()) {
      field.safeVisit(visitor, glob.getValue(field));
    }
    jsonGenerator.writeEnd();
    jsonGenerator.writeEnd();

  }

  private static class jSonFieldValueVisitorConverter implements FieldValueVisitor {
    private JsonGenerator jsonGenerator;

    public jSonFieldValueVisitorConverter(JsonGenerator jsonGenerator) {

      this.jsonGenerator = jsonGenerator;
    }

    public void visitInteger(IntegerField field, Integer value) throws Exception {
      if (value != null){
        jsonGenerator.write(field.getName(), value);
      }
    }

    public void visitDouble(DoubleField field, Double value) throws Exception {
      if (value != null){
        jsonGenerator.write(field.getName(), value);
      }
    }

    public void visitString(StringField field, String value) throws Exception {
      if (value != null){
        jsonGenerator.write(field.getName(), value);
      }
    }

    public void visitBoolean(BooleanField field, Boolean value) throws Exception {
      if (value != null){
        jsonGenerator.write(field.getName(), value);
      }
    }

    public void visitLong(LongField field, Long value) throws Exception {
      if (value != null){
        jsonGenerator.write(field.getName(), value);
      }
    }

    public void visitBlob(BlobField field, byte[] value) throws Exception {
      throw new RuntimeException("conversion of byte to json not available.");
    }
  }
}
