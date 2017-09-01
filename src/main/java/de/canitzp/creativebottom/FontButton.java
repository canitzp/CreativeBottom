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
        super(gui, 0, 0, 200, 16, null, text);
        this.font = font;
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, Graphics g) {
        if(this.hasBackground){
            g.setColor(this.isMouseOverPrioritized(game) ? this.colorButton : this.colorButtonUnselected);
            g.fillRect(this.x, this.y, this.sizeX, this.sizeY);

            g.setColor(this.colorOutline);
            g.drawRect(this.x, this.y, this.sizeX, this.sizeY);
        }

        String text = this.getText();
        if(text != null){
            this.font.drawCenteredString(this.x+this.sizeX/2F, this.y+this.sizeY/2F+0.5F, text, 0.35F, true);
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
