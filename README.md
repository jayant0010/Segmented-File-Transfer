# Project 1 - Computer Networks

Client-server file transfer using socket.

Saijayanth Chidirala


## Usage

### Server
```bash
saijayant98@jayc:~$ cd Desktop/socket/
saijayant98@jayc:~/Desktop/socket$ javac Server.java
saijayant98@jayc:~/Desktop/socket$ ls
Client.java   Server.java            uploadTestFile.pptx
Server.class  downloadTestFile.pptx
saijayant98@jayc:~/Desktop/socket$ java Server 


Client request to  upload file uploadTestFile.pptx


Client request to  upload file newuploadTestFile.pptx


Client request to  get file wrongfile
Requested file does not exist


Client request to  get file downloadTestFile.pptx
```
### Client
```bash
saijayant98@jayc:~$ cd Desktop/socket
saijayant98@jayc:~/Desktop/socket$ javac Client.java 
saijayant98@jayc:~/Desktop/socket$ ls
Client.class  Server.class  downloadTestFile.pptx
Client.java   Server.java   uploadTestFile.pptx
saijayant98@jayc:~/Desktop/socket$ java client
Error: Could not find or load main class client
saijayant98@jayc:~/Desktop/socket$ java Client
List of valid commands: 
				ftpclient <port number>
Enter command : 
ftpclient 5106


List of valid commands: 
				 get <filename>
 				upload <filename> 
 				exit
Enter command : 
upload uploadTestFile.pptx


List of valid commands: 
				 get <filename>
 				upload <filename> 
 				exit
Enter command : 
upload wrongfile 
Specified file does not exist
```

## Methodology

Both client and server have upload and download capabilities. All file transfer happens by breaking down files in parts of 1024bytes, sending them over socket and reassembling them at the receiving end. Files given at input that do not exist are ignored.

## Requirements

Java Development Kit 8
