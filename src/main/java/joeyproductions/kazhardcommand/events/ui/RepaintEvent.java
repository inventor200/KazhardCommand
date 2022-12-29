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
import javax.swing.SwingUtilities;
import joeyproductions.kazhardcommand.events.SessionEvent;

/**
 * An event for calling distant repaint request.
 * @author Joseph Cramsey
 */
public class RepaintEvent implements SessionEvent {
    
    private final int destination;
    private final boolean revalidate;
    
    public RepaintEvent(int destination, boolean revalidate) {
        this.destination = destination;
        this.revalidate = revalidate;
    }
    
    public RepaintEvent(int destination) {
        this(destination, false);
    }
    
    public int getDestinationType() {
        return destination;
    }
    
    public boolean requestsRevalidation() {
        return revalidate;
    }
    
    public void submitToEvent(RepaintHandler handler) {
        Component component = handler.getRepaintTarget();
        if (requestsRevalidation()) {
            SwingUtilities.invokeLater(() -> {
                handler.beforeRevalidate();
                component.revalidate();
                handler.beforeRepaint();
                component.repaint();
            });
        }
        else {
            SwingUtilities.invokeLater(() -> {
                handler.beforeRepaint();
                component.repaint();
            });
        }
    }
}
