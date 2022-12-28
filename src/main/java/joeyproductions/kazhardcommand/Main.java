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
package joeyproductions.kazhardcommand;

import joeyproductions.kazhardcommand.spritecore.Sprite;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.github.weisj.darklaf.theme.Theme;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import joeyproductions.kazhardcommand.sessioncore.SessionFrame;
import joeyproductions.kazhardcommand.spritecore.SpriteTilePatternSwitch;
import joeyproductions.kazhardcommand.sessioncore.ui.VisualTacticalTile;

/**
 * The entry point class of the program.
 * @author Joseph Cramsey
 */
public class Main {
    
    private static Main SINGLETON;
    private SessionFrame sessionFrame;
    
    private int screenWidth = 640;
    private int screenHeight = 400;
    public static final int SCREEN_STANDARD_WIDTH = 1280;
    public static final int SCREEN_STANDARD_HEIGHT = 960;
    public static final int SCREEN_EDGE_BUFFER = 128;
    public static final int PIXEL_UPSCALE_STEPS = 2;
    
    public static void main(String[] args) {
        SINGLETON = new Main();
        
        Theme preferredTheme = new DarculaTheme();
        LafManager.setTheme(preferredTheme);
        LafManager.install();
        
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        SINGLETON.screenWidth = graphicsDevice.getDisplayMode().getWidth();
        SINGLETON.screenHeight = graphicsDevice.getDisplayMode().getHeight();
        
        // Load sprites
        Sprite.loadSprites();
        
        // Load sprite patterns
        SpriteTilePatternSwitch.loadSpritePatterns();
        
        // Load testing session frame
        SINGLETON.sessionFrame = SessionFrame.create();
    }
    
    public static Main getMain() {
        return SINGLETON;
    }
    
    public static SessionFrame getSessionFrame() {
        return SINGLETON.sessionFrame;
    }
    
    public static int getScreenWidth() {
        return SINGLETON.screenWidth;
    }
    
    public static int getScreenHeight() {
        return SINGLETON.screenHeight;
    }
}
