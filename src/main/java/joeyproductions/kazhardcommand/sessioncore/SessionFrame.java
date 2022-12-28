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
package joeyproductions.kazhardcommand.sessioncore;

import joeyproductions.kazhardcommand.sessioncore.ui.VisualTacticalGrid;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import joeyproductions.kazhardcommand.Main;

/**
 * The root window that handles a single game.
 * @author Joseph Cramsey
 */
public class SessionFrame {
    
    private final JFrame jframe;
    private final JPanel contentPanel;
    private VisualTacticalGrid tileGrid;
    
    private SessionFrame() {
        jframe = new JFrame("Kazhard Command");
        contentPanel = new JPanel();
    }
    
    public static SessionFrame create() {
        SessionFrame product = new SessionFrame();
        
        SwingUtilities.invokeLater(() -> {
            product.jframe.setSize(
                    Math.min(
                            Main.getScreenWidth() - Main.SCREEN_EDGE_BUFFER, 
                            Main.SCREEN_STANDARD_WIDTH
                    ), 
                    Math.min(
                            Main.getScreenHeight() - Main.SCREEN_EDGE_BUFFER, 
                            Main.SCREEN_STANDARD_HEIGHT
                    )
            );
            
            product.jframe.setResizable(true);
            product.jframe.setLocationRelativeTo(null);
            product.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            product.contentPanel.setLayout(new BorderLayout());
            product.jframe.setContentPane(product.contentPanel);
            
            product.tileGrid = VisualTacticalGrid.create();
            product.contentPanel.add(product.tileGrid.getScrollPane(), BorderLayout.CENTER);
            
            product.jframe.setVisible(true);
        });
        
        return product;
    }
    
    private static SessionFrame getSingleton() {
        return Main.getSessionFrame();
    }
    
    private static void checkSessionFrame() {
        if (getSingleton() == null) {
            throw new RuntimeException("There is no SessionFrame.");
        }
    }
    
    public static void refresh() {
        checkSessionFrame();
        
        SwingUtilities.invokeLater(() -> {
            getSingleton().jframe.repaint();
        });
    }
    
    public static void fullRefresh() {
        checkSessionFrame();
        
        SwingUtilities.invokeLater(() -> {
            getSingleton().jframe.revalidate();
            getSingleton().jframe.repaint();
        });
    }
}
