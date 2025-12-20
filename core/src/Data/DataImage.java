package Data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DataImage {
    public static TextureAtlas TextureAtl;
    public Sprite tower_player, tower_player_auxiliary_1,tower_enemy, tower_enemy_auxiliary_1;
    public Sprite corpus_player,corpus_track_remount_enemy,corpus_track_soldat_enemy,corpus_enemy,helicopter_enemy_t1,
            helicopter_blade,pepper_object_map;
    public Sprite build_1, big_build_wood_1;
    public Sprite track_enemy_1lvl;
    public Sprite grass,dirt_2,dirt_3,dirt_4;
    public Sprite asphalt1;
    public Sprite soldat_1,ExpBuild,frameInventory,InventoryBackground;
    public Sprite AK74,FlameGun,BottleFlame,BottleAcid,BulletPenetration,BulletFragmentation,Medicine;
    public Texture buff;
    public DataImage(){
        TextureAtl = new TextureAtlas();
        LoadImage("image/item/bottleFlame.png","BottleFlame");
        LoadImage("image/item/bottleAcid.png","BottleAcid");
        LoadImage("image/item/penetration.png","BulletPenetration");
        LoadImage("image/item/fragmention.png","BulletFragmentation");
        LoadImage("image/enemy/machine_enemy_1lvl.png","corpus_track_soldat_enemy");
        LoadImage("image/item/frameSlot.png","frameInventory");
        LoadImage("image/item/InventoryBackground.png","InventoryBackground");
        LoadImage("image/item/AK74.png","AK74");
        LoadImage("image/item/flamegun.png","FlameGun");
        LoadImage("image/item/Medicine.png","Medicine");
        LoadImage("image/object_map/pepper.png","pepper_object_map");
        LoadImage("image/enemy/corpus_enemy_medic_1.png","corpus_track_remount_enemy");
        LoadImage("image/player/tower_player_1.png","tower_player");
        LoadImage("image/enemy/tower_enemy_1.png","tower_enemy");
        LoadImage("image/player/corpus_player_many_tower_1.png","corpus_player");
        LoadImage("image/enemy/corpus_enemy_many_tower_1.png","corpus_enemy");
        LoadImage("image/build/home_3.png","build_1");
        LoadImage("image/build/big_build_wood_1.png","big_build_wood_1");
        LoadImage("image/build/AssetExp.png","ExpBuild");
        LoadImage("image/other/grass_256_1.png","grass");
        LoadImage("image/other/dirt_256_1.png","dirt_2");
        LoadImage("image/other/dirt_256_2.png","dirt_3");
        LoadImage("image/other/dirt_2.png","dirt_4");
        LoadImage("image/other/asphalt1.png","asphalt1");
        LoadImage("image/other/machinegun.png","machinegun");
        LoadImage("image/player/tower_auxiliart_player_1.png","tower_player_auxiliary_1");
        LoadImage("image/enemy/tower_auxiliart_enemy_1.png","tower_enemy_auxiliary_1");
        LoadImage("image/infantry/soldat_enemy.png","soldat_1");
        LoadImage("image/enemy/helicopter_corpus.png","helicopter_enemy_t1");
        LoadImage("image/enemy/medic_machine_enemy.png","track_enemy_1lvl");
        LoadImage("image/other/blade_helicopter.png","helicopter_blade");
        LoadImage("image/build/Build2.png","Build2");

        LoadImage("image/item/armorB1.png","armorB1");

        LoadImage("buffer2.png","Buffer");
        LoadImage("buffer.png","Buffer2");
    }
    private Texture LoadImage(String image){
        buff = new Texture(image);
        return buff;
    }
    private void LoadImage(String image,String Name){
        buff = new Texture(image);
        TextureRegion texture = new TextureRegion(buff,buff.getWidth(),buff.getHeight());
        TextureAtl.addRegion(Name,texture);
    }
}
