package vn.unicloud.genericqueue.server.utils.serealization;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.unicloud.genericqueue.avro.model.AvroHttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer {
    private static final Logger log = LoggerFactory.getLogger(AvroSerializer.class);

    public byte[] serialize(AvroHttpRequest request){
        DatumWriter<AvroHttpRequest> writer = new SpecificDatumWriter<>(AvroHttpRequest.class);
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder encoder = null;
        try {
            encoder = EncoderFactory.get().jsonEncoder(AvroHttpRequest.getClassSchema(), stream);
            writer.write(request, encoder);
            encoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    public byte[] serializeAvroHttpRequestBinary(AvroHttpRequest request) {
        DatumWriter<AvroHttpRequest> writer = new SpecificDatumWriter<>(AvroHttpRequest.class);
        byte[] data = new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encoder jsonEncoder = EncoderFactory.get()
            .binaryEncoder(stream, null);
        try {
            writer.write(request, jsonEncoder);
            jsonEncoder.flush();
            data = stream.toByteArray();
        } catch (IOException e) {
            log.error("Serialization error " + e.getMessage());
        }

        return data;
    }

    
}
