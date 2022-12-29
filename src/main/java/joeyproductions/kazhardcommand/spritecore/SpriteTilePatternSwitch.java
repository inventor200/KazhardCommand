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

import joeyproductions.kazhardcommand.Main;
import joeyproductions.kazhardcommand.sessioncore.data.TacticalTileData;

/**
 * An object that looks through two arrays of SpriteTilePatterns, based on if
 * some value on the center tile is true or false.
 * @author Joseph Cramsey
 */
public abstract class SpriteTilePatternSwitch {
    
    public static final int SIDE_PILLAR = 0;
    public static final int SIDE_WEST = 1;
    public static final int SIDE_EAST = 2;
    public static final int SIDE_FLAT = 3;
    public static final int PILLAR = 4;
    public static final int NORTH_SOUTH = 5;
    public static final int EAST_WEST = 6;
    public static final int NORTH_EDGE = 7;
    public static final int SOUTH_EDGE = 8;
    public static final int EAST_EDGE = 9;
    public static final int WEST_EDGE = 10;
    public static final int NORTH = 11;
    public static final int SOUTH = 12;
    public static final int EAST = 13;
    public static final int WEST = 14;
    public static final int NORTHEAST = 15;
    public static final int SOUTHEAST = 16;
    public static final int SOUTHWEST = 17;
    public static final int NORTHWEST = 18;
    public static final int BLANK = 19;
    public static final int SPRITE_COUNT = 20;
    
    private static SpriteTilePattern[] FALSE_PATTERNS;
    private static SpriteTilePattern[] TRUE_PATTERNS;
    
    private final Sprite[] spriteArray;
    
    public SpriteTilePatternSwitch() {
        spriteArray = new Sprite[SPRITE_COUNT];
    }
    
    public void setDefault(Sprite sprite) {
        for (int i = 0; i < SPRITE_COUNT; i++) {
            spriteArray[i] = sprite;
        }
    }
    
    public void setSprite(int spriteIndex, Sprite sprite) throws InvalidSpriteIndexException {
        if (spriteIndex < 0 || spriteIndex >= SPRITE_COUNT) {
            throw new InvalidSpriteIndexException(spriteIndex);
        }
        
        spriteArray[spriteIndex] = sprite;
    }
    
    public Sprite getSprite(TacticalTileData dataTile) {
        boolean isTrue = isTrue(dataTile);
        byte subjectPattern = getPattern(dataTile);
        
        if (isTrue) {
            for (SpriteTilePattern spattern : TRUE_PATTERNS) {
                if (spattern.hasMatch(subjectPattern)) {
                    return spriteArray[spattern.spriteIndex];
                }
            }
        }
        else {
            for (SpriteTilePattern spattern : FALSE_PATTERNS) {
                if (spattern.hasMatch(subjectPattern)) {
                    return spriteArray[spattern.spriteIndex];
                }
            }
        }
        
        return spriteArray[BLANK];
    }
    
    // Meant to be overridden
    public abstract boolean isTrue(TacticalTileData dataTile);
    
    // Meant to be overridden
    public abstract byte getPattern(TacticalTileData dataTile);
    
    /**
     * Converts a diagram String of ones, zeroes, and spaces into a directional
     * pattern to use in tile comparison.<br>
     * <br>
     * Example of diagram String:<br>
     * "010 0 0 101"<br>
     * which would represent:<br>
     * 010<br>
     * 0 0<br>
     * 101
     * @param diagram
     * @return directional pattern (int)
     */
    private static byte getPatternFromDiagram(String diagram) throws InvalidDiagramStringException {
        if (diagram.length() != 11) {
            throw new InvalidDiagramStringException(diagram);
        }
        
        /*
         * points[0] = NW
         * points[1] = N
         * points[2] = NE
         * points[3] = ignored (line seperator)
         * points[4] = W
         * points[5] = ignored (center)
         * points[6] = E
         * points[7] = ignored (line seperator)
         * points[8] = SW
         * points[9] = S
         * points[10] = SE
         */
        char[] points = diagram.toCharArray();
        byte pattern = 0;
        
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.NORTHWEST, points[0] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.NORTH, points[1] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.NORTHEAST, points[2] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.WEST, points[4] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.EAST, points[6] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.SOUTHWEST, points[8] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.SOUTH, points[9] == '1');
        pattern = TacticalTileData.setDirectionFlag(pattern, TacticalTileData.SOUTHEAST, points[10] == '1');
        
        return pattern;
    }
    
    // Switches
    
    public static SpriteTilePatternSwitch IS_RAISED_SWITCH;
    
    public static void loadSpritePatterns() {
        try {
            FALSE_PATTERNS = new SpriteTilePattern[] {
                    new SpriteTilePattern(
                            SIDE_PILLAR,
                            getPatternFromDiagram("111 0 0 000"),
                            getPatternFromDiagram("010 0 0 000")
                    ),
                    new SpriteTilePattern(
                            SIDE_WEST,
                            getPatternFromDiagram("111 0 0 000"),
                            getPatternFromDiagram("011 0 0 000")
                    ),
                    new SpriteTilePattern(
                            SIDE_EAST,
                            getPatternFromDiagram("111 0 0 000"),
                            getPatternFromDiagram("110 0 0 000")
                    ),
                    new SpriteTilePattern(
                            SIDE_FLAT,
                            getPatternFromDiagram("111 0 0 000"),
                            getPatternFromDiagram("111 0 0 000")
                    )
            };

            TRUE_PATTERNS = new SpriteTilePattern[] {
                    new SpriteTilePattern(
                            PILLAR,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 0 0 000")
                    ),
                    new SpriteTilePattern(
                            NORTH_SOUTH,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 0 0 010")
                    ),
                    new SpriteTilePattern(
                            EAST_WEST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 1 1 000")
                    ),
                    new SpriteTilePattern(
                            NORTH_EDGE,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 1 1 010")
                    ),
                    new SpriteTilePattern(
                            SOUTH_EDGE,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 1 1 000")
                    ),
                    new SpriteTilePattern(
                            EAST_EDGE,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 1 0 010")
                    ),
                    new SpriteTilePattern(
                            WEST_EDGE,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 0 1 010")
                    ),
                    new SpriteTilePattern(
                            NORTH,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 0 0 010")
                    ),
                    new SpriteTilePattern(
                            SOUTH,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 0 0 000")
                    ),
                    new SpriteTilePattern(
                            EAST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 1 0 000")
                    ),
                    new SpriteTilePattern(
                            WEST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 0 1 000")
                    ),
                    new SpriteTilePattern(
                            NORTHEAST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 1 0 010")
                    ),
                    new SpriteTilePattern(
                            SOUTHEAST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 1 0 000")
                    ),
                    new SpriteTilePattern(
                            SOUTHWEST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("010 0 1 000")
                    ),
                    new SpriteTilePattern(
                            NORTHWEST,
                            getPatternFromDiagram("010 1 1 010"),
                            getPatternFromDiagram("000 0 1 010")
                    )
            };

            IS_RAISED_SWITCH = new SpriteTilePatternSwitch() {
                @Override
                public boolean isTrue(TacticalTileData dataTile) {
                    return dataTile.isRaised();
                }

                @Override
                public byte getPattern(TacticalTileData dataTile) {
                    return dataTile.neighborRaisePattern;
                }
            };
            IS_RAISED_SWITCH.setDefault(Sprite.BLANK_GROUND);
            IS_RAISED_SWITCH.setSprite(SIDE_PILLAR, Sprite.CLIFFSIDE_PILLAR);
            IS_RAISED_SWITCH.setSprite(SIDE_WEST, Sprite.CLIFFSIDE_WEST);
            IS_RAISED_SWITCH.setSprite(SIDE_EAST, Sprite.CLIFFSIDE_EAST);
            IS_RAISED_SWITCH.setSprite(SIDE_FLAT, Sprite.CLIFFSIDE_FLAT);
            IS_RAISED_SWITCH.setSprite(PILLAR, Sprite.CLIFF_PILLAR);
            IS_RAISED_SWITCH.setSprite(NORTH_SOUTH, Sprite.CLIFF_NORTH_SOUTH);
            IS_RAISED_SWITCH.setSprite(EAST_WEST, Sprite.CLIFF_EAST_WEST);
            IS_RAISED_SWITCH.setSprite(NORTH_EDGE, Sprite.CLIFF_NORTH_EDGE);
            IS_RAISED_SWITCH.setSprite(SOUTH_EDGE, Sprite.CLIFF_SOUTH_EDGE);
            IS_RAISED_SWITCH.setSprite(EAST_EDGE, Sprite.CLIFF_EAST_EDGE);
            IS_RAISED_SWITCH.setSprite(WEST_EDGE, Sprite.CLIFF_WEST_EDGE);
            IS_RAISED_SWITCH.setSprite(NORTH, Sprite.CLIFF_NORTH);
            IS_RAISED_SWITCH.setSprite(SOUTH, Sprite.CLIFF_SOUTH);
            IS_RAISED_SWITCH.setSprite(EAST, Sprite.CLIFF_EAST);
            IS_RAISED_SWITCH.setSprite(WEST, Sprite.CLIFF_WEST);
            IS_RAISED_SWITCH.setSprite(NORTHEAST, Sprite.CLIFF_NORTHEAST);
            IS_RAISED_SWITCH.setSprite(SOUTHEAST, Sprite.CLIFF_SOUTHEAST);
            IS_RAISED_SWITCH.setSprite(SOUTHWEST, Sprite.CLIFF_SOUTHWEST);
            IS_RAISED_SWITCH.setSprite(NORTHWEST, Sprite.CLIFF_NORTHWEST);
        } catch (InvalidDiagramStringException | InvalidSpriteIndexException ex) {
            Main.showInitException(ex, SpriteTilePatternSwitch.class);
        }
    }
}
