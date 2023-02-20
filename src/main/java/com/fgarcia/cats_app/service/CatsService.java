/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fgarcia.cats_app.service;

import com.fgarcia.cats_app.model.Cats;
import com.fgarcia.cats_app.model.FavoriteCats;
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

    private static final String BASE_URL = "https://api.thecatapi.com/v1/";
    private static final String SEARCH_ENDPOINT = BASE_URL + "images/search";
    private static final String FAVORITE_ENDPOINT = BASE_URL + "favourites";
    private static final String FAVORITE_MENU = """
                                               Options: 
                                                1. See other image 
                                                2. Delete favorite 
                                                3. Back 
                                               """;
    private static final String RANDOM_CAT_MENU = """
                                                  Options: 
                                                   1. See other image 
                                                   2. Favorite 
                                                   3. Back 
                                                  """;

    public static void seeCats() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(SEARCH_ENDPOINT).get()
                .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        jsonData = jsonData.substring(1, jsonData.length() - 1);

        Gson gson = new Gson();
        Cats cat = gson.fromJson(jsonData, Cats.class);

        Image image;
        try {
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);

            ImageIcon catImageIcon = new ImageIcon(image);

            if (catImageIcon.getIconWidth() > 800) {

                Image background = catImageIcon.getImage();

                Image modified = background.getScaledInstance(800, 800, Image.SCALE_SMOOTH);

                catImageIcon.setImage(modified);
            }
            String[] buttons = {"See other image", "Favorite", "Back"};
            String cat_id = cat.getId();

            String option = (String) JOptionPane.showInputDialog(null,
                    RANDOM_CAT_MENU,
                    cat_id,
                    JOptionPane.INFORMATION_MESSAGE,
                    catImageIcon,
                    buttons,
                    buttons[0]);

            int selection = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (option == null) {
                    selection = 2;
                } else if (option.equals(buttons[i])) {
                    selection = i;
                }
            }

            switch (selection) {
                case 0 ->
                    seeCats();
                case 1 ->
                    favoriteCat(cat);
                default -> {
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void favoriteCat(Cats cat) {

        try {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\"image_id\": \"" + cat.getId() + "\"}");
            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT).post(body)
                    .addHeader("x-api-key", cat.getApikey())
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                response.body().close();
            }

        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void seeFavoriteCat(String api_key) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites").get()
                    .addHeader("x-api-key", api_key).build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                response.body().close();
            }

            String jsonData = response.body().string();

            Gson gson = new Gson();

            FavoriteCats[] catsArr = gson.fromJson(jsonData, FavoriteCats[].class);

            if (catsArr.length > 0) {
                int min = 1;
                int max = catsArr.length;

                int randomNumber = (int) (Math.random() * ((max - min) + 1)) + min;

                int index = randomNumber - 1;
                FavoriteCats favoriteCat = catsArr[index];

                Image image;
                URL url = new URL(favoriteCat.getImage().getUrl());
                image = ImageIO.read(url);

                ImageIcon catIcon = new ImageIcon(image);

                if (catIcon.getIconWidth() > 800) {

                    Image icon = catIcon.getImage();

                    Image iconModified = icon.getScaledInstance(800, 800, Image.SCALE_SMOOTH);

                    catIcon.setImage(iconModified);
                }
                String[] buttons = {"See other image", "Delete favorite", "Back"};
                String cat_id = favoriteCat.getId();

                String option = (String) JOptionPane.showInputDialog(null,
                        FAVORITE_MENU,
                        cat_id,
                        JOptionPane.INFORMATION_MESSAGE,
                        catIcon,
                        buttons,
                        buttons[0]);

                int selection = -1;

                for (int i = 0; i < buttons.length; i++) {

                    if (option == null) {
                        selection = 2;
                    } else if (option.equals(buttons[i])) {
                        selection = i;
                    }
                }

                switch (selection) {
                    case 0 ->
                        seeFavoriteCat(api_key);
                    case 1 ->
                        deleteFavorite(favoriteCat);
                    default -> {
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void deleteFavorite(FavoriteCats favoriteCat) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT + favoriteCat.getId()).delete()
                    .addHeader("x-api-key", favoriteCat.getApikey())
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                response.body().close();
            }

        } catch (IOException ex) {
            Logger.getLogger(CatsService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
