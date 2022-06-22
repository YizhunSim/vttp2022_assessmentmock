package vttp2022.assessmentmock;

import vttp2022.assessmentmock.Server.HttpServer;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {
        HttpServer server = new HttpServer(args);
        server.start();
    }
}
