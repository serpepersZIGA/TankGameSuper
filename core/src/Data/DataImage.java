package Data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.unit.UnitPattern;

import java.io.File;
import java.io.IOException;

public class DataImage {
    public static TextureAtlas TextureAtl;
    public Texture buff;
    public DataImage(){
        TextureAtl = new TextureAtlas();
        ImageIncilization();
//        LoadImage("Buffer.png");
//        LoadImage("Buffer2.png");
    }
    private String NameAdd(String string){
        String name = "";
        for(int i=0;i<string.length();i++){
            char c = string.charAt(i);
            switch(c){
                case '/':
                    name = "";
                    break;
                case '.':
                    return name;
                default:
                    name += c;
                    break;
            }

        }
        return name;
    }
    private void LoadImage(String image){
        buff = new Texture(image);
        TextureRegion texture = new TextureRegion(buff,buff.getWidth(),buff.getHeight());
        TextureAtl.addRegion(NameAdd(NameAdd(image)),texture);
    }
    public void ImageIncilization(){
        String txt = "";
        String c;
        FileHandle file = Gdx.files.classpath("assets.txt");
        char str;

        //System.out.println(file.readString());
        for(int i = 0;i<file.readString().length();i++){
            str = file.readString().charAt(i);
            switch (str){
                case '\n':
                    c = txt.substring(txt.length()-3);
                    if(c.equals("png") || c.equals("jpg")){
                        LoadImage(txt);
                    }
                    txt = "";
                    break;

                default:
                    txt += str;
                    break;
            }
        }
    }
}
