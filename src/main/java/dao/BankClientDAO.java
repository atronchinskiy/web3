package dao;

import com.mysql.jdbc.Connection;
import model.BankClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean deleteBankClient(String name) throws SQLException {
        boolean flag = false;
        try {
            PreparedStatement ps = null;
            ps = connection.prepareStatement("delete from bank_client WHERE name = (?);");
            ps.setString(1, name);
            if (ps.executeUpdate() > 0) {
                ps.close();
                flag = true;
            }
            return false;
        } catch (Exception e) {}
        finally {
            try {
                connection.close();
            } catch (Exception e) {}

            return flag;
        }
    }

    public void deleteClient(Long id) throws SQLException {
        Statement stmt = null;
        try {
            String deleteQuery = "DELETE FROM bank_client WHERE id = " + id;
            stmt = connection.createStatement();
            stmt.execute(deleteQuery);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                connection.close();
            } catch (Exception e) {

            }
        }
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> resultList = new ArrayList<>();
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("select * from bank_client");
            result = stmt.getResultSet();
            while (result.next()) {
                resultList.add(new BankClient(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getLong(4)));
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
                connection.close();
            } catch (Exception e) {
                //NOP
            }
        }
        return resultList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        Statement stmt = null;
        boolean flag = false;
        try {
            stmt = connection.createStatement();
            stmt.execute("select id, name, password, money from bank_client WHERE name='" + name + "' AND password = '" + password + "';");
            ResultSet result = stmt.getResultSet();
            if (result.next()) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (stmt!=null)  stmt.close();
                connection.close();
            } catch (Exception e) {}

            return flag;
        }

    }

    public void updateClientsMoneyM(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement ps = null;
        Statement st = null;
        try {
            st = connection.createStatement();
            st.execute("select name, password, money from bank_client WHERE name='" + name + "';");
            ResultSet result = st.getResultSet();
            result.next();
            String getName = result.getString(1);
            String getPassword = result.getString(2);
            long currentMoney = result.getLong(3);

            if (name.equals(getName) && password.equals(getPassword)) {
                ps = connection.prepareStatement("UPDATE bank_client SET money = (?) WHERE name = (?) and password = (?);");
                //ps.setLong(1, currentMoney + transactValue);
                ps.setLong(1, transactValue);
                ps.setString(2, name);
                ps.setString(3, password);
                ps.executeUpdate();
            }
//        } catch (SQLException e) {
//            e.printStackTrace();
        } finally {
            try {
                if(st != null) st.close();
                if(ps!= null) ps.close();
                connection.close();
            } catch (Exception e) {}
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String preparedStatement = "select * from bank_client where name=?";
        PreparedStatement to = null;
        try {
            stmt = connection.prepareStatement(preparedStatement);
            stmt.setString(1, name);
            stmt.execute();
            result = stmt.getResultSet();
            String getName = "";
            String getPassword = "";
            if (result.next()) {
                getName = result.getString(2);
                getPassword = result.getString(3);
            }
            if (name.equals(getName) && password.equals(getPassword)) {
                String updateQuery = "UPDATE bank_client SET money =? WHERE name =? AND  password =?";
                to = connection.prepareStatement(updateQuery);
                to.setLong(1, transactValue);
                to.setString(2, name);
                to.setString(3, password);
                to.execute();
            }
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (to != null) {
                    to.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        BankClient bCl = null;
        boolean flag = false;
        long idCl;
        String name;
        String passord;
        Long mony = null;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            flag = stmt.execute("select id, name, password, money from bank_client WHERE id=" + id);
            ResultSet result = stmt.getResultSet();

            if (result.next()) {
                idCl = result.getInt(1);
                name = result.getString(2);
                passord = result.getString(3);
                mony = result.getLong(4);
                bCl = new BankClient(idCl, name, passord, mony);
            }
        } catch (Exception e) {}
        finally {
            try {
                if (stmt != null) stmt.close();
                connection.close();
            } catch (Exception e) {}

            return bCl;
        }
    }


    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        BankClient bankClient = getClientByName(name);
        return bankClient.getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        try {
            return getClientByName(name).getId();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                //NOP
            }
        }
    }

    public BankClient getClientByName(String name) {
        Statement stmt = null;
        ResultSet result = null;
        String preparedStatement = "select * from bank_client where name=?";
        BankClient bankClient = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("select id, name, password, money from bank_client where name='" + name + "'");
            result = stmt.getResultSet();
            if (result.next()) {
                bankClient = new BankClient(result.getLong(1), result.getString(2), result.getString(3), result.getLong(4));
            }
        } catch (SQLException e) {}
        finally {
            try {
                if (stmt != null) stmt.close();
                if (result != null)result.close();
                connection.close();
            } catch (Exception e) {}

            return bankClient;
        }
    }

    public void addClient(BankClient client) throws SQLException {
        Boolean flag = false;
        int cnt = 0;
        PreparedStatement ps = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement stmt = null;

        try{
            String preparedStatementString = "select * from bank_client where name=?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1, client.getName());
            preparedStatement.execute();
            result = preparedStatement.getResultSet();

            if (!result.next()) {
            //if (getClientByName(client.getName()) == null) {
                ps = connection.prepareStatement("INSERT INTO bank_client (name, password, money) VALUES((?), (?), (?))");
                ps.setString(1, client.getName());
                ps.setString(2, client.getPassword());
                ps.setLong(3, client.getMoney());
                ps.executeUpdate();
//                stmt = connection.createStatement();
//                stmt.execute("insert into bank_client (name, password, money) values (" + "\"" + client.getName() + "\", " + "\"" + client.getPassword() + "\", " + client.getMoney() + ")");
            }else {
                throw new SQLException();
            }

//        }
//        catch (SQLException e) {
            //e.printStackTrace();
        }finally {
            try {
                if (ps != null) ps.close();
                if (preparedStatement != null) preparedStatement.close();
                if (stmt != null) stmt.close();
                connection.close();
            } catch (Exception e) {}
        }
    }


    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}

/*

package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> resultList = new ArrayList<>();
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("select * from bank_client");
            result = stmt.getResultSet();
            while (result.next()) {
                resultList.add(new BankClient(
                        result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getLong(4)));
            }
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
        return resultList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        BankClient bankClient = getClientByName(name);

        if (bankClient != null) {
            return bankClient.getName().equals(name) && bankClient.getPassword().equals(password);
        }
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String preparedStatement = "select * from bank_client where name=?";
        PreparedStatement to = null;
        try {
            stmt = connection.prepareStatement(preparedStatement);
            stmt.setString(1, name);
            stmt.execute();
            result = stmt.getResultSet();
            String getName = "";
            String getPassword = "";
            if (result.next()) {
                getName = result.getString(2);
                getPassword = result.getString(3);
            }
            if (name.equals(getName) && password.equals(getPassword)) {
                String updateQuery = "UPDATE bank_client SET money =? WHERE name =? AND  password =?";
                to = connection.prepareStatement(updateQuery);
                to.setLong(1, transactValue);
                to.setString(2, name);
                to.setString(3, password);
                to.execute();
            }
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (to != null) {
                    to.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;
        BankClient bankClient = null;

        try {
            String getById = "select * from bank_client where id=?";
            statement = connection.prepareStatement(getById);
            statement.setLong(1, id);
            statement.execute();
            result = statement.getResultSet();
            result.next();
            bankClient = new BankClient(id, result.getString(2), result.getString(3), result.getLong(4));
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (result != null) {
                    result.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        BankClient bankClient = getClientByName(name);

        return bankClient.getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        try {
            return getClientByName(name).getId();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {


            }
        }

    }

    public BankClient getClientByName(String name) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String preparedStatement = "select * from bank_client where name=?";
        BankClient bankClient = null;
        try {
            stmt = connection.prepareStatement(preparedStatement);
            stmt.setString(1, name);
            stmt.execute();
            result = stmt.getResultSet();
            if (result.next()) {
                bankClient = new BankClient(result.getLong(1), name, result.getString(3), result.getLong(4));
            }
            return bankClient;
        } catch (SQLException e) {


        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        Statement stmt = null;

        try {
            String preparedStatementString = "select * from bank_client where name=?";
            preparedStatement = connection.prepareStatement(preparedStatementString);
            preparedStatement.setString(1, client.getName());
            preparedStatement.execute();
            result = preparedStatement.getResultSet();

            if (!result.next()) {
                stmt = connection.createStatement();
                stmt.execute("insert into bank_client (name, password, money) values (" + "\"" + client.getName() + "\", " + "\"" + client.getPassword() + "\", " + client.getMoney() + ")");
            } else {
                throw new SQLException();
            }
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }

    public void deleteClient(Long id) throws SQLException {
        Statement stmt = null;
        try {
            String deleteQuery = "DELETE FROM bank_client WHERE id = " + id;
            stmt = connection.createStatement();
            stmt.execute(deleteQuery);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }

    public void dropTable() throws SQLException {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                connection.close();
            } catch (Exception e) {


            }
        }
    }
}
*/
