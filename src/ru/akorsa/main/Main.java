package ru.akorsa.main;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class Main {
    private static Connection con;

    public static void main(String[] args) throws IOException, XMLStreamException {

        // initialize Bean
        Bean bean = new Bean();
        bean.setCount(1000000);
        bean.setDbURL("jdbc:mysql://localhost:3306/test");
        bean.setDbUser("root");
        bean.setDpPassword("root");

        connect(bean.getDbURL(), bean.getDbUser(), bean.getDpPassword());

        long startTime = System.nanoTime();

        // record - count rows in db now
        int record = checkRecords();
        if (record != 0) {
            deleteRecords();
            writeRecords(bean.getCount());
        } else {
            writeRecords(bean.getCount());
        }
        int[] result = getAllRecords(bean.getCount());

        Path outfile1 = Paths.get("./1.xml");
        XMLUtil xmlutil = new XMLUtil();
        xmlutil.writeToXml(outfile1, result);

        xmlutil.transformXML("newForm.xsl");

        List<Integer> integers = xmlutil.parseXML("./2.xml");
        long sum = integers.stream().mapToLong(i -> i).sum();

        long endTime = System.nanoTime();
        double duration = (endTime - startTime)/1000000000.0;
        System.out.println("Sum all elements = " + sum +"; executiom time in seconds = " + duration);

    }

    //method to connect DB
    public static void connect(String url, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //method for write data to db
    public static void writeRecords(int count) {
        try (PreparedStatement stmt = con.prepareStatement("INSERT INTO test (field) VALUES (?)");) {
            con.setAutoCommit(false);
            for (int i = 1; i <= count; i++) {
                stmt.setInt(1, i);
                stmt.addBatch();
            }
            stmt.executeBatch();
            con.commit();
            System.out.println("Data is inserted");
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    public static int checkRecords() {
        int recordCount = 0;
        try (Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM TEST");
            rs.next();
            recordCount = rs.getInt(1);
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
        return recordCount;
    }

    public static void deleteRecords() {
        try (Statement stmt = con.createStatement();) {
            stmt.execute("TRUNCATE TABLE test");
            System.out.println("Delete data");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getAllRecords(int size) {
        int recordCount = 0;
        int[] arr = new int[size];
        int i = 0;
        try (Statement stmt = con.createStatement();) {
            ResultSet rs = stmt.executeQuery("SELECT FIELD FROM TEST");
            while (rs.next()) {
                arr[i] = rs.getInt(1);
                i++;
            }

        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
        return arr;
    }


}

