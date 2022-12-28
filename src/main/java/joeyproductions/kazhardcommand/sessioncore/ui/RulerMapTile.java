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
package joeyproductions.kazhardcommand.sessioncore.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import joeyproductions.kazhardcommand.spritecore.Sprite;

/**
 * A kind of tile placed around the borders of the map UI.
 * @author Joseph Cramsey
 */
public class RulerMapTile extends VisualMapTile {
    
    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int EAST = 2;
    public static final int SOUTHEAST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTHWEST = 5;
    public static final int WEST = 6;
    public static final int NORTHWEST = 7;
    
    private static final int RULER_WIDTH = 8;
    private static final int RULER_LEAD = VisualTacticalGrid.TILE_SIDE_LEN - RULER_WIDTH;
    private static final int HALF_WIDTH = VisualTacticalGrid.TILE_SIDE_LEN / 2;
    private static final int TEXT_LEAD = (VisualTacticalGrid.TILE_SIDE_LEN / 8) * 3;
    
    private static final float RULER_TINT_A = 0.4f;
    private static final Color RULER_COLOR_A = new Color(RULER_TINT_A, RULER_TINT_A, RULER_TINT_A);
    private static final float RULER_TINT_B = 0.2f;
    private static final Color RULER_COLOR_B = new Color(RULER_TINT_B, RULER_TINT_B, RULER_TINT_B);
    private static final float RULER_TINT_C = (RULER_TINT_A + RULER_TINT_B) / 2f;
    private static final Color RULER_COLOR_C = new Color(RULER_TINT_C, RULER_TINT_C, RULER_TINT_C);
    
    private static final int FONT_HEIGHT = 18;
    private static final int FONT_WIDTH = FONT_HEIGHT / 2;
    private static final Font COORDINATE_FONT = new Font(Font.MONOSPACED, Font.BOLD, FONT_HEIGHT);
    
    private final int direction;
    private final boolean isOther;
    private final String coordinateStr;
    
    public RulerMapTile(int direction, boolean isOther) {
        this(direction, isOther, "");
    }
    
    public RulerMapTile(int direction, boolean isOther, String coordinateStr) {
        this.direction = direction;
        this.isOther = isOther;
        this.coordinateStr = coordinateStr;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(
                direction % 2 == 1
                ? RULER_COLOR_C
                : (isOther ? RULER_COLOR_B : RULER_COLOR_A)
        );
        
        // Paint marker
        switch (direction) {
            case NORTH:
                g2.fillRect(
                        0,
                        RULER_LEAD,
                        VisualTacticalGrid.TILE_SIDE_LEN,
                        RULER_WIDTH);
                break;
            case EAST:
                g2.fillRect(
                        0,
                        0,
                        RULER_WIDTH,
                        VisualTacticalGrid.TILE_SIDE_LEN);
                break;
            case SOUTH:
                g2.fillRect(
                        0,
                        0,
                        VisualTacticalGrid.TILE_SIDE_LEN,
                        RULER_WIDTH);
                break;
            case WEST:
                g2.fillRect(
                        RULER_LEAD,
                        0,
                        RULER_WIDTH,
                        VisualTacticalGrid.TILE_SIDE_LEN);
                break;
            case NORTHEAST:
                g2.fillRect(
                        0,
                        RULER_LEAD,
                        RULER_WIDTH,
                        RULER_WIDTH
                );
                break;
            case SOUTHEAST:
                g2.fillRect(
                        0,
                        0,
                        RULER_WIDTH,
                        RULER_WIDTH
                );
                break;
            case NORTHWEST:
                g2.fillRect(
                        RULER_LEAD,
                        RULER_LEAD,
                        RULER_WIDTH,
                        RULER_WIDTH
                );
                break;
            case SOUTHWEST:
                g2.fillRect(
                        RULER_LEAD,
                        0,
                        RULER_WIDTH,
                        RULER_WIDTH
                );
                break;
        }
        
        // Paint grid
        g2.drawImage(Sprite.TEST_GRID.img, null, 0, 0);
        
        // Erase half of grid
        g2.setColor(Color.BLACK);
        switch (direction) {
            case NORTH:
            case NORTHEAST:
            case NORTHWEST:
                g2.fillRect(
                        0,
                        0,
                        VisualTacticalGrid.TILE_SIDE_LEN,
                        HALF_WIDTH);
                break;
            case SOUTH:
            case SOUTHEAST:
            case SOUTHWEST:
                g2.fillRect(
                        0,
                        HALF_WIDTH,
                        VisualTacticalGrid.TILE_SIDE_LEN,
                        HALF_WIDTH);
                break;
        }
        switch (direction) {
            case EAST:
            case NORTHEAST:
            case SOUTHEAST:
                g2.fillRect(
                        HALF_WIDTH,
                        0,
                        HALF_WIDTH,
                        VisualTacticalGrid.TILE_SIDE_LEN);
                break;
            case WEST:
            case NORTHWEST:
            case SOUTHWEST:
                g2.fillRect(
                        0,
                        0,
                        HALF_WIDTH,
                        VisualTacticalGrid.TILE_SIDE_LEN);
                break;
        }
        
        if (direction % 2 == 1) return;
        
        g2.setFont(COORDINATE_FONT);
        g2.setColor(Color.WHITE);
        int width = FONT_WIDTH * coordinateStr.length();
        int x = HALF_WIDTH - (width >> 1);
        int y = HALF_WIDTH + (FONT_HEIGHT >> 1);
        g2.drawString(coordinateStr, x, y);
    }
}
