package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.gui.component.ComponentButton;
import org.newdawn.slick.Graphics;

import java.lang.reflect.Field;

/**
 * @author canitzp
 */
public class FontButton extends ComponentButton {

    public Font font;

    public FontButton(GuiFontChoose gui, String text, Font font) {
        super(gui, 0, 0, 0, 0, 10, text);
        this.font = font;
    }

    public void render(IGameInstance game, IAssetManager manager, Graphics g, int x, int y, int width){
        if(this.hasBackground){
            this.x = x;
            this.y = y;
            this.sizeX = width;
            g.setColor(this.isMouseOverPrioritized(game) ? this.colorButton : this.colorButtonUnselected);
            g.fillRect(x, y, width, 10);

            g.setColor(this.colorOutline);
            g.drawRect(x, y, width, 10);
        }

        String text = this.getText();
        if(text != null){
            this.font.drawCenteredString(x + width/2F, y + 10/2F+0.5F, text, 0.35F, true);
        }
    }

    @Override
    public boolean onPressed(IGameInstance game) {
        try {
            setFont(game, this.font);
            CreativeBottom.settings.stringsToSave.put("font", this.text);
            game.getDataManager().savePropSettings(CreativeBottom.settings);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onPressed(game);
    }

    public static void setFont(IGameInstance game, Font font) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //Field field = Class.forName("de.ellpeck.rockbottom.assets.AssetManager").getDeclaredField("currentFont");
        //field.setAccessible(true);
        //field.set(game.getAssetManager(), font);
        game.getAssetManager().setFont(font);
    }
}
