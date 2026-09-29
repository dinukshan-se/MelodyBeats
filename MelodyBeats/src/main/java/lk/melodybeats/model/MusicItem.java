package lk.melodybeatsapp.model;

import java.math.BigDecimal;

public abstract class MusicItem {
    private final int itemId;
    private final String title;
    private final String artist;
    private final Genre genre;
    private BigDecimal price;

    protected MusicItem(int itemId, String title, String artist, Genre genre, BigDecimal price) {
        this.itemId = itemId;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.price = price;
    }

    public int getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public Genre getGenre() { return genre; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public abstract String getItemFormat();

    @Override
    public String toString() {
        return itemId + " - " + title + " - " + artist + " (Rs. " + price + ")";
    }
}
