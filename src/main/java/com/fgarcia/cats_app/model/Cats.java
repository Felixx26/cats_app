/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fgarcia.cats_app.model;

import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author felix
 */
public class Cats {
    
    Dotenv dotenv = Dotenv.load();
    
    private String id;
    private String url;
    private String apikey = dotenv.get("API_KEY");
    private String image;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    
    
}
