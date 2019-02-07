public class Main {

	public static void main(String args[]) {
		int port = Integer.parseInt(args[0]);
		int capacity = Integer.parseInt(args[1]);
		Server server = new Server(port, capacity);
		server.run();
	}
}




