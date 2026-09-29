package lk.melodybeatsapp.model;

import java.math.BigDecimal;

public class DigitalTrack extends MusicItem {
    public DigitalTrack(int itemId, String title, String artist, Genre genre, BigDecimal price) {
        super(itemId, title, artist, genre, price);
    }

    @Override
    public String getItemFormat() { return "DIGITAL_TRACK"; }
}
