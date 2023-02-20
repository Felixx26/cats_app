/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.fgarcia.cats_app;

import com.fgarcia.cats_app.service.CatsService;
import com.fgarcia.cats_app.model.Cats;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author felix
 */
public class Cats_app {

    public static void main(String[] args) throws IOException {
        int option_menu = -1;
        String[] buttons = {" 1. See cats"," 2. See favorites", " 3. Exit"};
        
        do {            
            String option = (String) JOptionPane.showInputDialog(null,
                    "Cats Java",
                    "Main menu",
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    buttons,
                    buttons[0]);
            for (int i = 0; i < buttons.length; i++) {
                if(option == null){
                    option_menu = 2;
                } else if(option.equals(buttons[i])){
                    option_menu = i;
                }
            }
            
            switch (option_menu) {
                case 0 -> {
                    CatsService.seeCats();
                }
                case 1 -> {
                    Cats cat = new Cats();
                    CatsService.seeFavorites(cat.getApikey());
                }
                default -> {
                }
            }
            
        } while (option_menu != 2);
        
    }
}
