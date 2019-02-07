import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class Client extends Thread {

	private BufferedReader inchan;
	private DataOutputStream outchan;
	private Server server;
	private Socket socket;

	Client(Server s) {
		server = s;
	}

	public void run() {
		Socket s;

		while (true) {
			synchronized (server) {
				if (server.stillWaiting() == 0)
					try {
						server.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				s = server.removeFirstSocket();
			}
			try {
				inchan = new BufferedReader(new InputStreamReader(s.getInputStream()));
				outchan = new DataOutputStream(s.getOutputStream());
				socket = s;
				synchronized (server) {
					server.newConnect(outchan);
				}
				while (true) {
					String command = inchan.readLine();
					if (command == null || command.equals("")) {
						System.out.println("Fin de connexion.");
						break;
					}
					synchronized (server) {
						server.writeAllButMe(command + "\n", outchan);
					}
				}
				synchronized (server) {
					server.clientLeft(outchan);
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
