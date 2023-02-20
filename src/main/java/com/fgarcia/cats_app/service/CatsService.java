/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fgarcia.cats_app.service;

import com.fgarcia.cats_app.model.Cats;
import com.fgarcia.cats_app.model.CatsFav;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author felix
 */
public class CatsService {

    public static void seeCats() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        
        String jsonString = response.body().string();
        
        String jsonRes = jsonString.substring(1, jsonString.length()-1);
        
        Gson gson = new Gson();
        Cats cat = gson.fromJson(jsonRes, Cats.class);
        
        Image image;
        try {
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);
            
            ImageIcon catIcon = new ImageIcon(image);
            
            if(catIcon.getIconWidth() > 800){
                
                Image icon = catIcon.getImage();
                
                Image iconModified = icon.getScaledInstance(800, 800, Image.SCALE_SMOOTH);
                
                catIcon.setImage(iconModified);
            }
            
            String menu = """
                          Options: 
                           1. See other image 
                           2. Favorite 
                           3. Back 
                          """;
            String[] buttons = {"See other image","Favorite","Back"};
            String cat_id = cat.getId();
            
            String option = (String) JOptionPane.showInputDialog(null,
                    menu,
                    cat_id,
                    JOptionPane.INFORMATION_MESSAGE,
                    catIcon,
                    buttons,
                    buttons[0]);
            
            int selection = -1;
            
            for (int i = 0; i < buttons.length; i++) {
                if(option.equals(buttons[i])){
                    selection = i;
                }
            }
            
            switch (selection) {
                case 0 -> seeCats();
                case 1 -> favoriteCat(cat);
                default -> {
                }
            }
            
        } catch (HeadlessException | IOException e) {
            System.out.println(e);
        }
        
    }
    
    public static void favoriteCat(Cats cat) {
        
        try {
            
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"image_id\": \""+cat.getId()+"\"}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("x-api-key", cat.getApikey())
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            
            if(!response.isSuccessful()){
                response.body().close();
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
    }
    
    public static void seeFavorites(String api_key) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("GET", null)
                    .addHeader("x-api-key", api_key)
                    .build();
            Response response = client.newCall(request).execute();
            
            if(!response.isSuccessful()){
                response.body().close();
            }
            
            String rawRes = response.body().string();
            
            Gson gson = new Gson();
            
            CatsFav[] catsArr = gson.fromJson(rawRes, CatsFav[].class);

            if(catsArr.length > 0){
                int min = 1;
                int max = catsArr.length;
                
                int randomNumber = (int) (Math.random() * ((max-min)+1)) + min;
                
                int index = randomNumber-1;
                CatsFav catFav = catsArr[index];
                
                Image image;
                URL url = new URL(catFav.getImage().getUrl());
                image = ImageIO.read(url);

                ImageIcon catIcon = new ImageIcon(image);

                if (catIcon.getIconWidth() > 800) {

                    Image icon = catIcon.getImage();

                    Image iconModified = icon.getScaledInstance(800, 800, Image.SCALE_SMOOTH);

                    catIcon.setImage(iconModified);
                }

                String menu = """
                                  Options: 
                                   1. See other image 
                                   2. Delete favorite 
                                   3. Back 
                                  """;
                String[] buttons = {"See other image", "Delete favorite", "Back"};
                String cat_id = catFav.getId();

                String option = (String) JOptionPane.showInputDialog(null,
                        menu,
                        cat_id,
                        JOptionPane.INFORMATION_MESSAGE,
                        catIcon,
                        buttons,
                        buttons[0]);

                int selection = -1;

                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                    }
                }

                switch (selection) {
                    case 0 ->
                        seeFavorites(api_key);
                    case 1 ->
                        deleteFavorite(catFav);
                    default -> {
                    }
                }

                
            }
        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void deleteFavorite(CatsFav catFav) {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+catFav.getId())
                    .method("DELETE", body)
                    .addHeader("x-api-key", catFav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
            
            if(!response.isSuccessful()){
                response.body().close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
