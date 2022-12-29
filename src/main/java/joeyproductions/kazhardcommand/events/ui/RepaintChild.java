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

import java.util.ArrayList;

/**
 * A connection between possible repaint destinations and a RepaintHandler.
 * @author Joseph Cramsey
 */
class RepaintChild {
    
    private final ArrayList<Integer> destinationTypes = new ArrayList<>();
    private final RepaintHandler handler;
    
    RepaintChild(RepaintHandler handler, int destinationType) {
        this.handler = handler;
        destinationTypes.add(destinationType);
    }
    
    void addDestinationType(int destinationType) {
        if (!hasDestinationType(destinationType)) {
            destinationTypes.add(destinationType);
        }
    }
    
    boolean hasDestinationType(int destinationType) {
        return destinationTypes.contains(destinationType);
    }
    
    RepaintHandler getHandler() {
        return handler;
    }
}
