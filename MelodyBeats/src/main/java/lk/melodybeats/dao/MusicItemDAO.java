package lk.melodybeats.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lk.melodybeatsapp.dao.IMusicItemDAO;
import lk.melodybeatsapp.model.Album;
import lk.melodybeatsapp.model.DigitalTrack;
import lk.melodybeatsapp.model.Genre;
import lk.melodybeatsapp.model.MusicItem;
import lk.melodybeats.util.DBConnection;

public class MusicItemDAO implements IMusicItemDAO {
    @Override
    public List<MusicItem> getAllMusicItems() throws SQLException { return searchByGenre(null); }

    @Override
    public List<MusicItem> searchByGenre(Integer genreId) throws SQLException {
        List<MusicItem> items = new ArrayList<>();
        String base = "SELECT m.ItemID,m.Title,m.Artist,m.ItemFormat,m.Price,g.GenreID,g.GenreName "
                + "FROM MUSIC_ITEM m JOIN GENRE g ON m.GenreID=g.GenreID";
        String sql = base + (genreId == null ? " ORDER BY m.ItemID" : " WHERE m.GenreID=? ORDER BY m.Title");
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (genreId != null) ps.setInt(1, genreId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) items.add(mapItem(rs));
            }
        }
        return items;
    }

    @Override
    public void addMusicItem(String title, String artist, int genreId, String format, BigDecimal price)
            throws SQLException {
        String sql = "INSERT INTO MUSIC_ITEM(Title,Artist,GenreID,ItemFormat,Price) VALUES(?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title.trim());
            ps.setString(2, artist.trim());
            ps.setInt(3, genreId);
            ps.setString(4, format);
            ps.setBigDecimal(5, price);
            ps.executeUpdate();
        }
    }

    @Override
    public void updatePrice(int itemId, BigDecimal price) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE MUSIC_ITEM SET Price=? WHERE ItemID=?")) {
            ps.setBigDecimal(1, price);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        }
    }

    private MusicItem mapItem(ResultSet rs) throws SQLException {
        Genre genre = new Genre(rs.getInt("GenreID"), rs.getString("GenreName"));
        if ("PHYSICAL_ALBUM".equals(rs.getString("ItemFormat"))) {
            return new Album(rs.getInt("ItemID"), rs.getString("Title"), rs.getString("Artist"),
                    genre, rs.getBigDecimal("Price"));
        }
        return new DigitalTrack(rs.getInt("ItemID"), rs.getString("Title"), rs.getString("Artist"),
                genre, rs.getBigDecimal("Price"));
    }
}
