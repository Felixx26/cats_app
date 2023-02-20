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
public class FavoriteCats {
    
    Dotenv dotenv = Dotenv.load();
    
    private String id;
    private String image_id;
    private String apikey = dotenv.get("API_KEY");
    private ImageX image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public ImageX getImage() {
        return image;
    }

    public void setImage(ImageX image) {
        this.image = image;
    }
    
    
    
}
