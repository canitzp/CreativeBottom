package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.ComponentButton;
import de.ellpeck.rockbottom.api.gui.component.ComponentScrollMenu;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.util.BoundBox;
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

    public GuiFontChoose(Gui parent) {
        super(100, 100, parent);
    }

    @Override
    public void initGui(IGameInstance game) {
        super.initGui(game);

        int width = (int)game.getWidthInGui();
        int parts = width/4;
        int buttonWidth = 70;
        int start = (parts-buttonWidth)/2;
        int y = (int)game.getHeightInGui()-20;

        BoundBox box = new BoundBox(0, 0, 100, 128);
        ComponentScrollMenu scrollMenu = new ComponentScrollMenu(this, (int) game.getWidthInGui()/2 - 100, 20, 128, 1, 8, box);
        this.components.add(scrollMenu);

        this.components.add(new ComponentButton(this, width/2-buttonWidth/2, y, buttonWidth, 16, () ->{
            game.getGuiManager().openGui(this.parent);
            return true;
        } , "Back"));
        //this.components.add(new ComponentButton(this, 1, width/2-buttonWidth - 2, y, buttonWidth, 16, "Secret (WIP)"));

        for(Map.Entry<String, Font> entry : CreativeBottom.FONTS.entrySet()){
            scrollMenu.add(new FontButton(this, entry.getKey(), entry.getValue()));
        }
        scrollMenu.organize();

    }

    @Override
    public IResourceName getName() {
        return RockBottomAPI.createRes(CreativeBottom.INSTANCE, "font");
    }
}
