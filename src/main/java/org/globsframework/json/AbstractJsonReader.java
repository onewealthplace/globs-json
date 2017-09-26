package org.globsframework.json;

import javax.json.stream.JsonParser;

public class AbstractJsonReader {
  final JsonParser jsonParser;
  final JsonNameToGlobType jsonNameToGlobType;

  public AbstractJsonReader(JsonNameToGlobType type, JsonParser parser) {
    jsonNameToGlobType = type;
    jsonParser = parser;
  }

  protected void check(String actual, String expected) {
    if (!actual.equals(expected)) {
      throw new RuntimeException(expected + " expected but got " + actual);
    }
  }

  protected JsonParser.Event nextAndCheck(JsonParser.Event expectedEvent) {
    if (jsonParser.hasNext()) {
      JsonParser.Event event = jsonParser.next();
      if (event != expectedEvent) {
        throw new RuntimeException("Got event " + event + " but expect " + expectedEvent);
      }
      return event;
    }
    throw new RuntimeException("EOF");
  }
}
