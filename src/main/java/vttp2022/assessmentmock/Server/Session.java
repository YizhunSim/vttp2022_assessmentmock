package vttp2022.assessmentmock.Server;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Session implements Runnable{
  private Repository repository;
  private Socket sock;
  private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

  private final String GET = "GET";
  private final String SUCCESS200 = "HTTP/1.1 200 OK\r\n\r\n";
  private final String METHODNOTALLOWED405 = "HTTP/1.1 405 Method Not Allowed\r\n\r\n";
  private final String NOTFOUND404 = "HTTP/1.1 404 Not Found\r\n\r\n";

  public Session(Repository repo, Socket sock){
    this.repository = repo;
    this.sock = sock;
  }

  public void start() throws IOException{
    initializeStreams(sock);

    // GET<space>/index.html<space>HTTP/1.1
    String request = read();

    System.out.printf("Received request: %s\n", request);

    // Perform some operation on the request
    request = parseResponseToString(request);

    //HTTP/1.1<space>200<space>OK\r\n
    // \r\n
    // <contents of index.html in bytes>
    System.out.printf("Response to be send to client: %s\n", request);

    write(request);

    close();
  }

  private void initializeStreams(Socket sock) throws IOException{
    // The order of creating the stream is important
		// we are opening input stream first then output stream
		// on the client we should open output stram first followed by input stream
		is = sock.getInputStream();
		ois = new ObjectInputStream(is);
		os = sock.getOutputStream();
		oos = new ObjectOutputStream(os);
  }

  private String read() throws IOException {
    return ois.readUTF();
  }

  private void write(String out) throws IOException {
    oos.writeUTF(out);
    oos.flush();
  }

  private void close() throws IOException {
    is.close();
    os.close();
  }


   private String parseResponseToString(String request) throws IOException{
    // GET<space>/index.html<space>HTTP/1.1
    String[] requestTerms = request.split(" ");
    String restAPICommand = requestTerms[0];
    String resourceFilePath = requestTerms[1];
    String restProtocol = requestTerms[2];
    String response = "";
    switch (restAPICommand){
      case GET:
        response = getResponse(restAPICommand, resourceFilePath, restProtocol);
        break;

      default:
        response = METHODNOTALLOWED405 + restAPICommand + " not supported\r\n";
    }
    return response;
  }

  private String getResponse(String restCommand, String resourcePath, String httpProtocol){
    String result = "";
    String extension = "";
    boolean isResouceFound = false;
    // System.out.println("getResponse - restCommand: " + restCommand);
    // System.out.println("getResponse - resourcePath: " + resourcePath);
    // System.out.println("getResponse - httpProtocol: " + httpProtocol);
    int index = resourcePath.lastIndexOf('.');
    System.out.println("INDEX: " +index);
    if (index > 0) {
      extension = resourcePath.substring(index);
      System.out.println("extension: " +extension);
    }

    if (resourcePath == "/"){
      resourcePath = "/index.html";
    }

    String resourceAbsolutePath = this.repository.getRepositoryAbsolutePath(resourcePath);

    if (this.repository.isPathExists() && this.repository.isPathDirectory() && this.repository.isPathReadable()){
      if (extension.equals(".png")){
        if (isResoucePathFound(resourcePath)){
          isResouceFound = true;
          result = SUCCESS200 + "Content-Type: image/png\r\n\r\n" + parseResourceContentsToBytes(resourceAbsolutePath).toString();
        }
      }
      else{
        if (isResoucePathFound(resourcePath)){
            isResouceFound = true;
            result = SUCCESS200 + parseResourceContentsToBytes(resourceAbsolutePath).toString();
        }
      }
    }

    if (!isResouceFound){
      result = NOTFOUND404 + resourcePath + " not found\r\n";
    }
    return result;
  }

  private boolean isResoucePathFound(String resourcePath){
     for (File file : this.repository.retrieveFiles()){
          if (resourcePath.substring(1).equals(file.getName())){
            return true;
          }
      }
      return false;
  }

  private byte[] parseResourceContentsToBytes(String path){
    try{
      byte[] array = Files.readAllBytes(Paths.get(path));
      if (array != null){
        return array;
      } else{
        return null;
      }
    } catch(IOException ex){
        System.out.println(ex.getMessage());
        return null;
    }
  }

  @Override
  public void run() {
      try{
        start();
      } catch(IOException ex){
        ex.printStackTrace();
      }
  }
}
