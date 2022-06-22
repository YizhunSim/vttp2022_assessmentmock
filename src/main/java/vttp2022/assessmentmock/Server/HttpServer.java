package vttp2022.assessmentmock.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
  private int port = 3000;
  private String repoDir = "./target";

  private ServerSocket serverSocket;
  private Socket sock;

  private final String portExpression = "--port";
  private final String docRootExpression = "--docRoot";

  public HttpServer(String[] args){
    if (args.length > 0){
      List<String> argsList = new ArrayList<>(Arrays.asList(args));
      if (argsList.contains(portExpression)){
        //java -cp ./myserver.jar --port 8080
        int portIndex = argsList.indexOf(portExpression) + 1;
        this.port = Integer.parseInt(args[portIndex]);
      }
      if (argsList.contains(docRootExpression)){
        //java -cp ./myserver.jar --docRoot ./target:/opt/tmp/www
        int docRootIndex = argsList.indexOf(docRootExpression) + 1;
        this.repoDir = args[docRootIndex].replace(":","");
      }
    }

    System.out.println("HttpServer - PORT: "+ this.port + " REPODIR: "+ this.repoDir);
  }

  public void start(){
    try {
      serverSocket = new ServerSocket(this.port);
      ExecutorService threadPool = Executors.newFixedThreadPool(3);

      while (true){
        System.out.println("Waiting for client connection");
        sock = serverSocket.accept();

        System.out.println("Starting client connection handler");
        // Create a repository
        Repository repo = new Repository(this.repoDir);
        Session session = new Session(repo, sock);

        threadPool.execute(session);
      }
    } catch (IOException ex){
      System.err.println("Server error, exiting");
      ex.printStackTrace();
    }
  }

}
