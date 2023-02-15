import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(5106);
        } catch (IOException ex) {
            System.out.println("Port number not empty");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
		String command = null;
		String filename = null;
		ObjectInputStream commandIn = null;
		String fileSt = null;
        
        
       
        
        
        boolean flag = true;
        while(flag)
        {
			
			try {
            socket = serverSocket.accept();
            //System.out.println("Connection received from " + socket.getInetAddress().getHostName());
        } catch (IOException ex) {
            System.out.println("Error accepting client connection");
        }
		
			try{
			commandIn = new ObjectInputStream(socket.getInputStream());
			}
			catch(EOFException e){
				//System.out.println("ignore");
				continue;
			}
        
			try{
				command = (String)commandIn.readObject();
				
				filename = (String)commandIn.readObject();
				System.out.println("\n");
				System.out.println("Client request to  "+command+" file "+filename);
				//System.out.println("filename is : "+filename);
				
			}catch(ClassNotFoundException ex){
				System.out.println("data recieved in unknown format");
			}
			
			
			if(command.equals("upload"))
			{
				
				//System.out.println("upload operation");
				try {
					in = socket.getInputStream();
				} catch (IOException ex) {
					System.out.println("Error getting socket input stream ");
				}

				try {
					out = new FileOutputStream("new"+filename);
				} catch (FileNotFoundException ex) {
					System.out.println("File not found. ");
				}
				
				
				byte[] bytes = new byte[1024];

				int count;
				while ((count = in.read(bytes)) > 0) {
					out.write(bytes, 0, count);
				}

				//out.close();
				//in.close();
				socket.close();
		}
		
		
		else if(command.equals("get"))
		{
			//System.out.println("download operation");
			//already have file name
			ObjectOutputStream filestatus = new ObjectOutputStream(socket.getOutputStream());
			 
			
			File file = new File(filename);
			if(file.exists()){
				// sizeof file
					
					fileSt = "FileExists";
					filestatus.writeObject(fileSt);
					filestatus.flush();
					
        			long length = file.length();
					
					
        			byte[] bytess = new byte[1024];
        			InputStream inp = new FileInputStream(file);
        			OutputStream sockOut = socket.getOutputStream();
					
        			int count;
        			while ((count = inp.read(bytess)) > 0) {
            			sockOut.write(bytess, 0, count);
        			}
        			sockOut.close();
			}
			else{
				fileSt = "FileDoesNotExists";
				filestatus.writeObject(fileSt);
				filestatus.flush();
				System.out.println("Requested file does not exist");
				
			}
			socket.close();
			
		}
		
		
		else if(command.equals("exit"))
		{
			System.out.println("Client Disconnected");
			//flag = false;
			socket.close();
		}
		else{
			System.out.println("Unrecognized command recieved from client");
		}
		
		
	
		}
		
		
        
        
    }
}
