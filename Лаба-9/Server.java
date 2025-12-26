package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    // Порт, который будет слушать сервер. Можете изменить на любой свободный.
    public static final int PORT = 8080;
    // Список всех активных подключений (нитей-обработчиков)
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        // 1. СОЗДАЮ СЕРВЕРНЫЙ СОКЕТ (ServerSocket)
        // Он "привязывается" к указанному порту и ждет подключений.
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Сервер запущен и слушает порт " + PORT + "...");

        try {
            // 2. ОСНОВНОЙ ЦИКЛ ПРИНЯТИЯ ПОДКЛЮЧЕНИЙ
            while (true) {
                // Метод accept() БЛОКИРУЕТ программу, пока кто-то не подключится.
                Socket socket = server.accept(); // Когда подключились - получаем сокет для общения

                try {
                    // 3. ДЛЯ КАЖДОГО НОВОГО КЛИЕНТА СОЗДАЮ ОТДЕЛЬНУЮ НИТЬ
                    // Класс ServerSomething унаследован от Thread и будет общаться с клиентом.
                    serverList.add(new ServerSomething(socket));
                    System.out.println("Новый клиент подключился. Всего клиентов: " + serverList.size());

                } catch (IOException e) {
                    // Если что-то пошло не так при создании обработчика, закрываю сокет.
                    socket.close();
                }
            }
        } finally {
            // Этот блок выполнится при завершении программы (нажали Ctrl+C).
            server.close();
            System.out.println("Сервер остановлен.");
        }
    }
}