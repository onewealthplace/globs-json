package org.globsframework.json;

import org.globsframework.metamodel.GlobType;
import org.globsframework.model.Glob;
import org.globsframework.model.format.GlobPrinter;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonParser;

import java.io.StringReader;

public class JsonGlobReaderTest {

  @Test
  public void name() throws Exception {
    JsonParser parser = Json.createParser(new StringReader(JsonGlobWriterTest.EXPECTED
    .replace('\'', '"')));
    JsonGlobReader reader = new JsonGlobReader(parser, new JsonNameToGlobType() {
      public GlobType getType(String type) {
        return JsonGlobType.TYPE;
      }
    }, false);
    Glob glob = reader.readGlob();

    Assert.assertEquals("id=1\n" +
                        "name=a json object name\n" +
                        "count=2\n" +
                        "value=3.14159\n" +
                        "isPresent=true\n", GlobPrinter.toString(glob));
  }
}