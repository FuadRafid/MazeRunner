package com.fuadrafid.entities;

import com.fuadrafid.gamepack.Game;
import com.fuadrafid.level.Level;
import com.fuadrafid.level.tiles.Tile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Mob extends Entity {

    protected String name;
    protected int speed;
    protected int numSteps = 0;
    protected boolean isMoving;
    //    protected int movingDir = 1;
//    protected int scale = 1;
    public static boolean write;
    File f = new File("level" + Game.levelNo + ".maze");

    File f1 = new File("hs.maze");

    public Mob(Level level, String name, int x, int y, int speed) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        if (!f.exists())
            write = true;
    }

    public void move(int xa, int ya) {
              /*  if (xa != 0 && ya != 0) {
                        move(xa, 0);
                        move(0, ya);
                        numSteps -= 1;
                        return;
                }*/
        numSteps += 1;
        if (!hasCollided(xa, ya)) {

            x += xa * speed;
            y += ya * speed;
            //System.out.printf("%d %d\n",x,y);
        }
    }

    public abstract boolean hasCollided(int xa, int ya);

    public boolean isSolidTile(int xa, int ya) {
        if (level == null) return false;
        if (xa > 0)
            xa = xa + 7;
        if (ya > 0)
            ya = ya + 7;
        Tile newTile = level.getTile((this.x + xa) >> 3, (this.y + ya) >> 3);

        if (newTile.getId() == Tile.KEY.getId()) {


            if (Game.levelNo == 101) {

                Game.level.player.mov1 = 0;
                Game.StartTime = System.currentTimeMillis();
                Game.ChangeTime = System.currentTimeMillis();
                Level.KeyTime = System.currentTimeMillis();
                //Game.levelNo=1;
                this.x = 16 << 3;
                this.y = 10 << 3;
                Game.levelNo = 0;

                Game.level.generateLevel();
                return true;

            }
            if (System.currentTimeMillis() - Game.StartTime < Game.highScore[Game.levelNo] || Game.highScore[Game.levelNo] == 0) {
                Game.highScore[Game.levelNo] = (int) (System.currentTimeMillis() - Game.StartTime);
            }
            //System.out.println(Game.highscore[Game.levelNo]);
            FileWriter fileWritter;
            try {
                if (!f1.exists()) {
                    f1.createNewFile();
                }
                fileWritter = new FileWriter(f1.getName(), false);
                BufferedWriter bufferWriter = new BufferedWriter(fileWritter);
                //Scanner sc = new Scanner(f);
                for (int i = 1; i < Game.levelNo + 1; i++) {
                    bufferWriter.write(Integer.toString(Game.highScore[i]));
                    System.out.println(Game.highScore[i]);
                    bufferWriter.newLine();

                }
                bufferWriter.close();
                fileWritter.close();
                //System.out.println(sc.nextLine());


            } catch (IOException e) {

                e.printStackTrace();
            }


            Game.StartTime = System.currentTimeMillis();
            Game.ChangeTime = System.currentTimeMillis();
            Level.KeyTime = System.currentTimeMillis();
            Game.plx = this.x;
            Game.ply = this.y;
            Game.levelEnd = 1;
            Game.levelNo++;
            Game.level.generateLevel();


        }

        if (newTile.getId() == Tile.STONE.getId() && write)  //here
        {
            if (xa > 0)
                xa = xa - 7;
            if (ya > 0)
                ya = ya - 7;
            Level.tiles[(Game.level.player.x / 8) + xa + ((Game.level.player.y / 8) + ya) * Level.width] = Tile.GRASS.getId();
            FileWriter fileWritter;
            try {
                fileWritter = new FileWriter(f.getName(), true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                //Scanner sc = new Scanner(f);
                //String s=Integer.toString(x/8+xa)+" "+Integer.toString(y/8+xa)+"\n";

                bufferWritter.write(Integer.toString(Game.level.player.x / 8 + xa));
                bufferWritter.write(" ");
                bufferWritter.write(Integer.toString((Game.level.player.y / 8 + ya)));
                bufferWritter.newLine();
                bufferWritter.close();
                fileWritter.close();
                //System.out.println(sc.nextLine());


            } catch (IOException e) {

                e.printStackTrace();
            }
        }//here
        return newTile.isSolid();
    }

//    public String getName() {
//        return name;
//    }
}