package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

/**
 * @author canitzp
 */
public class GuiCreative extends GuiContainer {

    public GuiCreative(AbstractEntityPlayer player) {
        super(player, 158, 83);
    }

    @Override
    public ResourceName getName() {
        return new ResourceName(CreativeBottom.INSTANCE, "creative");
    }
}
