import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;


public class Client {
    public static void main(String[] args) throws IOException {
    Socket socket = null;
    String host = "127.0.0.1";
	int port = 0;
	Scanner in = new Scanner(System.in);
	String input = null;
	String[] inputArray = null;
	String operation;
	String filename;
	InputStream inSock = null;
	OutputStream outFile = null;
	ObjectInputStream fileExists = null;
	String fileBit = null;
	
	boolean flag = true;
	
	//indefinite while loop controlled by exit command from user
	while(flag){
		
		System.out.println("List of valid commands: \n				ftpclient <port number>");
		System.out.println("Enter command : ");
		input = in.nextLine();
		inputArray = input.split(" ");
	
		if(inputArray[0].equals("ftpclient")){
			try{
			port = Integer.parseInt(inputArray[1]);
			flag = false;
			
			}catch(NumberFormatException ex){
				//ex.printStackTrace();
				System.out.println("Invalid port input : <port number> should be a number");
			}		
		
		}
		else{
			System.out.println("Invlaid Command");
		}
	}
	
	
	flag = true;
	while(flag){
		try{
        		socket = new Socket(host, port);
        	}
			catch(SocketException e){
        			//e.printStackTrace();
        			System.out.println("Port number wrong or server not online");
       		}
		System.out.println("\n");		
		System.out.println("List of valid commands: \n				 get <filename>\n 				upload <filename> \n 				exit");
		System.out.println("Enter command : ");
		input = in.nextLine();
		inputArray = input.split(" ");

		
		if(inputArray[0].equals("get")){
			//System.out.println("download");
			//variables "operation" and "filename" are sent to the server
			operation = inputArray[0];
			filename = inputArray[1];
			
			ObjectOutputStream commandOut = new ObjectOutputStream(socket.getOutputStream());
        	commandOut.writeObject(operation);
        	commandOut.flush();
        			
        	commandOut.writeObject(filename);
        	commandOut.flush();
			
			fileExists = new ObjectInputStream(socket.getInputStream());
			try{
				fileBit = (String)fileExists.readObject();
			}
			catch(ClassNotFoundException ex){
				System.out.println("data recieved in unknown format");
			}
			
			//System.out.println("file status "+fileBit);
			if(fileBit.equals("FileDoesNotExists"))
			{
				System.out.println("This file does not exist on the server");
				commandOut.close();
				fileExists.close();
				socket.close();
				continue;
			}
			
			try {
					inSock = socket.getInputStream();
				} catch (IOException ex) {
					System.out.println("Error getting socket input stream ");
				}

				try {
					outFile = new FileOutputStream("down"+filename);
					//file downloaded via "get" command will have prefix "down" added to filename
				} catch (FileNotFoundException ex) {
					System.out.println("File not found. ");
				}
				
				//1024 byte buffer to recieve file in parts
				byte[] bytess = new byte[1024];

				//combining parts to make a file
				int count;
				while ((count = inSock.read(bytess)) > 0) {
					outFile.write(bytess, 0, count);
				}

				outFile.close();
				inSock.close();
				commandOut.close();
				fileExists.close();
				
			
			
		}
		else if(inputArray[0].equals("upload")){
			operation = inputArray[0];
		
			//System.out.println("upload");
			filename = inputArray[1];
			
			File file = new File(filename);
			
			if(file.exists()){
				// Get the size of the file
        			long length = file.length();
					
					//init byte array to send file in parts
        			byte[] bytes = new byte[1024];
					
        			InputStream inp = new FileInputStream(file);
        			
					OutputStream out = socket.getOutputStream();
					
        			ObjectOutputStream commandOut = new ObjectOutputStream(socket.getOutputStream());
        			commandOut.writeObject(operation);
        			commandOut.flush();
        			
        			commandOut.writeObject(filename);
        			commandOut.flush();
        			
        			int count;
        			while ((count = inp.read(bytes)) > 0) {
            			out.write(bytes, 0, count);
        			}
        			inp.close();
					out.close();
					commandOut.close();
			}
			else{
				System.out.println("Specified file does not exist");
			}

					
		}
		else if(inputArray[0].equals("exit")){
			//System.out.println("exit");
			operation = inputArray[0];
			filename = "exit";
			
			ObjectOutputStream commandOut = new ObjectOutputStream(socket.getOutputStream());
        	commandOut.writeObject(operation);
        	commandOut.flush();
        			
        	commandOut.writeObject(filename);
        	commandOut.flush();
			
			commandOut.close();
			flag = false;
		}
		else{
		System.out.println("Invalid command");
		}
		
		socket.close();
	}
        
        
    }
}
