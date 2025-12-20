package com.mygdx.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.main.ClientMain;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class InputWindow{
    public static JButton Button;
    public static JFrame frame;
    public InputWindow(){
        // Создаем новое окно
        frame = new JFrame("Input Window");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 320);

        // Создаем текстовое поле
        // Создаем кнопку для сохранения текста
        Button = new JButton("Save");
        //frame.add(button);

        // Добавляем текстовое поле и кнопку в окно
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("bufferIP.txt"))) {
            result.append(br.readLine());

        } catch (IOException e) {
            e.printStackTrace();
            try {
                File file = new File("bufferIP.txt");
                file.createNewFile();
            } catch (IOException ignored) {
            }
            File myFile = new File("bufferIP.txt");
            try {
                myFile.createNewFile();
            } catch (IOException ignored) {
            }
            String data = "127.0.0.1";
            //Path file = Paths.get(path);
            try {
                PrintWriter out = new PrintWriter("bufferIP.txt");
                out.println(data);
                out.close();
            } catch (IOException ignored) {
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader("bufferIP.txt"));
                result.append(br.readLine());
            } catch (IOException ignored) {
            }
        }


        ClientMain.IP = result.toString();
        JTextField textField = new JTextField(ClientMain.IP,40);

        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(Button, "South");

        // Создаем переменную для хранения текста

        // Добавляем обработчик событий для кнопки
        Button.addActionListener(e -> {
            ClientMain.IP = textField.getText();
            FileHandle file = Gdx.files.local("bufferIP.txt");
            file.writeString(ClientMain.IP, false);
            //"127.0.0.1"

        });

        // Делаем окно видимым
        frame.setVisible(true);
    }
}
