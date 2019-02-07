import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class Server {

	private Vector<Client> clients;
	private Vector<Socket> sockets;
	private Vector<DataOutputStream> streams;
	private ServerSocket serv;
	private Socket client;
	private int capacity;
	private int nbConnectedClients;
	private int nbWaitingSocks;
	private int port;

	Server(int p, int c) {
		capacity = c;
		port = p;
		clients = new Vector<Client>(c);
		sockets = new Vector<Socket>();
		streams = new Vector<DataOutputStream>();
		for (int i = 0; i < c; i++) {
			Client tmpEcho = new Client(this);
			clients.add(tmpEcho);
			tmpEcho.start();
		}
		nbConnectedClients = 0;
		nbWaitingSocks = 0;
	}

	public Socket removeFirstSocket() {
		Socket ret = sockets.get(0);
		sockets.removeElementAt(0);
		return ret;
	}

	public void newConnect(DataOutputStream out) {
		nbConnectedClients++;
		nbWaitingSocks--;
		System.out.println(" Thread handled connection.");
		System.out.println("   * " + nbConnectedClients + " connected.");
		System.out.println("   * " + nbWaitingSocks + " waiting.");
		streams.add(out);
		writeAllButMe("*** New user on chat ***", out);
	}

	public void clientLeft(DataOutputStream out) {
		nbConnectedClients--;
		System.out.println(" Client left.");
		System.out.println("   * " + nbConnectedClients + " connected.");
		System.out.println("   * " + nbWaitingSocks + " waiting.");
		writeAllButMe("*** A user has left ***", out);
		streams.remove(out);
	}

	public void writeAllButMe(String s, DataOutputStream out) {
		try {
			for (int i = 0; i < nbConnectedClients; i++)
				if (streams.elementAt(i) != out)
					streams.elementAt(i).writeChars(s);
		} catch (IOException e) {
		}
	}

	public int stillWaiting() {
		return nbWaitingSocks;
	}

	public void run() {
		try {
			serv = new ServerSocket(port);
			while (true) {
				client = serv.accept();
				System.out.println("New connexion at server.");
				synchronized (this) {
					sockets.add(client);
					nbWaitingSocks++;
					this.notify();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}
}