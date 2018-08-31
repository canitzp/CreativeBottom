package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.ContainerCreative;
import de.canitzp.creativebottom.CreativeBottom;
import de.canitzp.creativebottom.DataCheck;
import de.canitzp.creativebottom.GuiCreative;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.set.ModBasedDataSet;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.ContainerOpenEvent;
import de.ellpeck.rockbottom.api.event.impl.GuiOpenEvent;

public class OldInvOverride implements IFeature{
    
    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb){
        handler.registerListener(GuiOpenEvent.class, (result, event) -> {
            if (event.gui != null) {
                if(event.gui.getName().getResourceName().equals("inventory")){
                    AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                    if (DataCheck.from(player).isCreative()) {
                        GuiCreative creative = new GuiCreative(RockBottomAPI.getGame().getPlayer());
                        RockBottomAPI.getGame().getGuiManager().openGui(creative);
                        return EventResult.CANCELLED;
                    }
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ContainerOpenEvent.class, (result, event) -> {
            if (event.container != null && event.container.getName().getResourceName().equals("inventory")) {
                AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                if (DataCheck.from(player).isCreative()) {
                    player.openContainer(new ContainerCreative(player));
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
    }
}
