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
package joeyproductions.kazhardcommand.sessioncore.data;

/**
 * The data side of the map tile, which is not concerned with rendering.
 * A lot of bit-crushing is to reduce memory impact, as there can be a LOT of
 * these loaded at one time.
 * @author Joseph Cramsey
 */
public class TacticalTileData {
    
    public static final int EAST = 0;
    public static final int NORTHEAST = 1;
    public static final int NORTH = 2;
    public static final int NORTHWEST = 3;
    public static final int WEST = 4;
    public static final int SOUTHWEST = 5;
    public static final int SOUTH = 6;
    public static final int SOUTHEAST = 7;
    public static final int DIR_LEN = 8;
    
    private static final int X_COORD_MASK = 0x0F;
    private static final int[] TRUE_BY_DIR_ARRAY = {
        0b00000001, 0b00000010, 0b00000100, 0b00001000,
        0b00010000, 0b00100000, 0b01000000, 0b10000000
    };
    
    private static final int B_TRAIT_MASK = 0xFF;
    
    private byte terrainShapeBits = 0;
    private static final int B_IS_BORDER =          0b00000001;
    private static final int B_IS_RAISED =          0b00000010;
    private static final int B_IS_RAMP_BASE =       0b00000100;
    private static final int B_IS_RAMP =            0b00001000;
    
    public TacticalTileData[] neighbors = new TacticalTileData[8];
    
    public byte coordinates = 0;
    
    public byte neighborRaisePattern = 0;
    
    public TacticalTileData() {
        //
    }
    
    private static boolean checkBit(byte sourceByte, int bitSelector) {
        return (Byte.toUnsignedInt(sourceByte) & bitSelector) != 0;
    }
    
    private static byte setBit(byte sourceByte, int bitSelector, boolean state) {
        int nextBits = Byte.toUnsignedInt(sourceByte) & ~bitSelector;
        if (state) {
            return (byte)((nextBits | bitSelector) & B_TRAIT_MASK);
        }
        return (byte)(nextBits & B_TRAIT_MASK);
    }
    
    public boolean isBorderTile() {
        return checkBit(terrainShapeBits, B_IS_BORDER);
    }
    
    public void setBorderTile(boolean state) {
        terrainShapeBits = setBit(terrainShapeBits, B_IS_BORDER, state);
    }
    
    public boolean isRaised() {
        return checkBit(terrainShapeBits, B_IS_RAISED);
    }
    
    public void setRaised(boolean state) {
        terrainShapeBits = setBit(terrainShapeBits, B_IS_RAISED, state);
    }
    
    public boolean isRampBase() {
        return checkBit(terrainShapeBits, B_IS_RAMP_BASE);
    }
    
    public void setRampBase(boolean state) {
        terrainShapeBits = setBit(terrainShapeBits, B_IS_RAMP_BASE, state);
    }
    
    public boolean isRamp() {
        return checkBit(terrainShapeBits, B_IS_RAMP);
    }
    
    public void setRamp(boolean state) {
        terrainShapeBits = setBit(terrainShapeBits, B_IS_RAMP, state);
    }
    
    public static byte setDirectionFlag(byte original, int direction, boolean flagValue) {
        if (!flagValue) return original;
        return (byte)(Byte.toUnsignedInt(original) | TRUE_BY_DIR_ARRAY[direction]);
    }
    
    public void updatePatterns() {
        if (isBorderTile()) return; // Don't care about border tiles
        
        neighborRaisePattern = 0;
        for (int i = 0; i < DIR_LEN; i++) {
            TacticalTileData neighbor = neighbors[i];
            
            boolean raiseState = neighbor.isRaised();
            if (isRaised()) {
                raiseState = raiseState || neighbor.isRampBase();
            }
            
            // isRaised
            neighborRaisePattern = setDirectionFlag(
                    neighborRaisePattern, i, raiseState
            );
        }
    }
    
    public void updateSecondaryPatterns() {
        if (isBorderTile()) return; // Don't care about border tiles
        
        if (isRampBase()) {
            // Orthogonal neighbors are always connected to ramp bases
            neighborRaisePattern = (byte)(Byte.toUnsignedInt(neighborRaisePattern) & 0b10101010);
            for (int i = 0; i < DIR_LEN; i += 2) {
                TacticalTileData neighbor = neighbors[i];
                
                if (neighbor.isRaised()) {
                    neighbor.setRamp(true);
                }
            }
        }
    }
    
    public int getX() {
        return Byte.toUnsignedInt(coordinates) & X_COORD_MASK;
    }
    
    public int getY() {
        return Byte.toUnsignedInt(coordinates) >> 4;
    }
    
    public void setCoordinates(int x, int y) {
        if (isBorderTile()) return; // Don't care about border tiles
        
        coordinates = createCoordinates(x, y);
    }
    
    public static int getXOffsetFromDirection(int direction) {
        switch (direction) {
            case EAST:
                return 1;
            case NORTHEAST:
                return 1;
            case NORTH:
                return 0;
            case NORTHWEST:
                return -1;
            case WEST:
                return -1;
            case SOUTHWEST:
                return -1;
            case SOUTH:
                return 0;
            case SOUTHEAST:
                return 1;
        }
        throw new RuntimeException("Direction index out of range: " + direction);
    }
    
    public static int getYOffsetFromDirection(int direction) {
        switch (direction) {
            case EAST:
                return 0;
            case NORTHEAST:
                return -1;
            case NORTH:
                return -1;
            case NORTHWEST:
                return -1;
            case WEST:
                return 0;
            case SOUTHWEST:
                return 1;
            case SOUTH:
                return 1;
            case SOUTHEAST:
                return 1;
        }
        throw new RuntimeException("Direction index out of range: " + direction);
    }
    
    public static byte createCoordinates(int x, int y) {
        if (x >= TacticalMapData.MAX_DIM || x < 0) {
            throw new RuntimeException("X coordinate out of range: " + x);
        }
        if (y >= TacticalMapData.MAX_DIM || y < 0) {
            throw new RuntimeException("Y coordinate out of range: " + y);
        }
        
        return (byte)((y << 4) | x);
    }
}
