package org.globsframework.json;

import org.globsframework.model.MutableGlob;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;

public class JsonGlobWriterTest {

  public static final String EXPECTED = "" +
                                        "{ 'version':1," +
                                        " 'type':'jsonGlobType'," +
                                        " 'fields':" +
                                        " {" +
                                        "  'id':1, " +
                                        "  'name':'a json object name'," +
                                        "  'count':2," +
                                        "  'value':3.14159," +
                                        "  'isPresent': true" +
                                        " }" +
                                        "}";

  @Test
  public void writeGlob() throws Exception {
    MutableGlob glob = JsonGlobType.TYPE.instantiate()
      .set(JsonGlobType.ID, 1)
      .set(JsonGlobType.COUNT, 2)
      .set(JsonGlobType.NAME, "a json object name")
      .set(JsonGlobType.VALUE, 3.14159)
      .set(JsonGlobType.IS_PRESENT, Boolean.TRUE);

    StringWriter actual = new StringWriter();
    JsonGenerator generator = Json.createGenerator(actual);
    JsonGlobWriter jsonGlobWriter =
      new JsonGlobWriter(new GlobTypeToJsonName() {
      }, generator);

    jsonGlobWriter.write(glob);
    generator.close();
    JsonTestUtils.checkSame(EXPECTED,
                            actual.toString());

  }
}