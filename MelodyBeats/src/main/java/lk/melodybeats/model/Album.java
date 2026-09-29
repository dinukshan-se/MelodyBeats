package lk.melodybeatsapp.model;

import java.math.BigDecimal;

public class Album extends MusicItem {
    public Album(int itemId, String title, String artist, Genre genre, BigDecimal price) {
        super(itemId, title, artist, genre, price);
    }

    @Override
    public String getItemFormat() { return "PHYSICAL_ALBUM"; }
}
