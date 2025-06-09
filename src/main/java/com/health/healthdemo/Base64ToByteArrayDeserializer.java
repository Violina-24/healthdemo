package com.health.healthdemo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Base64;

public class Base64ToByteArrayDeserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String base64 = p.getText();
        if (base64 == null || base64.isEmpty()) {
            return null;
        }
        String[] parts = base64.split(",");
        return Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);
    }
}
