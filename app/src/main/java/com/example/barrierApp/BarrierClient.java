package com.example.barrierApp;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Класс, осуществляющий взаимодействие с Thrift API сервера
 * @see BarrierClient
 * Имеет два метода
 * addUser - первичное добавление пользователя в базу данных
 * @see BarrierClient#addUser(String, String)
 * openBarrier - открытие шлагбаума по id пользователя и координате
 * @see BarrierClient#openBarrier(String, double, double)
 * сервер проверяет, разрешено ли пользователю открывать определенный по координатам шлагбаум
 * и присылает соответствующий ответ, который далее обрабатывается в приложении
 */

public class BarrierClient {

    // константы параметров подключения. При продакшене нужно вынести в конструктор класса
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 9090;

    /**
     * Метод добавления пользователя в БД приложения с использованием Thrift API сервера
     * @param login логин пользователя, задается в GUI
     * @param id идентификатор устройства, определяется в GUI
     * @return возвращает логическое значение, которое вернул сервер, в зависимости от него
     * пользователь видит соответствующую форму
     */

    public boolean addUser(String login, String id) {
        try {

            // открытие соединения
            TTransport transport;
            transport = new TSocket(this.IP_ADDRESS, this.PORT);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);

            // взаимодействие с API
            BarrierService.Client barService = new BarrierService.Client(protocol);
            boolean result = barService.addUser(login, id);

            // закрытие соединения
            transport.close();

            // возврат результата
            return result;

            // обработка исключений коммуникации с сервером
        } catch (TException x) {
            x.printStackTrace();
        }
        return false;
    }

    /**
     * Метод открытия шлагбаума посредством взаимодействия с Thrift API
     * @param id - идентификатор устройства пользователя, был внесен в БД при первичной регистрации
     * @param longitude - долгота шлагбаума, определенная с использованием Google Maps API
     * @param latitude - широта шлагбаума, определенная с использованием Google Maps API
     * @return - флаг ответа сервера. В зависимости от ответа сервера пользователь видит
     * соответствующую форму успех/отказ
     */
    public boolean openBarrier(String id, double longitude, double latitude) {
        try {
            // открытие соединения
            TTransport transport;

            transport = new TSocket(this.IP_ADDRESS, this.PORT);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);

            // взаимодействие с API
            BarrierService.Client barService = new BarrierService.Client(protocol);
            boolean result = barService.openBarrier(id, longitude, latitude);

            // закрытие соединения
            transport.close();

            return result;

            // обработка исключений соединения
        } catch (TException x) {
            x.printStackTrace();
        }
        return false;
    }
}

