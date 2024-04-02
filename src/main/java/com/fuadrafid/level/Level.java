package com.fuadrafid.level;

import com.fuadrafid.entities.Entity;
import com.fuadrafid.entities.Mob;
import com.fuadrafid.entities.Player;
import com.fuadrafid.gamepack.Game;
import com.fuadrafid.gfx.Screen;
import com.fuadrafid.level.tiles.Tile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Level {

    private long playtime = System.currentTimeMillis();
    public static byte[] tiles;
    public static int width;
    public int height;
    public ArrayList<Entity> entities = new ArrayList<>();
    public Player player;
    //int TimesPlayed=0;
    public static long KeyTime;
    public int keyX, keyY;
    static Clip clip = null;
    public boolean dead = false, key = false;
    static int clipSize;
    public static boolean MusicOn = true;
    public static float vol = 0;

    public static synchronized void playSound() {
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Objects.requireNonNull(Level.class.getResourceAsStream("/music.WAV")));

                clip.open(inputStream);

                Level.clipSize = (int) (clip.getMicrosecondLength() / 1000);
                clip.start();


            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }


    public Level(int width, int height) {
        tiles = new byte[width * height];
        Level.width = width;
        this.height = height;

        this.generateLevel();
        playSound();

        player = new Player(this, Game.plx << 3, Game.ply << 3, Game.input);
        this.addEntity(player);

    }

    public void generateLevel() {


        if (Game.levelNo != 0) {

            entities.remove(player);
            Game.plx = 8;
            Game.ply = 4;
            player = new Player(this, Game.plx, Game.ply, Game.input);
            this.addEntity(player);

        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[x + y * width] = Tile.STONE.getId();
            }

        }
        tiles[1 + width] = Tile.GRASS.getId();
        if (Game.levelNo == 0) {


            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    tiles[x + y * width] = Tile.GRASS.getId();
                }

            }

            for (int y = 0; y < height; y++) {
                tiles[15 + y * width] = Tile.STONE.getId();
                tiles[17 + y * width] = Tile.STONE.getId();
            }
            for (int x = 0; x < width; x++) {
                tiles[x + 9 * width] = Tile.STONE.getId();
                tiles[x + 11 * width] = Tile.STONE.getId();
            }
            tiles[16 + 9 * width] = Tile.GRASS.getId();
            tiles[16 + 11 * width] = Tile.GRASS.getId();
            tiles[15 + 10 * width] = Tile.GRASS.getId();
            tiles[17 + 10 * width] = Tile.GRASS.getId();
            return;
        }


        String s1 = "level" + Game.levelNo + ".maze";
        int i1 = 1, i2 = 1;
        try {
            File file = new File(s1);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();

                StringTokenizer t = new StringTokenizer(s);
                int i = 0, j = 0;
                while (t.hasMoreTokens()) {
                    i = Integer.parseInt(t.nextToken());
                    j = Integer.parseInt(t.nextToken());
                }
                tiles[i + j * width] = Tile.GRASS.getId();
                i1 = i;
                i2 = j;
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 10; i++) {
            changeKey();
        }
        if (!key && !Mob.write) {
            tiles[i1 + i2 * width] = Tile.KEY.getId();
        }


    }

    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }
        //System.out.println(Level.vol);

        if (!MusicOn) {
            clip.stop();
            //System.out.println("off");
            playtime = 0;
        }
        if (MusicOn && clip!=null && clip.isOpen()) {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(Level.vol);
        }
        if (System.currentTimeMillis() - playtime >= clipSize + 10000 && MusicOn) {
            playSound();
            playtime = System.currentTimeMillis();

        }

    }

    public void renderTiles(Screen screen, int xOffset, int yOffset) {
        if (xOffset < 0)
            xOffset = 0;
        if (xOffset > ((width << 3) - screen.width))
            xOffset = ((width << 3)) - screen.width;
        if (yOffset < 0)
            yOffset = 0;
        if (yOffset > ((height << 3) - screen.height))
            yOffset = ((height << 3) - screen.height);

        screen.setOffset(xOffset, yOffset);

        for (int y = (yOffset >> 3); y < (yOffset + screen.height >> 3) + 1; y++) {
            for (int x = (xOffset >> 3); x < (xOffset + screen.width >> 3) + 1; x++) {
                getTile(x, y).render(screen, this, x << 3, y << 3);
            }
        }


    }

    public void renderEntities(Screen screen) {
        for (Entity e : entities) {
            e.render(screen);

        }
    }

    public Tile getTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height)
            return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }


    public void changeKey() {

        int x1 = 4;

        int yy = (int) (Math.random() * 20) + (int) (Math.random() * 20) + (int) (Math.random() * 20);
        int xx1 = 0;    //(int)(Math.random()*20)+(int)(Math.random()*20)+(int)(Math.random()*20);
        int xx2 = 30;
        if (player.x / 8 < 30) {
            //System.out.println(player.x/8);
            xx1 = 30;
            xx2 = 60;
        }
        //System.out.printf("%d %d\n",ri,rj);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[x + y * width] == Tile.GRASS.getId()) {
                    if ((int) (Math.random() * 100) < x1 && x > xx1 && x < xx2 && y > yy) {//System.out.println((int)Math.random()*1000);
                        tiles[x + y * width] = Tile.KEY.getId();
                        tiles[keyX + keyY * width] = Tile.GRASS.getId();
                        keyX = x;
                        keyY = y;
                        //System.out.printf("%d %d\n",keyX,keyY);
                        key = true;
                        return;
                    }
                }
            }

        }

    }


}