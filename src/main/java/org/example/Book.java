package org.example;

public class Book {
    private Integer id;
    private String szerzo;
    private String cim;
    private Integer oldal;

    public Book(int id, String szerzo, String cim, int oldal) {
        this.id = id;
        this.szerzo = szerzo;
        this.cim = cim;
        this.oldal = oldal;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setSzerzo(String szerzo) {
        this.szerzo = szerzo;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public void setOldal(int oldal) {
        this.oldal = oldal;
    }

    public int getId() {
        return id;
    }

    public String getSzerzo() {
        return szerzo;
    }

    public String getCim() {
        return cim;
    }

    public int getOldal() {
        return oldal;
    }

}
