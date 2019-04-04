import java.util.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import Books.BookStore;
import Books.Book;

public class SampleHttpServer {

    private static BookStore store= new BookStore();
    private static Gson gson=new Gson();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/viewBooks", new MyHandler1());
        server.createContext("/book/q", new MyHandler2());
        server.createContext("/addBook", new MyHandler3());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler1 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String[] booklist = store.listAllBooks();
            String response = gson.toJson(booklist);
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class MyHandler2 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String param=t.getRequestURI().getQuery();
            int id = Integer.parseInt(param.split("=")[1]);
            String response="";

            if(store.currentId() > id){
                //get book object
                Book book = store.bookDetails(id);
                t.getResponseHeaders().set("Content-Type", "application/json");
                response=gson.toJson(book);
            }else{
                t.getResponseHeaders().set("Content-Type", "application/text");
                response="Invalid id";
            }
            System.out.println();


            //send response
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


    static class MyHandler3 implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {

            //used for reading in the request data
            InputStream is=t.getRequestBody();
            String requestString = "";
            String response="";
            StringBuilder requestBuffer = new StringBuilder();

            int rByte;
            while ((rByte = is.read()) != -1) {
                requestBuffer.append((char) rByte);
            }
            is.close();

            if (requestBuffer.length() > 0) {
                requestString = URLDecoder.decode(requestBuffer.toString(), "UTF-8");
            } else {
                requestString = null;
            }

            // Convert JSON String to JSON Object
            JsonElement jsonString = gson.fromJson(requestString, JsonElement.class);
            JsonObject json=jsonString.getAsJsonObject();

            try{

                // Read JSON object
                String name=json.get("name").getAsString();
                String author=json.get("author").getAsString();
                String lang=json.get("lang").getAsString();
                int pages=json.get("pages").getAsInt();

                // Create new book object and add to BookStore
                Book newBook = new Book(name, author, lang, pages);
                store.addBook(newBook);
                response =  "Book is saved successfully";

            }catch(Exception e){

                response =  "Invalid data: " + e;

            }

            // Send response message.
            t.getResponseHeaders().set("Content-Type", "application/text");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}