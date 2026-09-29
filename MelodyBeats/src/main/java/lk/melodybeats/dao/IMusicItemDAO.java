package lk.melodybeatsapp.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import lk.melodybeatsapp.model.MusicItem;

public interface IMusicItemDAO {
    List<MusicItem> getAllMusicItems() throws SQLException;
    List<MusicItem> searchByGenre(Integer genreId) throws SQLException;
    void addMusicItem(String title, String artist, int genreId, String format, BigDecimal price) throws SQLException;
    void updatePrice(int itemId, BigDecimal price) throws SQLException;
}
