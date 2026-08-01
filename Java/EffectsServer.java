import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class EffectsServer {
    private static final int PORT = 8081;
    private static ServerSocket serverSocket;
    private static boolean running = true;
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Effects Server on port " + PORT);
        System.out.println("📅 " + new Date());
        System.out.println("✅ Ready for connections...");
        
        try {
            serverSocket = new ServerSocket(PORT);
            
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("\n🔗 New connection from: " + clientSocket.getInetAddress());
                
                // Handle each connection in its own thread
                new Thread(new ConnectionHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("❌ Server error: " + e.getMessage());
        }
    }
    
    static class ConnectionHandler implements Runnable {
        private Socket socket;
        
        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                // Set timeout to prevent hanging
                socket.setSoTimeout(3000);
                
                // Get input stream
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                
                // Read request (simple approach)
                byte[] buffer = new byte[1024];
                int bytesRead = input.read(buffer);
                
                if (bytesRead > 0) {
                    String request = new String(buffer, 0, bytesRead);
                    
                    // Extract the request line (first line)
                    String[] lines = request.split("\\r?\\n");
                    if (lines.length > 0) {
                        String requestLine = lines[0];
                        System.out.println("📨 Request: " + requestLine);
                        
                        // Generate response based on path
                        String path = extractPath(requestLine);
                        String jsonResponse = generateResponse(path);
                        
                        // Send HTTP response
                        String httpResponse = 
                            "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Access-Control-Allow-Origin: *\r\n" +
                            "Content-Length: " + jsonResponse.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            jsonResponse;
                        
                        output.write(httpResponse.getBytes());
                        output.flush();
                        
                        System.out.println("📤 Sent response for: " + path);
                    }
                }
                
                // Close connection
                socket.close();
                System.out.println("🔌 Connection closed");
                
            } catch (SocketTimeoutException e) {
                System.out.println("⏰ Timeout - closing connection");
            } catch (IOException e) {
                // This is normal - browser closes connections
                System.out.println("⚠️  Connection closed by client");
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        
        private String extractPath(String requestLine) {
            // Extract path from "GET /path HTTP/1.1"
            String[] parts = requestLine.split(" ");
            if (parts.length >= 2) {
                return parts[1];
            }
            return "/";
        }
        
        private String generateResponse(String path) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            Random rand = new Random();
            
            if (path.equals("/") || path.equals("/status") || path.equals("/health")) {
                return String.format(
                    "{\n" +
                    "  \"status\": \"running\",\n" +
                    "  \"service\": \"Java Effects Server\",\n" +
                    "  \"version\": \"1.0\",\n" +
                    "  \"port\": %d,\n" +
                    "  \"timestamp\": \"%s\",\n" +
                    "  \"endpoints\": [\"/\", \"/fractal\", \"/wave\", \"/transform\", \"/api\"]\n" +
                    "}", PORT, timestamp);
            }
            else if (path.equals("/fractal")) {
                return String.format(
                    "{\n" +
                    "  \"type\": \"fractal\",\n" +
                    "  \"name\": \"Julia Set\",\n" +
                    "  \"complexity\": %d,\n" +
                    "  \"color\": \"#%06X\",\n" +
                    "  \"timestamp\": \"%s\"\n" +
                    "}", rand.nextInt(100) + 50, rand.nextInt(0xFFFFFF), timestamp);
            }
            else if (path.equals("/wave")) {
                StringBuilder points = new StringBuilder("[");
                for (int i = 0; i < 8; i++) {
                    double y = Math.sin(i * 0.8) * 5;
                    points.append(String.format("%.2f", y));
                    if (i < 7) points.append(", ");
                }
                points.append("]");
                
                return String.format(
                    "{\n" +
                    "  \"type\": \"wave\",\n" +
                    "  \"amplitude\": 5.0,\n" +
                    "  \"frequency\": 0.8,\n" +
                    "  \"points\": %s,\n" +
                    "  \"timestamp\": \"%s\"\n" +
                    "}", points.toString(), timestamp);
            }
            else if (path.equals("/transform")) {
                return String.format(
                    "{\n" +
                    "  \"type\": \"3d_transform\",\n" +
                    "  \"rotation\": {\"x\": %d, \"y\": %d, \"z\": %d},\n" +
                    "  \"scale\": {\"x\": %.1f, \"y\": %.1f, \"z\": %.1f},\n" +
                    "  \"timestamp\": \"%s\"\n" +
                    "}", 
                    rand.nextInt(360), rand.nextInt(360), rand.nextInt(360),
                    0.5 + rand.nextDouble(), 0.5 + rand.nextDouble(), 0.5 + rand.nextDouble(),
                    timestamp);
            }
            else if (path.equals("/api")) {
                return String.format(
                    "{\n" +
                    "  \"api\": \"Effects API\",\n" +
                    "  \"version\": \"1.0\",\n" +
                    "  \"documentation\": \"Simple JSON API for geometry effects\",\n" +
                    "  \"timestamp\": \"%s\"\n" +
                    "}", timestamp);
            }
            else if (path.equals("/favicon.ico")) {
                // Empty response for favicon
                return "{}";
            }
            else {
                return String.format(
                    "{\n" +
                    "  \"error\": \"Path not found\",\n" +
                    "  \"requested\": \"%s\",\n" +
                    "  \"available\": [\"/\", \"/fractal\", \"/wave\", \"/transform\", \"/api\"],\n" +
                    "  \"timestamp\": \"%s\"\n" +
                    "}", path, timestamp);
            }
        }
    }
    
    // Shutdown hook
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n🛑 Shutting down server...");
            running = false;
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("Error during shutdown: " + e.getMessage());
            }
            System.out.println("✅ Server stopped");
        }));
    }
}