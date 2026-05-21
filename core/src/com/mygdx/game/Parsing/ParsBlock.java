package com.mygdx.game.Parsing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.game.Inventory.Inventory;
import com.mygdx.game.Inventory.Item;
import com.mygdx.game.Inventory.TegItem;
import com.mygdx.game.block.UpdateBlock;
import com.mygdx.game.unit.ClassUnit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.mygdx.game.block.Block.BlockID;


public class ParsBlock {

    public static void Pars() {
        FileHandle[] files = Gdx.files.internal("ContentGlobal/Block").list();
        if (files.length == 0) {
            AddBuilding();
            files = Gdx.files.internal("ContentGlobal/Block").list();
        }
        for (FileHandle file : files) {
            //System.out.println(file.path());
            try {
                JSON(file.path());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


    }


    public static void JSON(String JSON) throws IOException {
        FileHandle file = Gdx.files.internal(JSON);
        String ID = file.name().replace(".json", "");
        String TxT = file.readString();
        // Чтение JSON-файла и создание объекта Person
        ObjectMapper objectMapper = new ObjectMapper();

        buffBlock obj = objectMapper.readValue(TxT, buffBlock.class);
        String Image = obj.Image;
        int ind = obj.ind;
        boolean GrassGrow = obj.GrassGrow;
        BlockID.put(ind, new UpdateBlock(Image,ind,GrassGrow,ID));





    }
    public static void AddBuilding(){
        new File("ContentGlobal").mkdirs();
        new File("ContentGlobal/Block").mkdirs();
        File Asphalt = new File("ContentGlobal/Block/Asphalt.json");
        File Void = new File("ContentGlobal/Block/Void.json");

        File Dirt = new File("ContentGlobal/Block/Dirt.json");

        File Grass = new File("ContentGlobal/Block/Grass.json");
        String data = "{\n" +
                "  \"Image\": \"asphalt1\",\n" +
                "  \"ind\":3,\n" +
                "  \"GrassGrow\": false\n" +
                "}";
        Create(Asphalt,data);

        data = "{\n" +
                "  \"Image\": \"Buffer\",\n" +
                "  \"ind\": 2,\n" +
                "  \"GrassGrow\": false\n" +
                "}";
        Create(Void,data);

        data = "{\n" +
                "  \"Image\": \"dirt_256_1\",\n" +
                "  \"ind\": 0,\n" +
                "  \"GrassGrow\":true\n" +
                "}";
        Create(Dirt,data);

        data = "{\n" +
                "  \"Image\": \"grass_256_1\",\n" +
                "  \"ind\": 1,\n" +
                "  \"GrassGrow\":false\n" +
                "}";
        Create(Grass,data);


    }
    private static void Create(File file, String str){
        try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        try {
            PrintWriter out = new PrintWriter(file);
            out.println(str);
            out.close();
        } catch (IOException ignored) {
        }

    }


}
