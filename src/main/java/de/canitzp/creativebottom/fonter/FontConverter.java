package de.canitzp.creativebottom.fonter;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author canitzp
 */
public class FontConverter {

    public static JFrame mainFrame = new JFrame("Rock Bottom Font converter by canitzp");
    public static JTextField pathTTF = new JTextField();
    public static JButton buttonTTF = new JButton("...");
    public static JButton convert = new JButton("Convert");

    public static File fileTTF = null;

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainFrame.setMinimumSize(new Dimension(854, 480));
        mainFrame.setLayout(new BorderLayout());

        JPanel panelTTF = new JPanel(new BorderLayout());
        pathTTF.setEditable(false);
        panelTTF.add(pathTTF, BorderLayout.CENTER);
        panelTTF.add(buttonTTF, BorderLayout.EAST);
        convert.setEnabled(false);
        panelTTF.add(convert, BorderLayout.SOUTH); // cause here is space for it
        mainFrame.add(panelTTF, BorderLayout.NORTH);

        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        initLogic();
    }

    private static void initLogic(){
        buttonTTF.addActionListener(e -> {
            JFileChooser chooserTTF = new JFileChooser(fileTTF);
            chooserTTF.setFileFilter(new FileNameExtensionFilter("Font file (.ttf)", "ttf"));
            if(chooserTTF.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
                fileTTF = chooserTTF.getSelectedFile();
                pathTTF.setText(fileTTF.getAbsolutePath());
                convert.setEnabled(true);
            }
        });
        convert.addActionListener(e -> {
            try {
                ImageIO.write(stringToBufferedImage("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!?.,:;-_+'#*~/\\@=\"^<>[]{}|&()0123456789"), "png", new File(fileTTF.getParentFile(), fileTTF.getName().replace(".ttf", ".png")));
                Files.write(new File(fileTTF.getParentFile(), fileTTF.getName().replace(".ttf", ".info")).toPath(), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!?.,:;-_+'#*~/\\@=\"^<>[]{}|&()0123456789".getBytes());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public static BufferedImage stringToBufferedImage(String s) throws IOException, FontFormatException {
        //First, we have to calculate the string's width and height

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = img.getGraphics();

        //Set the font to be used when drawing the string
        Font f = Font.createFont(Font.TRUETYPE_FONT, fileTTF).deriveFont(Font.PLAIN).deriveFont(20F);
        g.setFont(f);

        //Get the string visual bounds
        FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
        Rectangle2D rect = f.getStringBounds(s, frc);
        //Release resources
        g.dispose();

        //Then, we have to draw the string on the final image

        //Create a new image where to print the character
        img = new BufferedImage((int) Math.ceil(rect.getWidth()), (int) Math.ceil(rect.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
        g = img.getGraphics();
        g.setColor(Color.white); //Otherwise the text would be white
        g.setFont(f);

        //Calculate x and y for that string
        FontMetrics fm = g.getFontMetrics();
        int x = 0;
        int y = fm.getAscent(); //getAscent() = baseline
        g.drawString(s, x, y);

        //Release resources
        g.dispose();

        //Return the image

        return img;
    }

}
