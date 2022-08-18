package Assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;


public class XfxServer {

    public static void main(String[] args) throws IOException {
        
        ServerSocket ss = new ServerSocket(80);
        
        while(true){
            System.out.println("Server waiting...");
            Socket connectionFromClient = ss.accept();
            System.out.println("Server got a connection from a client whose port is: " + connectionFromClient.getPort());
            
            try{
		InputStream in = connectionFromClient.getInputStream();
		OutputStream out = connectionFromClient.getOutputStream();
                
		String errorMessage = "NOT FOUND\n";
                String errorMessage2 = "EMPTY FOLDER\n";
                
		BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
		BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));
		
                //DataInputStream dataIn = new DataInputStream(in);
		DataOutputStream dataOut = new DataOutputStream(out);
		
                String header = headerReader.readLine();
		StringTokenizer strk = new StringTokenizer(header, " ");
		String command = strk.nextToken();
		String fileName = strk.nextToken();
                
                switch (command) {
                    case "download":
                        try{
                            FileInputStream fileIn = new FileInputStream("ServerShare/" + fileName);
                            int fileSize = fileIn.available();
                            
                            header = "OK" + "\n";
                            headerWriter.write(header, 0, header.length());
                            headerWriter.flush();
                            
                            byte[] bytes = new byte[fileSize];
                            
                            fileIn.read(bytes);
                            fileIn.close();
                            
                            dataOut.write(bytes, 0, fileSize);
                        }catch(Exception ex) {
                            headerWriter.write(errorMessage, 0, errorMessage.length());
                            headerWriter.flush();
                        }finally {
                            connectionFromClient.close();
                        }   
                        break;
                        
                    case "upload":
                        break;
                        
                    case "getSize":
                        try{
                            FileInputStream file = new FileInputStream("ServerShare/" + fileName);
                            int fileSize = file.available();
                            
                            header = fileSize + "\n";
                            headerWriter.write(header, 0, header.length());
                            headerWriter.flush();
                        }finally {
                            connectionFromClient.close();
                        }   
                        break;
                     
                    case "list":
                        try{
                            String path = " ServerShare/";
                            
                            File folder = new File(path);
                            File[] list = folder.listFiles();
                            
                            header = "OK" + list.length;
                            headerWriter.write(header, 0, header.length());
                            headerWriter.flush();
                            
                            for (int i = 0; i < list.length; i++) {
                                header = list[i].getName();
                                headerWriter.write(header, 0, header.length());
                                headerWriter.flush();
                            }
                        }catch(Exception ex) {
                            headerWriter.write(errorMessage2, 0, errorMessage2.length());
                            headerWriter.flush();
                        }finally {
                            connectionFromClient.close();
                        }   break;
                        
                    case "resume":
                        String temp = strk.nextToken();
                        int fSize = Integer.parseInt(temp);
                        FileInputStream file = new FileInputStream("ServerShare/" + fileName);
                        int sameSize = file.available();
                        byte[] bytes = new byte[sameSize-fSize];
                        file.skip(fSize);
                        file.read(bytes);
                        file.close();
                        dataOut.write(bytes, fSize, sameSize-fSize);
                        break;
                    default:
                        System.out.println("Connection got from an incompatible client");
                        break;
                }
            }catch(IOException | NumberFormatException e) {}
        }
    }
}
