package de.gwdg.portlet.computecloud.ui.util.json;

/**
 * @author sthomas@gwdg.de
 */
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Logger;


public class JacksonReader {
    private static final Logger LOGGER = Logger.getLogger(JacksonReader.class.getName());

    private JsonNode jsonObject;

    public JacksonReader(String jsonString) throws JsonParseException,
	    IOException {
	ObjectMapper mapper = new ObjectMapper();
	JsonFactory factory = mapper.getJsonFactory();
	this.jsonObject = null;
	com.fasterxml.jackson.core.JsonParser jp;
	jp = factory.createJsonParser(jsonString);
	this.jsonObject = mapper.readTree(jp);
    }

    /**
     * 
     * @return JsonNode from given json string in constructor
     */
    public JsonNode getJsonNode() {
	return this.jsonObject;
    }

    public static void main(String[] args) {
	// TODO Auto-generated method stub

    }

}
