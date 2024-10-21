package Client_server.client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8189;
    private String clientName;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JFrame frame;
    private JTextField messageField;
    private JTextArea chatArea;
    private JTextField nameField;
    private JPasswordField passwordField;

    public Client(String clientName) {
        this.clientName = clientName;
        setupGUI();
    }

    public void start() {
        connectToServer();
    }

    private void setupGUI() {
        frame = new JFrame(clientName + " - Chat client");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Панель для ввода логина
        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        loginPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        nameField.setText(clientName);  // Автозаполнение имени клиента
        loginPanel.add(nameField);

        loginPanel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("login");
        loginButton.addActionListener(e -> connectToServer());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(loginPanel, BorderLayout.CENTER);
        topPanel.add(loginButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        // Поле для чата
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Поле для ввода сообщений
        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());

        JButton sendButton = new JButton("send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Отправить имя на сервер
            out.println(clientName);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Не удалось подключиться к серверу");
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.setText("");
        }
    }
}
