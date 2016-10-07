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

/**
 * Created by rdelfin on 8/27/16.
 */
public abstract class ServerRequest {
    private DataManager mgr;
    private JsonSchema reqSchema;
    private JsonSchema resSchema;
    private JsonSchemaFactory schemaFactory;

    public ServerRequest(DataManager mgr, String reqSchemaFile, String resSchemaFile) throws ProcessingException {
        this.mgr = mgr;
        schemaFactory = JsonSchemaFactory.byDefault();
        reqSchema = schemaFactory.getJsonSchema(reqSchemaFile);
        resSchema = schemaFactory.getJsonSchema(resSchemaFile);
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
