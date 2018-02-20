package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.CreativeBottom;
import de.canitzp.creativebottom.DataCheck;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.*;

/**
 * @author canitzp
 */
public class FeaturePlayer implements IFeature {

    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb) {
        handler.registerListener(EntityDamageEvent.class, (result, event) -> { // Cancel the damage if player is in creative mode
            if(event.entity instanceof AbstractEntityPlayer && !((AbstractEntityPlayer) event.entity).world.isClient()){
                if(DataCheck.from(event.entity).isCreative()){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(EntityDeathEvent.class, (result, event) -> { // Cancel the death if player is in creative mode
            if(event.entity instanceof AbstractEntityPlayer && !((AbstractEntityPlayer) event.entity).world.isClient()){
                if(DataCheck.from(event.entity).isCreative()){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ResetMovedPlayerEvent.class, (result, event) -> {
            if(event.player != null && !event.player.world.isClient()){
                if(DataCheck.from(event.player).isFlying()){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(PlayerLeaveWorldEvent.class, (result, event) -> {
            if(event.player != null && !event.player.world.isClient()){
                DataCheck data = DataCheck.from(event.player);
                if(data.isCreative()){
                    data.setGhost(false);
                    data.setShowLightLevel(false);
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(AddBreakProgressEvent.class, (result, event) -> {
            if(!event.player.world.isClient()){
                if (DataCheck.from(event.player).isCreative()) {
                    event.progressAdded = 1;
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
    }

}
