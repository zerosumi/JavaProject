package chatsys.server;

import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	Socket s;

	protected ServerThread(Socket s) {
		this.s = s;
	}

	public void run() {
		try {
			new ServerController(s).handle();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
