package DAO;

import Classes.Record;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO {

    private Connection connection;

    public RecordDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(String title, String full_text, Date date, Time time, int idUser, String type, Date editDate, Time editTime) throws SQLException {
        String sql = "INSERT INTO record (title, full_text, date, time, idUser, type, edit_date, edit_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, full_text);
            stmt.setDate(3, date);
            stmt.setTime(4, time);
            stmt.setInt(5, idUser);
            stmt.setString(6, type);
            stmt.setDate(7, editDate);
            stmt.setTime(8, editTime);
            stmt.executeUpdate();
        }
    }

    public void createRecords(List<Record> records) throws SQLException {
        for (Record record : records) {
            create(
               record.getTitle(), 
               record.getFullText(), 
               record.getDate(), 
               record.getTime(), 
               record.getIdUser(), 
               record.getType(), 
               record.getEditDate(), 
               record.getEditTime()
            );
        }
    }

    public List<Record> read() throws SQLException {
        List<Record> records = new ArrayList<>();

        String sql = "SELECT * FROM record " +
                     "ORDER BY date ASC ";
        
        try (
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
            ) {
            while (rs.next()) {
                Record record = new Record();

                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));

                records.add(record);
            }
        }
        return records;
    }

    public void update(Record record) throws SQLException {
        String sql = "UPDATE record SET title = ?, full_text = ?, " + 
                     "date = ?, time = ?, idUser = ?, type = ?, " +
                     "edit_date = ?, edit_time = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, record.getTitle());
            stmt.setString(2, record.getFullText());
            stmt.setDate(3, record.getDate());
            stmt.setTime(4, record.getTime());
            stmt.setInt(5, record.getIdUser());
            stmt.setString(6, record.getType());
            stmt.setDate(7, record.getEditDate());
            stmt.setTime(8, record.getEditTime());
            stmt.setInt(9, record.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM record WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Record getRecordById(int id) throws SQLException {
        String query = "SELECT * FROM record WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Record record = new Record();

                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));

                return record;

            } else {
                return null;
            }
        }
    }

    public List<Record> getRecordsByUserId(int user_id) throws SQLException {
        List<Record> records = new ArrayList<>();

        String query = "SELECT * FROM record WHERE idUser = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));
                records.add(record);
            }            
        }

        return records;
    }

    public List<Record> getRecordsByUserIdAnywhere(int user_id, String search) throws SQLException {
        List<Record> records = new ArrayList<>();

        String query = "SELECT * FROM record WHERE idUser = ? " +
                       "AND (" +
                       "LOWER(title) LIKE LOWER(?) " +
                       "OR LOWER(full_text) LIKE LOWER(?) " +
                       "OR LOWER(type) LIKE LOWER(?) " +
                       ") " +
                       "ORDER BY date ASC ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setString(2,  "%"+search+"%");
            stmt.setString(3,  "%"+search+"%");
            stmt.setString(4,  "%"+search+"%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));
                records.add(record);
            }            
        }

        return records;
    }

    public List<Record> getRecordsByUserIdTitle(int user_id, String search) throws SQLException {
        List<Record> records = new ArrayList<>();

        String query = "SELECT * FROM record WHERE idUser = ? " +
                       "AND LOWER(title) LIKE LOWER(?) " +
                       "ORDER BY date ASC ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setString(2,  "%"+search+"%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));
                records.add(record);
            }            
        }

        return records;
    }

    public List<Record> getRecordsByUserIdType(int user_id, String search) throws SQLException {
        List<Record> records = new ArrayList<>();

        String query = "SELECT * FROM record WHERE idUser = ? " +
                       "AND LOWER(type) LIKE LOWER(?) " +
                       "ORDER BY date ASC ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setString(2,  "%"+search+"%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));
                records.add(record);
            }            
        }

        return records;
    }

    public List<Record> getRecordsByUserIdFromDate(int user_id, String search) throws SQLException {
        List<Record> records = new ArrayList<>();

        String query = "SELECT * FROM record WHERE idUser = ? " +
                       "AND date >= ? " +
                       "ORDER BY date ASC ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            stmt.setString(2,  search);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt("id"));
                record.setTitle(rs.getString("title"));
                record.setFullText(rs.getString("full_text"));
                record.setDate(rs.getDate("date"));
                record.setTime(rs.getTime("time"));
                record.setIdUser(rs.getInt("idUser"));
                record.setType(rs.getString("type"));
                record.setEditDate(rs.getDate("edit_date"));
                record.setEditTime(rs.getTime("edit_time"));
                records.add(record);
            }            
        }

        return records;
    }
}