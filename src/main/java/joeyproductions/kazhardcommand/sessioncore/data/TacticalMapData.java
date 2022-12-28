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
 * Tactical-level map data, which can be generated at game start, and loaded
 * into a map viewer. Multiple references to such data can exist in one
 * strategic-level map, and for this reason we do not store unit data on a
 * TacticalMapData object.
 * @author Joseph Cramsey
 */
public class TacticalMapData {
    
    private static byte[][] TEST_GRID = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,1,1,1,0,1,0,0,0,0,0,1},
        {1,0,0,0,0,1,1,1,1,0,1,1,0,0,0,0,0,1},
        {1,1,1,1,2,1,0,0,0,1,1,1,1,2,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,2,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    
    public static final int MAX_DIM = 16;
    public static final int TILE_LENGTH = MAX_DIM * MAX_DIM;
    
    private final TacticalTileData[] tiles;
    
    private TacticalMapData() {
        tiles = new TacticalTileData[TILE_LENGTH];
    }
    
    public static TacticalMapData create() {
        TacticalMapData product = new TacticalMapData();
        
        int withBorderLen = MAX_DIM+2;
        TacticalTileData[][] tileBuffer = new TacticalTileData[withBorderLen][withBorderLen];
        
        // Create from map data
        for (int y = 0; y < withBorderLen; y++) {
            for (int x = 0; x < withBorderLen; x++) {
                TacticalTileData tile = new TacticalTileData();
                tile.setBorderTile(!(
                        x > 0 && x < withBorderLen - 1 && 
                        y > 0 && y < withBorderLen - 1
                ));
                switch (TEST_GRID[y][x]) {
                    case 1:
                        tile.setRaised(true);
                        break;
                    case 2:
                        tile.setRampBase(true);
                        break;
                }
                tile.setCoordinates(x - 1, y - 1);
                tileBuffer[y][x] = tile;
            }
        }
        
        // Find neighbors
        for (int y = 1; y < withBorderLen - 1; y++) {
            for (int x = 1; x < withBorderLen - 1; x++) {
                TacticalTileData dataTile = tileBuffer[y][x];
                for (int d = 0; d < TacticalTileData.DIR_LEN; d++) {
                    int xo = x + TacticalTileData.getXOffsetFromDirection(d);
                    int yo = y + TacticalTileData.getYOffsetFromDirection(d);
                    dataTile.neighbors[d] = tileBuffer[yo][xo];
                }
                product.tiles[Byte.toUnsignedInt(dataTile.coordinates)] = dataTile;
            }
        }
        
        // Update patterns
        for (TacticalTileData dataTile : product.tiles) {
            dataTile.updatePatterns();
        }
        
        // Update secondary patterns
        for (TacticalTileData dataTile : product.tiles) {
            dataTile.updateSecondaryPatterns();
        }
        
        return product;
    }
    
    public TacticalTileData[] getTiles() {
        return tiles;
    }
}
