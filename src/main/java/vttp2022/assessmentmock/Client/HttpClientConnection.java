package vttp2022.assessmentmock.Client;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpClientConnection {

  // private String user;
	private Socket sock;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;

  public HttpClientConnection(Socket sock){
    // this.user = user;
    this.sock = sock;
  }

  public void start() {

		try{
			initializeStreams(sock);
			Console cons = System.console();

			String input = cons.readLine(">");
			write(input);

			String response = read();
			System.out.println(response);

			close();
			sock.close();
		} catch (IOException ex){
			System.err.println(ex);
			ex.printStackTrace();
		}
  }

 private void initializeStreams(Socket sock) throws IOException {
		os = sock.getOutputStream();
		oos = new ObjectOutputStream(os);
		is = sock.getInputStream();
		ois = new ObjectInputStream(is);
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
}
