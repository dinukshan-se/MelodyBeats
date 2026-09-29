package lk.melodybeats.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lk.melodybeatsapp.dao.IGenreDAO;
import lk.melodybeatsapp.model.Genre;
import lk.melodybeats.util.DBConnection;

public class GenreDAO implements IGenreDAO {
    @Override
    public List<Genre> getAllGenres() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT GenreID, GenreName FROM GENRE ORDER BY GenreName";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) genres.add(new Genre(rs.getInt(1), rs.getString(2)));
        }
        return genres;
    }

    @Override
    public Integer getGenreIDByName(String genreName) throws SQLException {
        String sql = "SELECT GenreID FROM GENRE WHERE GenreName=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, genreName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }
}
