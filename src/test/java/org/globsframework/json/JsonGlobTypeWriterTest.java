package org.globsframework.json;

import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.StringWriter;
import java.util.Collections;

public class JsonGlobTypeWriterTest {

  public static final String EXPECTED = "" +
                                        "{" +
                                        "  'version':1," +
                                        "  'name':'jsonGlobType'," +
                                        "  'fields':[" +
                                        "   {" +
                                        "    'name':'id'," +
                                        "    'type':'int'," +
                                        "    'annotations':[" +
                                        "      {" +
                                        "       'version':1," +
                                        "       'type':'KeyAnnotation'," +
                                        "       'fields':" +
                                        "         {'index':0}" +
                                        "      }]" +
                                        "   }," +
                                        "   {" +
                                        "    'name':'name'," +
                                        "    'type':'string'" +
                                        "    }," +
                                        "  {" +
                                        "   'name':'count'," +
                                        "   'type':'int'" +
                                        "  }," +
                                        "  {'name':'value'," +
                                        "   'type':'double'" +
                                        "  }," +
                                        "  {'name':'isPresent'," +
                                        "   'type':'boolean'" +
                                        "   }" +
                                        "  ]" +
                                        "}";

  @Test
  public void writeType() throws Exception {
    JsonGeneratorFactory factory = Json.createGeneratorFactory(Collections.emptyMap());
    StringWriter writer = new StringWriter();
    JsonGenerator generator = factory.createGenerator(writer);
    JsonGlobTypeWriter jsonGlobTypeWriter =
      new JsonGlobTypeWriter(new GlobTypeToJsonName() {
      }, generator);

    jsonGlobTypeWriter.write(JsonGlobType.TYPE);
    generator.close();
    JsonTestUtils.checkSame(EXPECTED, writer.toString());
  }
}