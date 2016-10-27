package io.grpc.examples.person;

import com.nishant.person.Empty;
import com.nishant.person.PPerson;
import com.nishant.person.PPersonList;
import com.nishant.person.PersonServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PersonClient {
  private static final Logger LOGGER = Logger.getLogger(PersonClient.class.getSimpleName());

  private ManagedChannel channel;
  private PersonServiceGrpc.PersonServiceBlockingStub blockingStub;

  public PersonClient(String host, int port) {
    channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext(true)
        .build();
    blockingStub = PersonServiceGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String... args) throws InterruptedException {
    PersonClient client = new PersonClient("localhost", 50051);
    try {
      client.addPerson("nishant", "pathak");
      client.addPerson("ankur", "jain");
      client.getAllPersons();

      client.getById(1);
      client.getById(100);
    } finally {
      client.shutdown();
    }
  }

  private void getById(int id) {
    PPerson req = PPerson.newBuilder().setId(id).build();
    try {
      PPersonList reply = blockingStub.getPerson(req);
      System.out.println("got person of id: " + id + " " + reply);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void getAllPersons(){
    Empty empty = Empty.getDefaultInstance();
    try {
      PPersonList reply = blockingStub.getAllPerson(empty);
      System.out.println(reply);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void addPerson(String firstName, String lastName) {
    PPerson req = PPerson.newBuilder().setFirstName(firstName).setLastName(lastName).build();
    PPerson reply;
    try {
      reply = blockingStub.addPerson(req);
      System.out.println("got add Person Reply : " + reply.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
