package chat;

import java.io.*;
import java.net.Socket;

public class ServerSomething extends Thread {
    // Сокет для связи именно с этим клиентом
    private Socket socket;
    // Потоки для чтения сообщений ОТ клиента и отправки сообщений ЕМУ
    private BufferedReader in;
    private BufferedWriter out;

    // Конструктор. Вызывается из Server.java при новом подключении
    public ServerSomething(Socket socket) throws IOException {
        this.socket = socket;
        // Создаем потоки на основе сокета
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        // Запускаем нить (вызывается метод run())
        start();
        System.out.println("Запущен новый обработчик для клиента.");
    }

    @Override
    public void run() {
        String word; // Сюда будем читать сообщения от клиента
        try {
            // 1. ОСНОВНОЙ ЦИКЛ: ЧИТАЕМ СООБЩЕНИЯ ОТ КЛИЕНТА
            while (true) {
                word = in.readLine();
                if (word == null || word.equalsIgnoreCase("stop")) {
                    // Клиент отключился или отправил команду "stop"
                    this.downService();
                    break;
                }
                System.out.println("Клиент " + this.getName() + " написал: " + word);

                // 2. РАССЫЛАЕМ СООБЩЕНИЕ ВСЕМ ПОДКЛЮЧЕННЫМ КЛИЕНТАМ
                for (ServerSomething vr : Server.serverList) {
                    // Отправляем сообщение каждому обработчику (включая отправителя)
                    vr.send(word);
                }
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    // Метод для отправки сообщения КОНКРЕТНОМУ клиенту
    private void send(String msg) {
        try {
            out.write(msg + "\n"); // Добавляем \n как признак конца строки
            out.flush();           // ОБЯЗАТЕЛЬНО! Выталкиваем данные из буфера в сеть
        } catch (IOException ignored) {
            // Если не удалось отправить (клиент "упал"), ничего не делаем
        }
    }

    // Метод для корректного завершения работы с клиентом
    private void downService() {
        try {
            if (!socket.isClosed()) {
                // Закрываем сокет и потоки
                socket.close();
                in.close();
                out.close();
                // Удаляем себя из общего списка активных обработчиков
                Server.serverList.remove(this);
                System.out.println("Клиент " + this.getName() + " отключился.");
            }
        } catch (IOException ignored) {}
    }
}