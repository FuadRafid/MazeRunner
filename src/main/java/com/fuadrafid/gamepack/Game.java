package com.fuadrafid.gamepack;

import com.fuadrafid.entities.Mob;
import com.fuadrafid.gfx.Colours;
import com.fuadrafid.gfx.GameFont;
import com.fuadrafid.gfx.Screen;
import com.fuadrafid.gfx.SpriteSheet;
import com.fuadrafid.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.util.Scanner;

public class Game extends Canvas implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;
    public static double StartTime;
    public static final int WIDTH = 250;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 4;
    public static final String NAME = "Game";
    public static int plx = 16, ply = 10;
    public static int levelEnd = 0;
    public static int levelNo = 0;
    public boolean running = false;
    public int tickCount = 0;
    public static boolean IsPaused = false;
    public static double xPresstime;
    public static double ChangeTime = System.currentTimeMillis();

    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
            BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
            .getData();
    private final int[] colours = new int[6 * 6 * 6];
    public static int[] highScore = new int[102];
    public static Screen screen;
    public static InputHandler input;
    public static Level level;
    public static double speed = 60D;
    MouseHandler mouseHandler;
    // public Player player;

    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(NAME);


        //frame.addKeyListener(input);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                File f = new File("level101.maze");
                if (f.exists())
                    f.delete();
            }
        });
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void init() {
        int index = 0;
        for (int r = 0; r < 6; r++) {
            for (int g = 0; g < 6; g++) {
                for (int b = 0; b < 6; b++) {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);

                    colours[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }

        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/design.png"));
        mouseHandler = new MouseHandler(this);
        input = new InputHandler(this);
        level = new Level(64, 64);

        int i = 1;
        File file = new File("hs.maze");
        Scanner sc;

        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                highScore[i] = Integer.parseInt(s);
                System.out.println(highScore[i]);
                i++;
            }

            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }

        //player = new Player(level,plx<<3,ply<<3, input);
        //level.addEntity(player);
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

//    public synchronized void stop() {
//        running = false;
//    }

    public void run() {

        long lastTime = System.nanoTime();
        long lastTimer = System.currentTimeMillis();
        //System.out.println(speed);
        double nsPerTick;
        double delta = 0;

        init();

        while (running) {
            long now = System.nanoTime();
            if (speed <= 0)
                speed = 1;
            if (speed > 2000)
                speed = 2000;
            nsPerTick = 1000000000D / speed;

            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            //boolean shouldRender = true;

            while (delta >= 1) {
                try {
                    tick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                delta -= 1;
                //shouldRender = true;
            }

            // if (shouldRender) {
            render();
            //}


            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                //System.out.printf("%d %d\n",frames,ticks);
                //System.out.println(speed);
            }

        }
    }

    public void tick() {
        tickCount++;
        level.tick();
        if (System.currentTimeMillis() - StartTime > 120000 && levelNo > 0 && !IsPaused) {
            level.dead = true;
        }
        if (levelNo > 4 && System.currentTimeMillis() - ChangeTime > 35000 && !Mob.write) {
            level.changeKey();
            ChangeTime = System.currentTimeMillis();


        }
        if (levelEnd == 1 && System.currentTimeMillis() - Level.KeyTime > 2000)
            levelEnd = 0;


    }


    public void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }


        int xOffset = level.player.x - (screen.width / 2);
        int yOffset = level.player.y - (screen.height / 2);


        level.renderTiles(screen, xOffset, yOffset);
        level.renderEntities(screen);

        if (levelEnd == 1) {
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
            GameFont.render("    Level   ", screen, x + 11 * 8, y + 16 + 60, Colours.get(100, -1, -1, 555), 1);
            GameFont.render("  Complete  ", screen, x + 11 * 8, y + 8 + 16 + 60, Colours.get(200, -1, -1, 555), 1);
            GameFont.render("   Level" + levelNo + "   ", screen, x + 11 * 8, y + 32 + 60, Colours.get(300, -1, -1, 555), 1);


        }

        if (levelNo == 0) {
            GameFont.render("The Maze", Game.screen, 5 * 8, 3 * 8, Colours.get(-1, -1, -1, 0), 2);
            GameFont.render(" Runner", Game.screen, 12 * 8, 5 * 8, Colours.get(-1, -1, -1, 0), 2);
            GameFont.render("Click Here", Game.screen, 19 * 8, 7 * 8, Colours.get(-1, -1, -1, 0), 1);
            if (mouseHandler.isPressed()) {
                GameFont.render("Press M for MakeMode", Game.screen, 7 * 8, 12 * 8, Colours.get(-1, -1, -1, 511), 1);
                GameFont.render("Press Enter for Campaign", Game.screen, 5 * 8, 15 * 8, Colours.get(-1, -1, -1, 511), 1);
                GameFont.render("Press H for Help", Game.screen, 8 * 8, 18 * 8, Colours.get(-1, -1, -1, 511), 1);
            }
        } else {
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

            GameFont.render(((int) (System.currentTimeMillis() - StartTime) / 1000) / 60 + ":" + ((int) (System.currentTimeMillis() - StartTime) / 1000) % 60, Game.screen, x, y, Colours.get(-1, -1, -1, 0), 1);
            GameFont.render("Record", Game.screen, x + 14 * 8, y, Colours.get(-1, -1, -1, 555), 1);
            GameFont.render(highScore[levelNo] / 60000 + ":" + (highScore[levelNo] / 1000) % 60, Game.screen, x + 15 * 8, y + 8, Colours.get(-1, -1, -1, 555), 1);

        }


        if (levelNo > 0) {
            int x, y;
            x = level.player.x + (screen.width / 2) - 20;
            y = level.player.y - (screen.height / 2) + 4;
            if (x > 62 * 8 - 4)
                x = 62 * 8 - 4;
            if (y > 42 * 8)
                y = 42 * 8;
            if (x < 29 * 8 - 2)
                x = 29 * 8 - 2;
            if (y < 4)
                y = 4;


            if (level.keyX << 3 < level.player.x)
                GameFont.render(" +", screen, x, y, Colours.get(-1, -1, -1, 200), 1, 01);
            else
                GameFont.render(" +", screen, x, y, Colours.get(-1, -1, -1, 200), 1);

            if (level.keyY << 3 < level.player.y)
                GameFont.render("-", screen, x, y, Colours.get(-1, -1, -1, 200), 1);
            else
                GameFont.render("-", screen, x, y, Colours.get(-1, -1, -1, 200), 1, 02);

        }

        if (speed > 200 && System.currentTimeMillis() - xPresstime <= 2000) {
            GameFont.render("Speed", screen, plx + 8, ply - 16, Colours.get(-1, -1, -1, 550), 1);
            GameFont.render(Double.toString(speed), screen, plx + 8, ply - 8, Colours.get(-1, -1, -1, 550), 1);

        }

        for (int y = 0; y < screen.height; y++) {
            for (int x = 0; x < screen.width; x++) {
                int ColourCode = screen.pixels[x + y * screen.width];
                if (ColourCode < 255) {
                    pixels[x + y * WIDTH] = colours[ColourCode];

                }
            }
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();


    }


}