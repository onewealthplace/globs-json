package org.globsframework.json;

import org.junit.Assert;

import javax.json.*;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonTestUtils {

  static void checkSame(final String expected, final String actualJson) {
    JsonPatch diff = Json.createDiff(toJsonObject(expected),
                                     toJsonObject(actualJson));
    JsonArray values = diff.toJsonArray();
    StringWriter writer = new StringWriter();
    Json.createWriter(writer).writeArray(values);
    Assert.assertEquals("[]", writer.toString());
  }

  private static JsonObject toJsonObject(final String json) {
    JsonReader parser = Json.createReader(new StringReader(json.replace('\'', '"')));
    try {
      return parser.readObject();
    }
    catch (JsonParsingException e) {
      JsonLocation location = e.getLocation();
      long offset = location.getStreamOffset();
      throw new JsonParsingException(json.substring(0, (int)offset) + "\nof\n" + json, e, e.getLocation());
    }
  }
}
