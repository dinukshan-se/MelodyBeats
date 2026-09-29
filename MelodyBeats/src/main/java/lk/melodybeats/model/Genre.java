package lk.melodybeatsapp.model;

public class Genre {
    private final int genreId;
    private final String genreName;

    public Genre(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public int getGenreId() { return genreId; }
    public String getGenreName() { return genreName; }

    @Override
    public String toString() { return genreName; }
}
