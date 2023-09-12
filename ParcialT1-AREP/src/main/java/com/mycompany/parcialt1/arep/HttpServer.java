/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.parcialt1.arep;

import com.mycompany.parcialt1.arep.reflective.ChatGPT;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HttpServer {
    
    private Integer port = Integer.valueOf(System.getenv("PORT"));

    private static ChatGPT serviceChatGPT = new ChatGPT();
    
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;
        try { 
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine, request = "" ,typeRequest;
        Boolean firstLine = true;
        
        
        while ((inputLine = in.readLine()) != null) {
                //System.out.println("Recibí: " + inputLine);
                if (firstLine) {
                    request = inputLine.split(" ")[1];
                    typeRequest = inputLine.split(" ")[0];
                    firstLine = false;
                }
                if (!in.ready()) {break; }
            }
            System.out.println("Request " + request);
            if (request.startsWith("/consulta?comando=")) {
                String service = request.split("comando=")[1].replace(" ", "");
                System.out.println(service);
                if (service.startsWith("Class")) {
                    String command = request.split("\\(")[1].split("\\)")[0];
                    System.out.println("Command: " + command);
                    ArrayList<String> response = serviceChatGPT.getClass(command);
                    outputLine = getHtmlWithFormulary(getList(response));

                } else if (service.startsWith("invoke")) {
                    String command = request.split("\\(")[1].split("\\)")[0].replace(" ", "");
                    System.out.println("Command: " + command);
                    String className = command.split(",")[0];
                    System.out.println("Class: " + className);
                    String method = command.split(",")[1];
                    System.out.println("Method: " + method);
                    ArrayList<String> response = serviceChatGPT.getInvoke(className, method);
                    outputLine = getHtmlSimpleForm(getList(response));
                } else if (service.startsWith("unaryInvoke")) {
                    String command = request.split("\\(")[1].split("\\)")[0].replace(" ", "");
                    System.out.println("Command: " + command);
                    String className = command.split(",")[0];
                    System.out.println("Class: " + className);
                    String method = command.split(",")[1];
                    System.out.println("Method: " + method);
                    String type = command.split(",")[2];
                    System.out.println("Type: " + type);
                    String param = command.split(",")[3].replace("\"", "");
                    System.out.println("Param: " + param);
                    ArrayList<String> response = serviceChatGPT.getUnaryInvoke(className, method, type, param);
                    outputLine = getHtmlSimpleForm(getList(response));
                } else if (service.startsWith("binaryInvoke")) {
                    String command = request.split("\\(")[1].split("\\)")[0].replace(" ", "");
                    System.out.println("Command: " + command);
                    String className = command.split(",")[0];
                    System.out.println("Class: " + className);
                    String method = command.split(",")[1];
                    System.out.println("Method: " + method);
                    String type1 = command.split(",")[2];
                    System.out.println("Type1: " + type1);
                    String param1 = command.split(",")[3];
                    System.out.println("Param1: " + param1);
                    String type2 = command.split(",")[4];
                    System.out.println("Type2: " + type2);
                    String param2 = command.split(",")[5];
                    System.out.println("Param2: " + param2);
                    ArrayList<String> response = serviceChatGPT.getBinaryInvoke(className, method, type1, param1, type2, param2);
                    outputLine = getHtmlSimpleForm(getList(response));
                } else {
                    outputLine = getHeader() + getPageHtml();
                }
            } else {
                outputLine = getHeader() + getPageHtml();
            }
        out.println(outputLine);
        out.close(); 
        in.close(); 
        clientSocket.close(); 
        serverSocket.close(); 
    }
   
    /**
     * 
     * @return 
     */
    public static String getHeader(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }
    /**
     * 
     * @return 
     */
    public static String getPageHtml(){
        byte[] file;
        try {
            file = Files.readAllBytes(Paths.get("src/main/resources/page.html"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(file);
    }
    /**
     * 
     * @param values
     * @return 
     */
    public static String getList(ArrayList<String> values) {
        String response = "";
        for(String v: values) {
            String etiqueta = "<h3> " + v + "</h3>";
            response += etiqueta;
            response += "\r\n";
        }
        return response;
    }
    /**
     * 
     * @param text
     * @return 
     */
    public static String getHtmlSimpleForm(String text) {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Parcial</title>\n"
                + "</head>\n"
                + "<body>\n"
                +  text
                + "</body>\n"
                + "</html>";
        }
    
    public static String getHtmlWithFormulary(String text){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Solicita la Informacion</h1>\n" +
                "        <form action=\"/consulta\">\n" +
                "            <label for=\"name\">Opciones: Class, invoke, unaryInvoke, binaryInvoke</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"Digita \"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
        
    }
    
}
