package vttp2022.assessmentmock.Client;

import java.io.IOException;
import java.net.Socket;

public class Main {
  public static void main(String[] args) throws IOException{
    String terms[] = args[0].split(":");
		String host = terms[0]; // localhost
    int port = 80; // default port
    if (terms.length > 0){
      port = Integer.parseInt(terms[1]);
    }

		System.out.printf("Connecting to %s on port %d ...", host, port);

		Socket sock = new Socket(host, port);
    HttpClientConnection client = new HttpClientConnection(sock);

    client.start();
  }
}
