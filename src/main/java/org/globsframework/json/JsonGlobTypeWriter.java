package org.globsframework.json;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.annotations.FieldNameAnnotationType;
import org.globsframework.metamodel.fields.*;
import org.globsframework.model.Glob;

import javax.json.stream.JsonGenerator;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class JsonGlobTypeWriter {
  final GlobTypeToJsonName globTypeToJsonName;
  private JsonGenerator jsonGenerator;
  private final JsonGlobWriter jsonGlobWriter;
  private final JsonWriterFieldVisitor fieldType;

  public JsonGlobTypeWriter(GlobTypeToJsonName globTypeToJsonName,
                            JsonGenerator jsonGenerator) {
    this.globTypeToJsonName = globTypeToJsonName;
    this.jsonGenerator = jsonGenerator;
    jsonGlobWriter = new JsonGlobWriter(this.globTypeToJsonName, jsonGenerator);
    fieldType = new JsonWriterFieldVisitor(jsonGenerator);
  }

  public void write(GlobType type) {
    jsonGenerator.writeStartObject();
    jsonGenerator.write(JsonGlobTypeReader.VERSION, 1);
    jsonGenerator.write(JsonGlobTypeReader.NAME, globTypeToJsonName.getName(type));
    writeAnnotations(jsonGenerator, jsonGlobWriter, type.listAnnotations().stream());

    jsonGenerator.writeStartArray(JsonGlobTypeReader.FIELDS);
    for (Field field : type.getFields()) {
      jsonGenerator.writeStartObject();
      jsonGenerator.write(JsonGlobTypeReader.FIELD_NAME, field.getName());
      field.safeVisit(fieldType);
      writeAnnotations(jsonGenerator, jsonGlobWriter,
                       field.listAnnotations().stream()
                         .filter(glob -> glob.getType() != FieldNameAnnotationType.TYPE ||
                                         !glob.get(FieldNameAnnotationType.NAME).equals(field.getName())));
      jsonGenerator.writeEnd(); // field
    }
    jsonGenerator.writeEnd(); //fields
    jsonGenerator.writeEnd(); //type
  }

  private void writeAnnotations(JsonGenerator generator, JsonGlobWriter jsonGlobWriter, Stream<Glob> annotations) {
    //order for test
    CallAtFirstAndEnd<Glob> action = new CallAtFirstAndEnd<>(
      () -> generator.writeStartArray(JsonGlobTypeReader.ANNOTATIONS),
      generator::writeEnd,
      jsonGlobWriter::write);

    annotations.sorted(Comparator.comparing(g -> g.getType().getName())).forEach(action);

    action.end();
    // annotations
  }

  interface Functor {
    void call();
  }

  static class CallAtFirstAndEnd<T> implements Consumer<T> {
    boolean firstCall = true;
    final Functor first;
    final Functor end;
    final Consumer<T> consumer;

    public CallAtFirstAndEnd(Functor first, Functor end, Consumer<T> consumer) {
      this.first = first;
      this.end = end;
      this.consumer = consumer;
    }

    void end() {
      if (!firstCall) {
        end.call();
      }
    }

    public void accept(T t) {
      if (firstCall) {
        first.call();
        firstCall = false;
      }
      consumer.accept(t);
    }
  }

  private static class JsonWriterFieldVisitor implements FieldVisitor {
    private JsonGenerator jsonGenerator;
    private static String TYPE_FIELD_NAME = JsonGlobTypeReader.FIELD_TYPE;

    public JsonWriterFieldVisitor(JsonGenerator jsonGenerator) {
      this.jsonGenerator = jsonGenerator;
    }

    public void visitInteger(IntegerField field) throws Exception {
      jsonGenerator.write(TYPE_FIELD_NAME, JsonGlobTypeReader.INT_TYPE);
    }

    public void visitDouble(DoubleField field) throws Exception {
      jsonGenerator.write(TYPE_FIELD_NAME, JsonGlobTypeReader.DOUBLE_TYPE);
    }

    public void visitString(StringField field) throws Exception {
      jsonGenerator.write(TYPE_FIELD_NAME, JsonGlobTypeReader.STRING_TYPE);
    }

    public void visitBoolean(BooleanField field) throws Exception {
      jsonGenerator.write(TYPE_FIELD_NAME, JsonGlobTypeReader.BOOLEAN_TYPE);
    }

    public void visitLong(LongField field) throws Exception {
      jsonGenerator.write(TYPE_FIELD_NAME, JsonGlobTypeReader.LONG_TYPE);
    }

    public void visitBlob(BlobField field) throws Exception {
      throw new RuntimeException("Blob not supported.");
    }
  }
}
