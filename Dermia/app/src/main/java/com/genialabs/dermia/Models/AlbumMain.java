package com.genialabs.dermia.Models;

public class AlbumMain {
    private String id_album,bodypart,albumname,albumplus;
    public AlbumMain(String id, String albumname,String bodyPart,String albumplus){
        this.id_album=id;
        this.bodypart=bodyPart;
        this.albumname=albumname;
        this.albumplus=albumplus;
    }

    public String getId_album() {
        return id_album;
    }

    public void setId_album(String id_album) {
        this.id_album = id_album;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAlbumplus() {
        return albumplus;
    }

    public void setAlbumplus(String albumplus) {
        this.albumplus = albumplus;
    }
}
