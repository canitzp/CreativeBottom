package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

/**
 * @author canitzp
 */
public class GuiCreative extends GuiContainer {

    public GuiCreative(AbstractEntityPlayer player) {
        super(player, 158, 83);
    }

    @Override
    public IResourceName getName() {
        return RockBottomAPI.createRes(CreativeBottom.INSTANCE, "creative");
    }
}
