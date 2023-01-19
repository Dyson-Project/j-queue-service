//package vn.unicloud.genericqueue.client;
//
//import io.grpc.stub.ClientCallStreamObserver;
//import io.grpc.stub.ClientResponseObserver;
//import org.apache.avro.Schema;
//import vn.unicloud.eventbus.protobuf.ConsumerEvent;
//import vn.unicloud.eventbus.protobuf.FetchRequest;
//import vn.unicloud.eventbus.protobuf.FetchResponse;
//import vn.unicloud.genericqueue.client.events.ChangeEventHeader;
//import vn.unicloud.genericqueue.client.events.Event;
//import vn.unicloud.genericqueue.client.events.EventParser;
//
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class PubSubEventObserver implements ClientResponseObserver<FetchRequest, FetchResponse> {
//    private PubSubApiClient client;
//    private String topicName;
//    private Schema schema;
//    private int eventCoundRequested;
//
//    private static final Logger logger = Logger.getLogger(PubSubEventObserver.class.getName());
//
//    public PubSubEventObserver(PubSubApiClient client, String topicName, Schema schema, int eventCoundRequested) {
//        this.client = client;
//        this.topicName = topicName;
//        this.schema = schema;
//        this.eventCoundRequested = eventCoundRequested;
//    }
//
//    @Override
//    public void onNext(FetchResponse value) {
//        logger.info("Next event: " + value.toString());
//        EventParser parser = new EventParser(schema);
//        List<ConsumerEvent> consumerEvents = value.getEventsList();
//        try {
//            for (ConsumerEvent consumerEvent : consumerEvents) {
//                Event event = parser.parse(consumerEvent);
//                logger.info("Event raw payload: " + event);
//                logger.info("Event replay ID: " + event.getReplayId());
//                ChangeEventHeader header = event.getHeader();
//                logger.info(header.getChangeType() + " operation on " + header.getEntityName() + " with record ID "
//                        + String.join(",", header.getRecordIds()));
//                logger.info("Changed fields: " + String.join(", ", header.getChangedFields()));
//            }
//        } catch (EventParser.EventParseException e) {
//            logger.log(Level.SEVERE, "Failed to parse message: " + e.getMessage(), e);
//            this.client.shutdown();
//        }
//    }
//
//    @Override
//    public void onError(Throwable t) {
//        logger.log(Level.SEVERE, "Subscribe/receive error: " + t.getMessage(), t);
//    }
//
//    @Override
//    public void onCompleted() {
//        logger.info("Done receiving events.");
//        this.client.shutdown();
//    }
//
//    @Override
//    public void beforeStart(ClientCallStreamObserver<FetchRequest> requestStream) {
//        requestStream.setOnReadyHandler(new Runnable() {
//            @Override
//            public void run() {
//                FetchRequest request = FetchRequest.newBuilder().setNumRequested(eventCoundRequested)
//                        .setTopicName(topicName).build();
//                requestStream.onNext(request);
//            }
//        });
//    }
//}
