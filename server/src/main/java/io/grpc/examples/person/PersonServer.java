package io.grpc.examples.person;

import com.nishant.person.Empty;
import com.nishant.person.PPerson;
import com.nishant.person.PPersonList;
import com.nishant.person.PersonServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PersonServer {
  private static final Logger logger = Logger.getLogger(PersonServer.class.getName());

  /* The port on which the server should run */
  private static final int port = 50051;
  private Server server;

  private void start() throws IOException {
    server = ServerBuilder.forPort(port)
        .addService(new PersonServer.PersonImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        PersonServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Await termination on the main thread since the grpc library uses daemon threads.
   */
  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws IOException, InterruptedException {
    final PersonServer server = new PersonServer();
    server.start();
    server.blockUntilShutdown();
  }

  private static final Map<Long, PPerson> P_PERSON_LIST = new HashMap<>(50);

  private class PersonImpl extends PersonServiceGrpc.PersonServiceImplBase {
    @Override
    public void addPerson(PPerson request, StreamObserver<PPerson> responseObserver) {
      Long id = (long) P_PERSON_LIST.size();
      PPerson reply = PPerson.newBuilder(request).setId(id).build();
      P_PERSON_LIST.put(id, reply);
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    @Override
    public void getPerson(PPerson request, StreamObserver<PPersonList> responseObserver) {
      PPersonList.Builder personListBuilder = PPersonList.newBuilder();
      PPerson reply = P_PERSON_LIST.get(request.getId());
      if (reply != null) {
        personListBuilder.addPperson(reply);
      }
      responseObserver.onNext(personListBuilder.build());
      responseObserver.onCompleted();
    }

    @Override
    public void getAllPerson(Empty request, StreamObserver<PPersonList> responseObserver) {
      PPersonList.Builder builder = PPersonList.newBuilder();

      for(Map.Entry<Long, PPerson> entry: P_PERSON_LIST.entrySet()) {
        builder.addPperson(entry.getValue());
      }
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
    }
  }

}
