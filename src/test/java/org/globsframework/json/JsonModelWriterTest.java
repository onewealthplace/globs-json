package org.globsframework.json;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.annotations.AnnotationModel;
import org.globsframework.metamodel.impl.DefaultGlobTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.io.StringWriter;

public class JsonModelWriterTest {

    public static final String EXPECTED = "" +
          "{'types':" +
          "  [" +
          "    {" +
          "      'version':1," +
          "      'name':\"A\"," +
          "      'fields':" +
          "        [" +
          "         {" +
          "          'name':'ID'," +
          "          'type':'int'," +
          "           'annotations':[{'version':1,'type':'KeyAnnotation','fields':{'index':0}}]" +
          "         }," +
          "         {" +
          "          'name':'NAME_A'," +
          "          'type':'string'" +
          "         }" +
          "        ]" +
          "    }," +
          "    {" +
          "      'version':1," +
          "      'name':'B','fields':[{'name':'ID','type':'int'," +
          "       'annotations':[{'version':1,'type':'KeyAnnotation','fields':{'index':0}}]}," +
          "       {'name':'NAME_B','type':'string'}]" +
          "    }" +
          "  ]" +
          "}";

    @Test
    public void testWrite() throws Exception {
        GlobType globType1 = new DefaultGlobTypeBuilder("A")
              .addIntegerKey("ID")
              .addStringField("NAME_A")
              .get();
        GlobType globType2 = new DefaultGlobTypeBuilder("B")
              .addIntegerKey("ID")
              .addStringField("NAME_B")
              .get();

        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        JsonGlobTypeWriter jsonGlobTypeWriter = new JsonGlobTypeWriter(GlobType::getName, generator);

        generator.writeStartObject();
        generator.writeStartArray("types");
        jsonGlobTypeWriter.write(globType1);
        jsonGlobTypeWriter.write(globType2);
        generator.writeEnd();
        generator.writeEnd();
        generator.close();

        JsonTestUtils.checkSame(EXPECTED, writer.toString());
    }

    @Test
    public void testRead() {
        JsonParser parser = Json.createParser(new StringReader(EXPECTED.replace('\'', '"')));
        JsonGlobTypeReader jsonGlobTypeReader = new JsonGlobTypeReader(parser, type -> AnnotationModel.model.getType(type));

        JsonParser.Event event;
        event = parser.next();
        event = parser.next();
        String string = parser.getString();
        Assert.assertEquals("types", string);
        event = parser.next();

        GlobType type1 = jsonGlobTypeReader.readGlobType();
        GlobType type2 = jsonGlobTypeReader.readGlobType();

        Assert.assertEquals("A", type1.getName());
        type1.getField("ID");
        Assert.assertEquals("B", type2.getName());
        type2.getField("ID");
    }
}
