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

import joeyproductions.kazhardcommand.sessioncore.ui.VisualTacticalTile;
import joeyproductions.kazhardcommand.sessioncore.data.TacticalTileData;
import joeyproductions.kazhardcommand.spritecore.SpriteTilePatternSwitch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import joeyproductions.kazhardcommand.sessioncore.data.TacticalMapData;

/**
 * Basic handler for map grids.
 * @author Joseph Cramsey
 */
public class VisualTacticalGrid {
    
    public static final int TILE_SIDE_LEN = 64;
    public static final Dimension TILE_SIZE = new Dimension(TILE_SIDE_LEN, TILE_SIDE_LEN);
    private static final int SCROLL_SPEED = 8;
    
    private TacticalMapData tileData;
    private final VisualTacticalTile[] tiles;
    
    private JPanel tilePanel;
    private JScrollPane scrollPane;
    
    private VisualTacticalGrid() {
        tiles = new VisualTacticalTile[TacticalMapData.TILE_LENGTH];
    }
    
    public static VisualTacticalGrid create() {
        VisualTacticalGrid product = new VisualTacticalGrid();
        
        product.tilePanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return getMinimumSize();
            }
            
            @Override
            public Dimension getMaximumSize() {
                return getMinimumSize();
            }
        };
        
        JPanel vPadding = new JPanel();
        vPadding.setLayout(new BoxLayout(vPadding, BoxLayout.Y_AXIS));
        
        JPanel hPadding = new JPanel();
        hPadding.setLayout(new BoxLayout(hPadding, BoxLayout.X_AXIS));
        
        hPadding.add(Box.createHorizontalGlue());
        hPadding.add(product.tilePanel);
        hPadding.add(Box.createHorizontalGlue());
        
        vPadding.add(Box.createVerticalGlue());
        vPadding.add(hPadding);
        vPadding.add(Box.createVerticalGlue());
        
        product.scrollPane = new JScrollPane(vPadding);
        
        int dimWithBorders = TacticalMapData.MAX_DIM + 2;
        
        product.tilePanel.setLayout(new GridLayout(dimWithBorders, dimWithBorders));
        product.tilePanel.setBackground(Color.BLACK);
        hPadding.setBackground(Color.BLACK);
        vPadding.setBackground(Color.BLACK);
        product.scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        product.scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);
        
        String[] coordinateLetters = {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P"
        };
        
        // Northern border
        for (int i = 0; i < dimWithBorders; i++) {
            if (i == 0) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.NORTHWEST, false));
            }
            else if (i == dimWithBorders - 1) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.NORTHEAST, false));
            }
            else {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.NORTH, i % 2 == 0, coordinateLetters[i - 1]));
            }
        }
        
        // Create visual tiles
        int k = -1;
        for (int i = 0; i < TacticalMapData.TILE_LENGTH; i++) {
            int j = i % TacticalMapData.MAX_DIM;
            if (j == 0) {
                k++;
            }
            String coordinateNumber = "" + (TacticalMapData.MAX_DIM - k);
            if (j == 0) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.WEST, k % 2 == 0, coordinateNumber));
            }
            VisualTacticalTile visualTile = new VisualTacticalTile();
            visualTile.addMouseListener(visualTile);
            product.tiles[i] = visualTile;
            product.tilePanel.add(visualTile);
            if (j == TacticalMapData.MAX_DIM - 1) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.EAST, k % 2 == 1, coordinateNumber));
            }
        }
        
        // Southern border
        for (int i = 0; i < dimWithBorders; i++) {
            if (i == 0) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.SOUTHWEST, false));
            }
            else if (i == dimWithBorders - 1) {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.SOUTHEAST, false));
            }
            else {
                product.tilePanel.add(new RulerMapTile(RulerMapTile.SOUTH, i % 2 == 1, coordinateLetters[i - 1]));
            }
        }
        
        // Load default map
        product.loadMap(TacticalMapData.create());
        
        return product;
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    public void loadMap(TacticalMapData tileData) {
        this.tileData = tileData;
        SwingUtilities.invokeLater(() -> {
            TacticalTileData[] dataTiles = tileData.getTiles();
            for (int i = 0; i < tiles.length; i++) {
                tiles[i].data = dataTiles[i];
                tiles[i].cachedRaiseSprite =
                        SpriteTilePatternSwitch.IS_RAISED_SWITCH.getSprite(dataTiles[i]);
            }
            tilePanel.revalidate();
            tilePanel.repaint();
        });
    }
}
