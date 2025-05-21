package com.example.storyweave.game;

import java.util.Random;

public class GameCodeGenerator {
    //random code****************************
    public static String generateSixDigitCode(){

        String code = "";
        char[] uppercaseLetters = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        for (int i = 0; i < 6; i++){
            Random random = new Random();
            if (Math.round(random.nextDouble()) == 0){
                code += uppercaseLetters[random.nextInt(uppercaseLetters.length)];
            } else {
                code += numbers[random.nextInt(numbers.length)];
            }
        }
        return code;
    }
    //random code****************************
}
