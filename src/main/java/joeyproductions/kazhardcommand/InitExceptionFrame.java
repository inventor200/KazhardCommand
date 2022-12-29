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

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 * A factory for an emergency exception-catching GUI.
 * @author Joseph Cramsey
 */
public class InitExceptionFrame {
    
    private final Exception ex;
    private JFrame exFrame;
    
    private InitExceptionFrame(Exception ex) {
        this.ex = ex;
    }
    
    public static void showFor(Exception ex) {
        InitExceptionFrame product = new InitExceptionFrame(ex);
        SwingUtilities.invokeLater(() -> {
            // Show a GUI of what went wrong.
            JFrame exFrame = new JFrame("Init Exception");
            product.exFrame = exFrame;
            JPanel exPanel = new JPanel(new BorderLayout());
            exFrame.setContentPane(exPanel);
            JTextPane exPane = new JTextPane();
            exPane.setEditable(false);
            exPane.setForeground(new Color(1f, 0.5f, 0.5f, 1f));
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                String utf8 = StandardCharsets.UTF_8.name();
                PrintStream exStream = new PrintStream(outStream, true, utf8);
                product.ex.printStackTrace(exStream);
                exPane.setText(outStream.toString(utf8));
            } catch (UnsupportedEncodingException ex1) {
                // Wow
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1);
            }
            int padding = 8;
            exPane.setBorder(BorderFactory.createEmptyBorder(
                    padding, padding, padding, padding
            ));
            JScrollPane scrollPane = new JScrollPane(exPane);
            exPanel.add(scrollPane, BorderLayout.CENTER);
            exFrame.setSize(600, 400);
            exFrame.setResizable(false);
            exFrame.setLocationRelativeTo(null);
            exFrame.setVisible(true);
            exFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
