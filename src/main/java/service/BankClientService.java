package service;

import dao.*;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException { //
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) {
        BankClient bc = null;
        try {
            bc = getBankClientDAO().getClientByName(name);
        } catch (Exception e) {
        }
        return bc;
    }

    public List<BankClient> getAllClientM() {
        List<BankClient> listBc = new ArrayList<>();
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {

        }
        return listBc;
    }



    public List<BankClient> getAllClient() throws DBException {
        List<BankClient> list;
        try {
            list = getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

/*    public boolean deleteClientM(String name) {
        boolean flag = false;
        try {
            flag = getBankClientDAO().deleteBankClient(name);
        } catch (SQLException e) {
        }
        return flag;
    }*/

    public boolean deleteClient(String name) throws DBException {
        try {
            BankClient bankClient = getBankClientDAO().getClientByName(name);
            getBankClientDAO().deleteClient(bankClient.getId());
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return true;
    }

    public boolean addClientM(BankClient client) throws DBException {
        BankClientDAO dao = getBankClientDAO();
        boolean flag = false;
        try {
            dao.addClient(client);
            //if (dao.getClientByName(client.getName()) == null) flag = true;
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            getBankClientDAO().addClient(client);
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        BankClient consumer = null;
        try {
            BankClientDAO dao = getBankClientDAO();
            consumer = dao.getClientByName(name);

//            if (dao.isClientHasSum(sender.getName(), value)
//                    && dao.validateClient(sender.getName(), sender.getPassword())
//                    //&& consumer != null) {
//                    && dao.validateClient(consumer.getName(), consumer.getPassword())){
//                dao.updateClientsMoney(sender.getName(), sender.getPassword(), (-1) * value);
//                dao.updateClientsMoney(consumer.getName(), consumer.getPassword(), value);

            if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())
                    && getBankClientDAO().validateClient(name, consumer.getPassword())){
                if (getBankClientDAO().isClientHasSum(sender.getName(), value)) {
                    Long toSentMoney = consumer.getMoney() + value;
                    getBankClientDAO().updateClientsMoney(
                            consumer.getName(),
                            consumer.getPassword(),
                            toSentMoney);

                    Long senderMoney = sender.getMoney() - value;
                    getBankClientDAO().updateClientsMoney(
                            sender.getName(),
                            sender.getPassword(),
                            senderMoney);
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean sendMoneyToClient1(BankClient sender, String name, Long value) throws DBException {
        try {
            BankClient sendMoneyToThisClient = getBankClientDAO().getClientByName(name);
            if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())
                    && getBankClientDAO().validateClient(name, sendMoneyToThisClient.getPassword())) {

                if (getBankClientDAO().isClientHasSum(sender.getName(), value)) {

                    Long toSentMoney = sendMoneyToThisClient.getMoney() + value;
                    getBankClientDAO().updateClientsMoney(
                            sendMoneyToThisClient.getName(),
                            sendMoneyToThisClient.getPassword(),
                            toSentMoney);

                    Long senderMoney = sender.getMoney() - value;
                    getBankClientDAO().updateClientsMoney(
                            sender.getName(),
                            sender.getPassword(),
                            senderMoney);
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                append("jdbc:mysql://").        //db type
//                    append("10.0.75.1:").           //host name
                append("DOM-CS-WS-0250:").
                append("3306/").                //port
//                    append("db_example?").          //db name
                append("webData?").
//                    append("user=sys&").          //login
                append("user=sys");          //login
//                    append("password=");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
//      public static BankClientDAO getBankClientDAO() {

        BankClientDAO bcd = new BankClientDAO((com.mysql.jdbc.Connection) getMysqlConnection());



        return bcd;
    }
}
/*

package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        BankClient bankClient;
        try {
            bankClient = getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return bankClient;
    }

    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() throws DBException {
        List<BankClient> list;
        try {
            list = getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

    public boolean deleteClient(String name) throws DBException {
        try {
            BankClient bankClient = getBankClientDAO().getClientByName(name);
            getBankClientDAO().deleteClient(bankClient.getId());
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return true;
    }

    public boolean addClient(BankClient client) throws DBException {
        try {
            getBankClientDAO().addClient(client);
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        try {
            BankClient sendMoneyToThisClient = getBankClientDAO().getClientByName(name);
            if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())
                    && getBankClientDAO().validateClient(name, sendMoneyToThisClient.getPassword())) {

                if (getBankClientDAO().isClientHasSum(sender.getName(), value)) {

                    Long toSentMoney = sendMoneyToThisClient.getMoney() + value;
                    getBankClientDAO().updateClientsMoney(
                            sendMoneyToThisClient.getName(),
                            sendMoneyToThisClient.getPassword(),
                            toSentMoney);

                    Long senderMoney = sender.getMoney() - value;
                    getBankClientDAO().updateClientsMoney(
                            sender.getName(),
                            sender.getPassword(),
                            senderMoney);
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void cleanUp() throws DBException {
        try {
            getBankClientDAO().dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        try {
            getBankClientDAO().createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {

        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();
            url.


        append("jdbc:mysql://").        //db type
                    append("DOM-CS-WS-0250:").
                    append("3306/").                //port
                    append("webData?").
                    append("user=sys");          //login
            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO((com.mysql.jdbc.Connection) getMysqlConnection());
    }
}
*/
