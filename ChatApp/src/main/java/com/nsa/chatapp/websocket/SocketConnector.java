package com.nsa.chatapp.websocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;

public class SocketConnector implements Runnable {

	
	@Autowired
	private SessionRegistry sessionRegistry;
	
    private void acceptRequest() throws Exception {
        int portNumber = 4000;
        PrintWriter out;
        BufferedReader in;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Socket socket = new Socket();
        
        while (true) {
            try {
                //zasto zoves konstruktor     

                socket = serverSocket.accept();

                
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                char cb[] = new char[10000];
                //String str = new String();
                int procitaniKarakteri = in.read(cb);
                String inputBuffer = new String(cb, 0, procitaniKarakteri);
                int inputbufferSize = inputBuffer.length();
                out.write("HTTP/1.1 200 OK\nAccess-Control-Allow-Origin:*\n\nYdravo Caix");
                out.flush();
                System.out.println(cb);
//
//                HTTPRequest currentRequest = new HTTPRequest(inputBuffer);
//                if (currentRequest.getHeader().url.endsWith(".php")|| currentRequest.getHeader().url.endsWith(".html") ) {
//                    System.out.println("JESTE PHP");
//                    new PHPProxy(currentRequest.getHeader().getUrl(), out);
//                    //writeImage(currentRequest, out,socket.getOutputStream());
//                } else {
//                    System.out.println("NIJE PHP");
//                    new HTTPResponse(currentRequest.getHeader().getUrl(), socket.getOutputStream());
//                    //new PHPProxy("url jedne stranice", out);
//                }

                socket.close();
            } catch (Exception e) {
                socket.close();
                System.out.println("exception in the main thread maybe");
                System.out.println(e);
            }

        }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Chat Server je sada startovan");
			ChatServer.start(new String[]{"aaa",""});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
