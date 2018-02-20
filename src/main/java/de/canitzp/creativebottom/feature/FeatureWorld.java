package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.CreativeBottom;
import de.canitzp.creativebottom.DataCheck;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.AddBreakProgressEvent;
import de.ellpeck.rockbottom.api.event.impl.WorldObjectCollisionEvent;

/**
 * @author canitzp
 */
public class FeatureWorld implements IFeature {

    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb) {
        handler.registerListener(WorldObjectCollisionEvent.class, (result, event) -> {
            if(event.object instanceof AbstractEntityPlayer && !event.object.world.isClient()){
                if (DataCheck.from((Entity) event.object).isGhost()) {
                    event.boundBoxes.clear();
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });

    }

}
