package com.fuadrafid.entities;

import com.fuadrafid.gamepack.Game;
import com.fuadrafid.gamepack.InputHandler;
import com.fuadrafid.gfx.Colours;
import com.fuadrafid.gfx.GameFont;
import com.fuadrafid.gfx.Screen;
import com.fuadrafid.level.Level;
import com.fuadrafid.level.tiles.Tile;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.util.Objects;


public class Player extends Mob {


    //File f=new File("level.txt");
    //private boolean ghost=false;
    private final InputHandler input;
    private final int colour = Colours.get(-1, 0, 200, 543);
    private static final int scale = 1;
    int mov1 = 0;
    int up = 0, down = 0, left = 0, right = 0;
    boolean SoundOn = true;
    private static float vol = -28;
    long time = System.currentTimeMillis();
    long playtime = System.currentTimeMillis();

    public Player(Level level, int x, int y, InputHandler input) {
        super(level, "Player", x, y, 1);
        this.input = input;
    }

    public static synchronized void playSound() {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Objects.requireNonNull(Player.class.getResourceAsStream("/mov.WAV")));
                clip.open(inputStream);
                FloatControl gainControl =
                        (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // System.out.println(gainControl.getValue());
                gainControl.setValue(vol);
                clip.start();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }


    public void tick() {
        if (input.VolUp.isPressed()) {
            vol += 10;
            if (vol > 6) vol = 6;
            input.VolUp.pressed = false;
        }
        if (input.VolDown.isPressed()) {
            vol -= 10;
            if (vol < -35) vol = -35;
            input.VolDown.pressed = false;
        }
        if (input.LVolUp.isPressed()) {
            Level.vol += 10;
            if (Level.vol > 6) Level.vol = 6;
            input.LVolUp.pressed = false;
        }
        if (input.LVolDown.isPressed()) {
            Level.vol -= 10;
            if (Level.vol < -30) Level.vol = -30;
            input.LVolDown.pressed = false;
        }
        if (input.Sound.isPressed()) {
            SoundOn = !SoundOn;
            input.Sound.pressed = false;
        }
        if (input.Music.isPressed()) {
            Level.MusicOn = !Level.MusicOn;
            input.Music.pressed = false;
        }

        if (input.P.isPressed() && Game.levelNo > 0) {
            double time = System.currentTimeMillis();
            Game.IsPaused = true;
            for (int i = 0; i < 100; i++)
                input.disableButtons();
            JOptionPane.showMessageDialog(null, "Game Paused");
            Game.StartTime += System.currentTimeMillis() - time;
            Game.IsPaused = false;
            return;
        }

        if (input.H.isPressed()) {
            double time = System.currentTimeMillis();
            for (int i = 0; i < 100; i++)
                input.disableButtons();
            JOptionPane.showMessageDialog(null, """
                    Find the Key in the Maze
                    See upper right corner for directions
                    Press enter in Make Mode to play
                    Press H anytime for Help
                    Press 1 to Mute Player Sound and 2 to mute Game Music
                    Press U and Y to control Music, + and - to control Player Sound
                    Press P to pause
                    Press BackSpace anytime to Quit Game
                    Try to Press & Hold other buttons also?(Maybe Cheats)
                    Enjoy!!
                    """);
            input.disableButtons();

            Game.StartTime += System.currentTimeMillis() - time;
            Game.IsPaused = false;
            return;
        }

        if (input.Bk.isPressed()) {
            Game.StartTime = System.currentTimeMillis();
            Game.ChangeTime = System.currentTimeMillis();
            write = false;
            this.x = 16 << 3;
            this.y = 10 << 3;
            Game.levelNo = 0;
            mov1 = 0;
            Game.level.generateLevel();
        }

        if (Game.level.dead) {

            if (input.enter.isPressed()) {

                Game.ChangeTime = System.currentTimeMillis();
                Game.StartTime = System.currentTimeMillis();
                Game.level.dead = false;
                Game.level.generateLevel();

            }
            return;
        }

        if (System.currentTimeMillis() - Game.StartTime > 120000 && write) {
            write = false;
            level.generateLevel();

        }
        if (input.enter.isPressed() && write) {
            write = false;
            Game.ChangeTime = System.currentTimeMillis();
            Game.StartTime = System.currentTimeMillis();
            level.generateLevel();
        }
        if (Game.levelNo == 0) {
            if (input.enter.isPressed()) {

                Game.ChangeTime = System.currentTimeMillis();
                Game.StartTime = System.currentTimeMillis();
                Game.levelNo++;
                if (Game.levelNo == 100)
                    Game.levelNo = 0;
                Game.level.generateLevel();


            }

            if (input.M.isPressed()) {
                Game.levelNo = 101;
                Game.ChangeTime = System.currentTimeMillis();
                Game.StartTime = System.currentTimeMillis();
                Game.level.generateLevel();
            }


            return;
        }

        int Rtime = (int) ((15000) / Game.speed);
        if (Rtime > 250)
            Rtime = 250;
        int xa = 0;
        int ya = 0;
        if (input.G.TimesPressed() % 3 == 0 && input.G.TimesPressed() != 0) {
            Tile.STONE.solid = false;
        }

        if (input.G.TimesPressed() % 6 == 0 && input.G.TimesPressed() != 0) {
            Tile.STONE.solid = true;
        }

        if (input.x.isPressed()) {
            Game.speed += 1;
            Game.xPresstime = System.currentTimeMillis();
        }

        if (input.z.isPressed()) {
            Game.speed -= 1;
            Game.xPresstime = System.currentTimeMillis();
        }
        if (input.up.isPressed() && up == 0) {
            ya -= 1;
            if (mov1 != 2 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 2;
                time = System.currentTimeMillis();
            } else if (mov1 != 3 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 3;
                time = System.currentTimeMillis();

            }
            left = 1;
            right = 1;
            down = 1;

        }

        if (input.down.isPressed() && down == 0) {
            ya += 1;
            if (mov1 != 1 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 1;
                time = System.currentTimeMillis();
            } else if (mov1 != 0 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 0;
                time = System.currentTimeMillis();

            }
            left = 1;
            right = 1;
            up = 1;

        }
        if (input.left.isPressed() && left == 0) {
            xa -= 1;
            if (mov1 != 6 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 6;
                time = System.currentTimeMillis();
            } else if (mov1 != 7 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 7;
                time = System.currentTimeMillis();

            }
            down = 1;
            right = 1;
            up = 1;
        }
        if (input.right.isPressed() && right == 0) {
            xa += 1;
            if (mov1 != 4 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 4;
                time = System.currentTimeMillis();
            } else if (mov1 != 5 && System.currentTimeMillis() - time > Rtime) {
                mov1 = 5;
                time = System.currentTimeMillis();

            }
            down = 1;
            left = 1;
            up = 1;
        }
        if (!input.up.isPressed()) {
            left = 0;
            right = 0;
            down = 0;
        }
        if (!input.down.isPressed()) {
            left = 0;
            right = 0;
            up = 0;
        }
        if (!input.left.isPressed()) {
            up = 0;
            right = 0;
            down = 0;
        }
        if (!input.right.isPressed()) {
            left = 0;
            up = 0;
            down = 0;
        }

        if (xa != 0 || ya != 0) {


            move(xa, ya);
            Game.plx = x;
            Game.ply = y;

            isMoving = true;
            if (System.currentTimeMillis() - playtime > Rtime && SoundOn) {
                playSound();
                playtime = System.currentTimeMillis();
            }


        } else {
            isMoving = false;
        }


    }

    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        if (Game.speed > 200) {
            xTile += 12;

        }


        int modifier = 8 * scale;
        int xOffset = x - modifier / 2;
        int yOffset = y - modifier / 2;

        if (Game.level.dead) {
            int x, y;
            x = level.player.x - (screen.width / 2) + 2;
            y = level.player.y - (screen.height / 2) + 2;
            if (x > 33 * 8)
                x = 33 * 8;
            if (y > 42 * 8 - 2)
                y = 42 * 8 - 2;
            if (x < 2)
                x = 2;
            if (y < 2)
                y = 2;
            y += 7 * 8;


            GameFont.render("LEVEL ", Game.screen, x + 14, y, Colours.get(-1, -1, -1, 500), 4);
            GameFont.render("FAILED  ", Game.screen, x + 14, y + 40, Colours.get(-1, -1, -1, 500), 4);
            GameFont.render("Press Enter", Game.screen, x + 14, y + 16 + 60, Colours.get(-1, -1, -1, 500), 1);
            return;

        }

        if (mov1 == 0) {

            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper body part 1

            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2

            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2


        }
        if (mov1 == 1) {
            xTile = 2;
            if (Game.speed > 200) {
                xTile += 12;

            }


            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper body part 1
            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2
            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2

        }
        if (mov1 == 2) {
            xTile = 4;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper

            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2
            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2


        }


        if (mov1 == 3) {
            xTile = 6;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper

            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2
            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2

        }
        if (mov1 == 4) {
            xTile = 8;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper

            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2
            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2

        }
        if (mov1 == 5) {
            xTile = 10;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset, yOffset, xTile + yTile * 32, colour, 0x00, scale); // upper
            // body
            // part
            // 1
            screen.render(xOffset + modifier, yOffset, (xTile + 1) + yTile * 32,
                    colour, 0x00, scale); // upper body part 2
            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2

        }
        if (mov1 == 6) {
            xTile = 8;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset + modifier, yOffset, xTile + yTile * 32,
                    colour, 0x01, scale); // upper body part 2

            screen.render(xOffset, yOffset, (xTile + 1) + yTile * 32, colour, 0x01, scale); // upper


            screen.render(xOffset, yOffset + modifier, xTile + (yTile + 1) * 32,
                    colour, 0x00, scale); // lower body part 1
            screen.render(xOffset + modifier, yOffset + modifier, (xTile + 1)
                    + (yTile + 1) * 32, colour, 0x00, scale); // lower body part 2

        }
        if (mov1 == 7) {
            xTile = 10;
            if (Game.speed > 200) {
                xTile += 12;

            }

            screen.render(xOffset + modifier, yOffset, xTile + yTile * 32,
                    colour, 0x01, scale); // upper body part 2

            screen.render(xOffset, yOffset, (xTile + 1) + yTile * 32, colour, 0x01, scale); // upper


            screen.render(xOffset + 1, yOffset + modifier, xTile + 1 + (yTile + 1) * 32,
                    colour, 0x01, scale); // lower body part 1
            screen.render(xOffset + modifier + 1, yOffset + modifier, (xTile)
                    + (yTile + 1) * 32, colour, 0x01, scale); // lower body part 2

        }


    }

    public boolean hasCollided(int xa, int ya) {
        if (!Mob.write) {
            if (ya < 0)
                ya -= 2;
            ya += 4;
        }
        return isSolidTile(xa, ya);
    }

}