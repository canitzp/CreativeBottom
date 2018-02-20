package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.CreativeBottom;
import de.canitzp.creativebottom.DataCheck;
import de.canitzp.creativebottom.Util;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.data.settings.Settings;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.EntityTickEvent;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;

/**
 * @author canitzp
 */
public class FeatureCreativeBottom implements IFeature {

    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb) {
        handler.registerListener(EntityTickEvent.class, (result, event) -> {
            if(event.entity instanceof AbstractEntityPlayer && !game.isDedicatedServer()){
                AbstractEntityPlayer player = (AbstractEntityPlayer) event.entity;
                DataCheck data = DataCheck.fromOrCreate(player);

                if(game.getGuiManager().getGui() == null){
                    if(CreativeBottom.creativeState.isPressed()){ // change creative mode
                        data.setCreative(!data.isCreative());
                        player.fallStartY = 0;
                        data.setFlying(false);
                        data.setGhost(false);
                        data.setShowLightLevel(false);
                    }

                    if(data.isCreative()){ // change other modes
                        if(CreativeBottom.flyingState.isPressed()){
                            data.setFlying(!data.isFlying());
                        }
                        if(CreativeBottom.ghostState.isPressed()){
                            data.setGhost(!data.isGhost());
                        }
                        if(CreativeBottom.lightLevelState.isPressed()){
                            data.setShowLightLevel(!data.doesShowLightLevel());
                        }
                    }

                    if(data.isCreative() && CreativeBottom.middleMouse.isPressed()){ // Pick tile under mouse
                        Item item = Util.getTileUnderMouse(player);
                        if(item != null){
                            Inventory inv = player.getInv();
                            if(inv.get(player.getSelectedSlot()) == null){
                                inv.set(player.getSelectedSlot(), new ItemInstance(item, item.getMaxAmount()));
                            }
                        }
                    }
                }

                if(data.isFlying()){ // handle flying movement
                    player.motionY = 0.025D; // balance the player
                    if(CreativeBottom.upFly.isDown()){
                        player.motionY += !CreativeBottom.controlKey.isDown() ? 0.2D : 0.4D;
                    } else if(CreativeBottom.downFly.isDown()){
                        player.motionY -= !CreativeBottom.controlKey.isDown() ? 0.2D : 0.4D;
                    } else if (CreativeBottom.controlKey.isDown()) {
                        if (Settings.KEY_LEFT.isDown()) {
                            player.motionX -= 0.4D;
                        } else if (Settings.KEY_RIGHT.isDown()) {
                            player.motionX += 0.4D;
                        }
                    }
                }

                return EventResult.MODIFIED;
            }
            return EventResult.DEFAULT;
        });
    }

}
