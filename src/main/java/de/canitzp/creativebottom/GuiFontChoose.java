package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.ComponentButton;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import org.newdawn.slick.Graphics;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class GuiFontChoose extends Gui {

    private List<FontButton> fontButtons = new ArrayList<>();

    public GuiFontChoose(Gui parent) {
        super(100, 100, parent);
        for(Map.Entry<String, Font> entry : CreativeBottom.FONTS.entrySet()){
            this.fontButtons.add(new FontButton(this, entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void initGui(IGameInstance game) {
        super.initGui(game);

        int width = (int)game.getWidthInGui();
        int parts = width/4;
        int buttonWidth = 70;
        int start = (parts-buttonWidth)/2;
        int y = (int)game.getHeightInGui()-20;

        this.components.add(new ComponentButton(this, 0, width/2 + 2, y, buttonWidth, 16, "Back"));
        this.components.add(new ComponentButton(this, 1, width/2-buttonWidth - 2, y, buttonWidth, 16, "Secret (WIP)"));

    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, Graphics g) {
        int x = (int) (game.getWidthInGui()/2 - 100);
        int y = 20;
        g.fillRect(x, y, 200, (float) game.getHeightInGui() - 50);
        y += 2;
        for(FontButton font : this.fontButtons) {
            font.render(game, manager, g, x + 2, y, 196);
            y += 12;
        }
        super.render(game, manager, g);
    }

    @Override
    public boolean onButtonActivated(IGameInstance game, int button) {
        if(button == 0){
            game.getGuiManager().openGui(this.parent);
            return true;
        }
        return super.onButtonActivated(game, button);
    }

    @Override
    public boolean onMouseAction(IGameInstance game, int button, float x, float y) {
        for(FontButton fontButton : this.fontButtons){
            if(fontButton.onMouseAction(game, button, x, y)){
                return true;
            }
        }
        return super.onMouseAction(game, button, x, y);
    }

    @Override
    public boolean isMouseOverPrioritized(IGameInstance game, GuiComponent component) {
        for(FontButton fontButton : fontButtons){
            if(fontButton.isMouseOver(game) && component == fontButton){
                return true;
            }
        }
        return super.isMouseOverPrioritized(game, component);
    }

    @Override
    public IResourceName getName() {
        return RockBottomAPI.createRes(CreativeBottom.INSTANCE, "font");
    }
}
