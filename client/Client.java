package Client_server.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";  // Адрес сервера (локальный)
    private static final int SERVER_PORT = 12345;
    private PrintWriter out;
    private BufferedReader in;
    private JTextArea messageArea;

    public Client() {
        createClientWindow();
        connectToServer();
    }

    private void createClientWindow() {
        JFrame frame = new JFrame("Клиент");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JTextField inputField = new JTextField();
        frame.add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                out.println(message);  // Отправляем сообщение на сервер
                messageArea.append("Сообщение клиента: " + message + "\n");
                inputField.setText("");
            }
        });

        frame.setVisible(true);
    }

    private void connectToServer() {
        new Thread(() -> {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
                messageArea.append("Подключен к серверу...\n");

                out = new PrintWriter(socket.getOutputStream(), true);  // Для отправки данных на сервер
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Для получения данных от сервера

                String response;
                while ((response = in.readLine()) != null) {
                    messageArea.append("Ответ от сервера: " + response + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
