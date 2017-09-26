package org.globsframework.json;

import org.globsframework.metamodel.GlobModel;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.annotations.AnnotationModel;
import org.globsframework.metamodel.impl.DefaultGlobModel;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.StringReader;

public class JsonGlobTypeReaderTest {

  @Test
  public void name() throws Exception {

    JsonParser parser = Json.createParser(new StringReader(JsonGlobTypeWriterTest.EXPECTED
                                                             .replace('\'', '"')));
    JsonGlobTypeReader reader = new JsonGlobTypeReader(parser, new ModelJsonNameToGlobType());
    GlobType globType = reader.readGlobType();

    Assert.assertEquals(JsonGlobType.TYPE.getName(), globType.getName());
    Assert.assertTrue(globType.getField("id").isKeyField());
    globType.getField("count");
    globType.getField("isPresent");
  }

  private static class ModelJsonNameToGlobType implements JsonNameToGlobType {
    GlobModel types;

    public ModelJsonNameToGlobType() {
      this.types = new DefaultGlobModel(AnnotationModel.model, JsonGlobType.TYPE);
    }

    public GlobType getType(String type) {
      return types.getType(type);
    }
  }
}