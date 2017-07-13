package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.IGuiManager;
import de.ellpeck.rockbottom.api.gui.component.ComponentButton;
import org.newdawn.slick.Graphics;


/**
 * @author canitzp
 */
public class CustomMainMenu extends Gui {

    private Gui internalMainMenu;

    public CustomMainMenu() {
        super(100, 100);
        try {
            internalMainMenu = (Gui) Class.forName("de.ellpeck.rockbottom.gui.menu.GuiMainMenu").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui(IGameInstance game) {
        super.initGui(game);
        IAssetManager assetManager = game.getAssetManager();

        int width = (int)game.getWidthInGui();
        int parts = width/4;
        int buttonWidth = 70;
        int start = (parts-buttonWidth)/2;
        int y = (int)game.getHeightInGui()-30;

        this.components.add(new ComponentButton(this, 7, start+parts*3, y-12, buttonWidth, 10, "Fonts", "Change the font of Rock Bottom"));
        this.components.add(new ComponentButton(this, 0, start, y, buttonWidth, 16, assetManager.localize(RockBottomAPI.createInternalRes("button.play"))));
        this.components.add(new ComponentButton(this, 1, start+parts, y, buttonWidth, 16, assetManager.localize(RockBottomAPI.createInternalRes("button.join"))));
        this.components.add(new ComponentButton(this, 6, start+parts*2, y, buttonWidth, 16, assetManager.localize(RockBottomAPI.createInternalRes("button.player_editor"))));
        this.components.add(new ComponentButton(this, 2, start+parts*3, y, buttonWidth, 16, assetManager.localize(RockBottomAPI.createInternalRes("button.settings"))));
        this.components.add(new ComponentButton(this, 4, width-47, 2, 45, 10, assetManager.localize(RockBottomAPI.createInternalRes("button.credits"))));
        this.components.add(new ComponentButton(this, 5, width-47, 14, 45, 10, assetManager.localize(RockBottomAPI.createInternalRes("button.mods"))));
        this.components.add(new ComponentButton(this, 3, 2, 2, 45, 10, assetManager.localize(RockBottomAPI.createInternalRes("button.quit"))));
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, Graphics g){
        super.render(game, manager, g);
        Font font = manager.getFont();
        font.drawString(2.0F, (float) game.getHeightInGui() - 13.0F, "CreativeBottom by canitzp " + CreativeBottom.INSTANCE.getVersion(), 0.25F);
        this.internalMainMenu.render(game, manager, g);
    }

    @Override
    public boolean hasGradient(){
        return false;
    }

    @Override
    protected boolean tryEscape(IGameInstance game){
        return false;
    }

    @Override
    public boolean onButtonActivated(IGameInstance game, int button){
        IGuiManager guiManager = game.getGuiManager();

        if(button == 7){
            guiManager.openGui(new GuiFontChoose(this));
            return true;
        }

        return this.internalMainMenu.onButtonActivated(game, button);
    }

}
