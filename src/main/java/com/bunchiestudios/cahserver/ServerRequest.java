package com.bunchiestudios.cahserver;

import com.bunchiestudios.cahserver.database.DataManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by rdelfin on 8/27/16.
 */
public abstract class ServerRequest {
    private DataManager mgr;
    private JsonSchema reqSchema;
    private JsonSchema resSchema;
    private JsonSchemaFactory schemaFactory;

    public ServerRequest(DataManager mgr, String reqSchemaFile, String resSchemaFile) throws ProcessingException, IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.mgr = mgr;
            schemaFactory = JsonSchemaFactory.byDefault();
            URL reqSchemaURL = getClass().getClassLoader().getResource(reqSchemaFile);
            URL resSchemaURL = getClass().getClassLoader().getResource(resSchemaFile);
            reqSchema = schemaFactory.getJsonSchema(objectMapper.readTree(reqSchemaURL));
            resSchema = schemaFactory.getJsonSchema(objectMapper.readTree(resSchemaURL));
        } catch (IOException e) {
            System.err.println("Error opening schema files! Check your paths.");
            throw e;
        }
    }

    protected DataManager getMgr() { return mgr; }

    public abstract JSONObject perform(JSONObject message);
    public abstract RequestIdentifier getIdentifier();

    protected boolean requestValid(JSONObject req) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(req.toString());
            ProcessingReport report = reqSchema.validate(rootNode);
            return report.isSuccess();
        } catch(IOException e) {
            return false;
        } catch(ProcessingException e) {
            return false;
        }
    }

    protected boolean responseValid(JSONObject res) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(res.toString());
            ProcessingReport report = resSchema.validate(rootNode);
            return report.isSuccess();
        } catch(IOException e) {
            return false;
        } catch(ProcessingException e) {
            return false;
        }
    }
}
