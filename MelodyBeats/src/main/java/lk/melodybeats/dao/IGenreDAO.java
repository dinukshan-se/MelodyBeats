package lk.melodybeatsapp.dao;

import java.sql.SQLException;
import java.util.List;
import lk.melodybeatsapp.model.Genre;

public interface IGenreDAO {
    List<Genre> getAllGenres() throws SQLException;
    Integer getGenreIDByName(String genreName) throws SQLException;
}
