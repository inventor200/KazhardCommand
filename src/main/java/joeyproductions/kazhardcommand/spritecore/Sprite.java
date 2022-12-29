/*
 * The MIT License
 *
 * Copyright 2022 Joseph Cramsey.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package joeyproductions.kazhardcommand.spritecore;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import joeyproductions.kazhardcommand.Main;

/**
 * An organized and pre-processed image.
 * @author Joseph Cramsey
 */
public class Sprite {
    
    public BufferedImage img;
    public Dimension size;
    
    public Sprite(String path) {
        try {
            // Load
            File pathToFile = Main.openLocalFile("images/" + path + ".png", Sprite.class);

            this.img = upscale(ImageIO.read(pathToFile));
            this.size = new Dimension(img.getWidth(), img.getHeight());
        } catch (IOException ex) {
            Main.showInitException(ex, Sprite.class);
        }
    }
    
    private static Sprite createFromSheet(ArrayList<BufferedImage> sheet, int index) {
        try {
            BufferedImage img = sheet.get(index);
            Sprite product = new Sprite(img);
            return product;
        } catch (IndexOutOfBoundsException ex) {
            Main.showInitException(ex, Sprite.class);
            return null;
        }
    }
    
    private static BufferedImage upscale(BufferedImage baseImg) {
        // Bake scale
        int scale = getScale();
        int w = baseImg.getWidth() * scale;
        int h = baseImg.getHeight() * scale;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = img.createGraphics();

        for (int y = 0; y < baseImg.getHeight(); y++) {
            for (int x = 0; x < baseImg.getWidth(); x++) {
                int colorData = baseImg.getRGB(x, y);
                g2.setColor(new Color(colorData, true));
                g2.fillRect(x * scale, y * scale, scale, scale);
            }
        }
        
        return img;
    }
    
    public Sprite(BufferedImage img) {
        this.img = img;
        this.size = new Dimension(img.getWidth(), img.getHeight());
    }
    
    private static int getScale() {
        int scale = 1;
        for (int i = 1; i < Main.PIXEL_UPSCALE_STEPS; i++) {
            scale *= 2;
        }
        return scale;
    }
    
    public static ArrayList<BufferedImage> getGridSheet(String path, int xSize, int ySize) {
        try {
            // Load
            File pathToFile = Main.openLocalFile("images/" + path + ".png", Sprite.class);
            
            // Cut
            BufferedImage sheet = ImageIO.read(pathToFile);
            ArrayList<BufferedImage> images = new ArrayList<>();
            
            int scale = getScale();

            for (int y0 = 0; y0 < sheet.getHeight(); y0 += ySize) {
                for (int x0 = 0; x0 < sheet.getWidth(); x0 += xSize) {
                    BufferedImage cutout = new BufferedImage(
                            xSize * scale, ySize * scale,
                            BufferedImage.TYPE_INT_ARGB
                    );
                    Graphics2D g2 = cutout.createGraphics();

                    for (int y = 0; y < ySize; y++) {
                        for (int x = 0; x < xSize; x++) {
                            int colorData = sheet.getRGB(x0 + x, y0 + y);
                            g2.setColor(new Color(colorData, true));
                            g2.fillRect(x * scale, y * scale, scale, scale);
                        }
                    }
                    
                    images.add(cutout);
                }
            }
            
            return images;
        } catch (IOException ex) {
            Main.showInitException(ex, Sprite.class);
        }
        
        return new ArrayList<>();
    }
    
    public static Sprite TEST_GRID = null;
    
    public static Sprite BLANK_GROUND = null;
    
    public static Sprite CLIFF_NORTH_SOUTH = null;
    public static Sprite CLIFF_EAST_WEST = null;
    
    public static Sprite CLIFF_PILLAR = null;
    public static Sprite CLIFF_NORTH = null;
    public static Sprite CLIFF_NORTHEAST = null;
    public static Sprite CLIFF_EAST = null;
    public static Sprite CLIFF_SOUTHEAST = null;
    public static Sprite CLIFF_SOUTH = null;
    public static Sprite CLIFF_SOUTHWEST = null;
    public static Sprite CLIFF_WEST = null;
    public static Sprite CLIFF_NORTHWEST = null;
    
    public static Sprite CLIFF_NORTH_EDGE = null;
    public static Sprite CLIFF_EAST_EDGE = null;
    public static Sprite CLIFF_SOUTH_EDGE = null;
    public static Sprite CLIFF_WEST_EDGE = null;
    
    public static Sprite CLIFFSIDE_PILLAR = null;
    public static Sprite CLIFFSIDE_FLAT = null;
    public static Sprite CLIFFSIDE_EAST = null;
    public static Sprite CLIFFSIDE_WEST = null;
    
    public static void loadSprites() {
        TEST_GRID = new Sprite("TestGrid");
        
        ArrayList<BufferedImage> cliffSet = getGridSheet("CliffSetDark", 32, 32);
        
        CLIFF_PILLAR = createFromSheet(cliffSet, 0);
        BLANK_GROUND = createFromSheet(cliffSet, 1);
        CLIFF_NORTH = createFromSheet(cliffSet, 2);
        CLIFF_NORTH_SOUTH = createFromSheet(cliffSet, 4);
        CLIFFSIDE_PILLAR = createFromSheet(cliffSet, 5);
        CLIFF_NORTHWEST = createFromSheet(cliffSet, 6);
        CLIFF_SOUTH_EDGE = createFromSheet(cliffSet, 7);
        CLIFF_NORTHEAST = createFromSheet(cliffSet, 8);
        CLIFF_WEST = createFromSheet(cliffSet, 10);
        CLIFF_EAST_EDGE = createFromSheet(cliffSet, 11);
        CLIFFSIDE_FLAT = createFromSheet(cliffSet, 12);
        CLIFF_WEST_EDGE = createFromSheet(cliffSet, 13);
        CLIFF_EAST = createFromSheet(cliffSet, 14);
        CLIFFSIDE_WEST = createFromSheet(cliffSet, 15);
        CLIFF_SOUTHWEST = createFromSheet(cliffSet, 16);
        CLIFF_NORTH_EDGE = createFromSheet(cliffSet, 17);
        CLIFF_SOUTHEAST = createFromSheet(cliffSet, 18);
        CLIFFSIDE_EAST = createFromSheet(cliffSet, 19);
        CLIFF_EAST_WEST = createFromSheet(cliffSet, 20);
        CLIFF_SOUTH = createFromSheet(cliffSet, 22);
    }
}
