package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IInputHandler;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.assets.font.IFont;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.*;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;
import org.lwjgl.input.Keyboard;

/**
 * @author canitzp
 */
public class Events {

    public static void init(IEventHandler handler){
        handler.registerListener(EntityTickEvent.class, (result, event) -> {
            Entity entity = event.entity;
            if (entity instanceof AbstractEntityPlayer) {
                if (RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) entity)) {
                    boolean valid = RockBottomAPI.getGame().getGuiManager().getGui() == null;
                    IInputHandler input = RockBottomAPI.getGame().getInput();
                    DataSet data;
                    if (CreativeBottom.creativeState.isPressed() && valid) {
                        boolean before = false;
                        if (entity.getAdditionalData() != null) {
                            data = entity.getAdditionalData();
                            before = data.getBoolean("is_creative");
                        } else {
                            data = new DataSet();
                        }
                        data.addBoolean("is_creative", !before);
                        entity.fallStartY = 0;
                        data.addBoolean("is_flying", false);
                        data.addBoolean("pass_trough_world", false);
                        data.addBoolean("light_level", false);
                        entity.setAdditionalData(data);
                    }

                    data = entity.getAdditionalData();
                    if (entity.getAdditionalData() != null && data.getBoolean("is_creative")) {
                        if (CreativeBottom.flyingState.isPressed() && valid) {
                            data.addBoolean("is_flying", !data.getBoolean("is_flying"));
                            entity.setAdditionalData(data);
                        } else if (CreativeBottom.ghostState.isPressed() && valid) {
                            data.addBoolean("pass_trough_world", !data.getBoolean("pass_trough_world"));
                            entity.setAdditionalData(data);
                        } else if(input.isKeyDown(Keyboard.KEY_L) && valid){
                            data.addBoolean("light_level", !data.getBoolean("light_level"));
                            entity.setAdditionalData(data);
                        }

                        if (data.getBoolean("is_flying")) {
                            ((AbstractEntityPlayer) entity).motionY = 0.025D;
                            if (input.isKeyDown(Keyboard.KEY_W) && valid) {
                                entity.motionY += !input.isKeyDown(157) && !input.isKeyDown(29) ? 0.2D : 0.4D;
                            } else if (input.isKeyDown(Keyboard.KEY_S) && valid) {
                                entity.motionY -= !input.isKeyDown(157) && !input.isKeyDown(29) ? 0.2D : 0.4D;
                            } else if (input.isKeyDown(Keyboard.KEY_LCONTROL) || input.isKeyDown(Keyboard.KEY_RCONTROL) && valid) {
                                if (input.isKeyDown(Keyboard.KEY_A)) {
                                    ((AbstractEntityPlayer) entity).move(0);
                                } else if (input.isKeyDown(Keyboard.KEY_D)) {
                                    ((AbstractEntityPlayer) entity).move(1);
                                }
                            }
                        }
                    }
                }
                return EventResult.MODIFIED;
            } else {
                return EventResult.DEFAULT;
            }
        });
        handler.registerListener(EntityDamageEvent.class, (result, event) -> event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity) && event.entity.getAdditionalData() != null && event.entity.getAdditionalData().getBoolean("is_creative") ? EventResult.CANCELLED : EventResult.DEFAULT);
        handler.registerListener(OverlayRenderEvent.class, (result, event) -> {
            if (event.player != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                IWorld world = event.player.world;
                DataSet data = event.player.getAdditionalData();
                if (world != null && data != null && data.getBoolean("is_creative")) {
                    IFont font = event.assetManager.getFont();
                    font.drawString(0.0F, 00.0F, FormattingCode.ORANGE.toString() + "Creative mode (" + CreativeBottom.INSTANCE.getVersion() + ")", 0.175F);
                    font.drawString(0.0F, 03.6F, String.format(" Flying:      %s", data.getBoolean("is_flying") ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 07.2F, String.format(" Ghost:       %s", data.getBoolean("pass_trough_world") ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 10.8F, String.format(" Light-Level: %s", data.getBoolean("light_level") ? "SHOWN" : "HIDDEN"), 0.175F);
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(WorldRenderEvent.class, (result, event) -> {
            if (event.player != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                IWorld world = event.player.world;
                DataSet data = event.player.getAdditionalData();
                if (world != null && data != null && data.getBoolean("is_creative") && data.getBoolean("light_level")) {
                    IFont font = event.assetManager.getFont();
                    for(int x = -10; x < 10; x++){
                        int realX = (int)event.player.x + x;
                        for(int y = -10; y < 10; y++){
                            int realY = (int)event.player.y + y;
                            Tile aboveTile = world.getState(realX, realY + 1).getTile();
                            Tile realTile = world.getState(realX, realY).getTile();
                            if((aboveTile == GameContent.TILE_AIR || (!realTile.isFullTile() || realTile.getBoundBox(world, realX, realY, TileLayer.MAIN) == null)) && realTile != GameContent.TILE_AIR){
                                font.drawString(realX + 0.175F - event.translationX, -realY - event.translationY, String.valueOf(world.getCombinedLight(realX, realY)), 0.02F);
                            }
                        }
                    }
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ComponentRenderEvent.class, (result, event) -> {
            if (event.gui == null && event.id == 8) {
                DataSet data = RockBottomAPI.getGame().getPlayer().getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(GuiOpenEvent.class, (result, event) -> {
            if (event.gui != null) {
                if(event.gui.getName().getResourceName().equals("inventory")){
                    AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                    DataSet data = player.getAdditionalData();
                    if (data != null && data.getBoolean("is_creative")) {
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
                DataSet data = player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    player.openContainer(new ContainerCreative(player));
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(EntityDeathEvent.class, (result, event) -> {
            if(event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity)){
                DataSet data = event.entity.getAdditionalData();
                if(data != null && data.getBoolean("is_creative")){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(AddBreakProgressEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player)){
                DataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    event.progressAdded = 1;
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(WorldObjectCollisionEvent.class, (result, event) -> {
            if(event.object instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.object)){
                DataSet data = ((AbstractEntityPlayer) event.object).getAdditionalData();
                if (data != null && data.getBoolean("is_creative") && data.getBoolean("pass_trough_world")) {
                    event.boundBoxes.clear();
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ResetMovedPlayerEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player) || RockBottomAPI.getGame().isDedicatedServer()) {
                DataSet data = event.player.getAdditionalData();
                System.out.println(data);
                if (data != null && data.getBoolean("is_creative") && data.getBoolean("is_flying")) {
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(PlayerLeaveWorldEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player)){
                DataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    data.addBoolean("light_level", false);
                    data.addBoolean("pass_trough_world", false);
                }
            }
            return EventResult.DEFAULT;
        });
    }

}
