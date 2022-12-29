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
package joeyproductions.kazhardcommand.events.ui;

import java.awt.Component;
import java.util.ArrayList;

/**
 * An interface for handling repaint events.
 * @author Joseph Cramsey
 */
public abstract class RepaintHandler {
    
    public static final int SESSION_FRAME = 0;
    public static final int VISIBLE_GRID = 1;
    
    private final ArrayList<RepaintChild> repaintableChildren = new ArrayList<>();
    
    public abstract Component getRepaintTarget();
    public abstract boolean isValidRepaintTarget();
    
    public void beforeRepaint() {
        // Can be overridden.
    }
    
    public void beforeRevalidate() {
        // Can be overridden.
    }
    
    public void handleRepaintEvent(RepaintEvent event) {
        boolean foundPlausibleChild = false;
        
        for (RepaintChild checkChild : repaintableChildren) {
            if (checkChild.hasDestinationType(event.getDestinationType())) {
                foundPlausibleChild = true;
            }
            else {
                continue;
            }
            if (!checkChild.getHandler().isValidRepaintTarget()) continue;
            checkChild.getHandler().handleRepaintEvent(event);
        }
        
        if (foundPlausibleChild) return;
        
        // This is not going to any child, so it must be for us.
        event.submitToEvent(this);
    }
    
    protected void addRepaintableChild(RepaintHandler child, int destinationType) {
        for (RepaintChild checkChild : repaintableChildren) {
            if (checkChild.getHandler() == child) {
                checkChild.addDestinationType(destinationType);
                return;
            }
        }
        
        repaintableChildren.add(new RepaintChild(child, destinationType));
    }
}
