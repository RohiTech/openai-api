import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.*;
import org.json.JSONObject;

public class OpenAIExample {

    public static void main(String[] args) {
        // Crea la interfaz gráfica
        JFrame frame = new JFrame("OpenAI API Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Crear componentes
        JPanel panel = new JPanel();
        JTextArea inputTextArea = new JTextArea(5, 20);
        JTextArea outputTextArea = new JTextArea(10, 20);
        JButton generateButton = new JButton("Generar Respuesta");

        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(inputTextArea), BorderLayout.NORTH);
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        panel.add(generateButton, BorderLayout.SOUTH);
        
        frame.add(panel);
        frame.setVisible(true);
        
        // Acción del botón
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prompt = inputTextArea.getText();
                String response = getOpenAIResponse(prompt);
                outputTextArea.setText(response);
            }
        });
    }

    // Método para llamar a la API de OpenAI
    public static String getOpenAIResponse(String prompt) {
        String apiKey = "";  // Sustituye con tu clave API
        String endpoint = "https://api.openai.com/v1/completions";
        
        try {
            // Crear la solicitud JSON
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("model", "text-davinci-003");
            jsonRequest.put("prompt", prompt);
            jsonRequest.put("max_tokens", 100);
            
            // Crear la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                    .build();
            
            // Enviar la solicitud y recibir la respuesta
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Procesar la respuesta
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text").trim();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener respuesta de OpenAI.";
        }
    }
}
