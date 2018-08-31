package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IInputHandler;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.assets.font.IFont;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.data.set.ModBasedDataSet;
import de.ellpeck.rockbottom.api.data.settings.Settings;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.*;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.util.Util;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.util.List;

/**
 * @author canitzp
 */
public class Events {
    
    public static final ResourceName IS_CREATIVE = new ResourceName(CreativeBottom.INSTANCE, "is_creative");
    public static final ResourceName IS_FLYING = new ResourceName(CreativeBottom.INSTANCE, "is_flying");
    public static final ResourceName PASS_THROUGH_WORLD = new ResourceName(CreativeBottom.INSTANCE, "pass_trough_world");
    public static final ResourceName LIGHT_LEVEL = new ResourceName(CreativeBottom.INSTANCE, "light_level");

    public static void init(IEventHandler handler){
        handler.registerListener(EntityTickEvent.class, (result, event) -> {
            Entity entity = event.entity;
            if (entity instanceof AbstractEntityPlayer) {
                if (RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) entity)) {
                    boolean valid = RockBottomAPI.getGame().getGuiManager().getGui() == null;
                    IInputHandler input = RockBottomAPI.getGame().getInput();
                    ModBasedDataSet data;
                    if (CreativeBottom.creativeState.isPressed() && valid) {
                        boolean before = false;
                        if (entity.getAdditionalData() != null) {
                            data = entity.getAdditionalData();
                            before = data.getBoolean(IS_CREATIVE);
                        } else {
                            data = new ModBasedDataSet();
                        }
                        data.addBoolean(IS_CREATIVE, !before);
                        entity.fallStartY = 0;
                        data.addBoolean(IS_FLYING, false);
                        data.addBoolean(PASS_THROUGH_WORLD, false);
                        data.addBoolean(LIGHT_LEVEL, false);
                        entity.setAdditionalData(data);
                    }

                    data = entity.getAdditionalData();
                    if (entity.getAdditionalData() != null && data.getBoolean(IS_CREATIVE)) {
                        if (CreativeBottom.flyingState.isPressed() && valid) {
                            data.addBoolean(IS_FLYING, !data.getBoolean(IS_FLYING));
                            entity.setAdditionalData(data);
                        } else if (CreativeBottom.ghostState.isPressed() && valid) {
                            data.addBoolean(PASS_THROUGH_WORLD, !data.getBoolean(PASS_THROUGH_WORLD));
                            entity.setAdditionalData(data);
                        } else if(CreativeBottom.lightLevelState.isPressed() && valid){
                            data.addBoolean(LIGHT_LEVEL, !data.getBoolean(LIGHT_LEVEL));
                            entity.setAdditionalData(data);
                        }

                        if (data.getBoolean(IS_FLYING)) {
                            ((AbstractEntityPlayer) entity).motionY = 0.025D;
                            if (CreativeBottom.upFly.isDown() && valid) {
                                entity.motionY += !CreativeBottom.controlKey.isDown() ? 0.2D : 0.4D;
                            } else if (CreativeBottom.downFly.isDown() && valid) {
                                entity.motionY -= !CreativeBottom.controlKey.isDown() ? 0.2D : 0.4D;
                            } else if (CreativeBottom.controlKey.isDown() && valid) {
                                if (Settings.KEY_LEFT.isDown()) {
                                    ((AbstractEntityPlayer) entity).motionX -= 0.4D;
                                } else if (Settings.KEY_RIGHT.isDown()) {
                                    ((AbstractEntityPlayer) entity).motionX += 0.4D;
                                }
                            }
                        }
                        if(valid && CreativeBottom.middleMouse.isPressed()){
                            for(TileLayer layer : TileLayer.getLayersByInteractionPrio()){
                                int x = Util.floor(RockBottomAPI.getGame().getRenderer().getMousedTileX());
                                int y = Util.floor(RockBottomAPI.getGame().getRenderer().getMousedTileY());
                                Tile tile = ((AbstractEntityPlayer) entity).world.getState(layer, x, y).getTile();
                                if(tile != null){
                                    Item item = tile.getItem();
                                    if(item == null){
                                        List<ItemInstance> drops = tile.getDrops(((AbstractEntityPlayer) entity).world, x, y, layer, entity);
                                        if(!drops.isEmpty()){
                                            item = drops.get(0).getItem();
                                        }
                                    }
                                    if(item != null){
                                        Inventory inv = ((AbstractEntityPlayer) entity).getInv();
                                        if(inv.get(((AbstractEntityPlayer) entity).getSelectedSlot()) == null){
                                            inv.set(((AbstractEntityPlayer) entity).getSelectedSlot(), new ItemInstance(tile, item.getMaxAmount()));
                                        }
                                    }
                                    break;
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
        handler.registerListener(EntityDamageEvent.class, (result, event) -> event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity) && event.entity.getAdditionalData() != null && event.entity.getAdditionalData().getBoolean(IS_CREATIVE) ? EventResult.CANCELLED : EventResult.DEFAULT);
        handler.registerListener(OverlayRenderEvent.class, (result, event) -> {
            if (event.player != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                IWorld world = event.player.world;
                ModBasedDataSet data = event.player.getAdditionalData();
                if (world != null && data != null && data.getBoolean(IS_CREATIVE)) {
                    IFont font = event.assetManager.getFont();
                    font.drawString(0.0F, 00.0F, FormattingCode.ORANGE.toString() + "Creative mode (" + CreativeBottom.INSTANCE.getVersion() + ")", 0.175F);
                    font.drawString(0.0F, 03.6F, String.format(" Flying:      %s", data.getBoolean(IS_FLYING) ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 07.2F, String.format(" Ghost:       %s", data.getBoolean(PASS_THROUGH_WORLD) ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 10.8F, String.format(" Light-Level: %s", data.getBoolean(LIGHT_LEVEL) ? "SHOWN" : "HIDDEN"), 0.175F);
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(WorldRenderEvent.class, (result, event) -> {
            if (event.player != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                IWorld world = event.player.world;
                ModBasedDataSet data = event.player.getAdditionalData();
                if (world != null && data != null && data.getBoolean(IS_CREATIVE) && data.getBoolean(LIGHT_LEVEL)) {
                    IFont font = event.assetManager.getFont();
                    for(int x = -10; x < 10; x++){
                        int realX = (int)event.player.getX() + x;
                        for(int y = -10; y < 10; y++){
                            int realY = (int)event.player.getY() + y;
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
                ModBasedDataSet data = RockBottomAPI.getGame().getPlayer().getAdditionalData();
                if (data != null && data.getBoolean(IS_CREATIVE)) {
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(GuiOpenEvent.class, (result, event) -> {
            if (event.gui != null) {
                if(event.gui.getName().getResourceName().equals("inventory")){
                    AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                    ModBasedDataSet data = player.getAdditionalData();
                    if (data != null && data.getBoolean(IS_CREATIVE)) {
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
                ModBasedDataSet data = player.getAdditionalData();
                if (data != null && data.getBoolean(IS_CREATIVE)) {
                    player.openContainer(new ContainerCreative(player));
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(EntityDeathEvent.class, (result, event) -> {
            if(event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity)){
                ModBasedDataSet data = event.entity.getAdditionalData();
                if(data != null && data.getBoolean(IS_CREATIVE)){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(AddBreakProgressEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player)){
                ModBasedDataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean(IS_CREATIVE)) {
                    event.progressAdded = 1;
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(WorldObjectCollisionEvent.class, (result, event) -> {
            if(event.object instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.object)){
                ModBasedDataSet data = ((AbstractEntityPlayer) event.object).getAdditionalData();
                if (data != null && data.getBoolean(IS_CREATIVE) && data.getBoolean(PASS_THROUGH_WORLD)) {
                    event.boundBoxes.clear();
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ResetMovedPlayerEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player) || RockBottomAPI.getGame().isDedicatedServer()) {
                ModBasedDataSet data = event.player.getAdditionalData();
                System.out.println(data);
                if (data != null && data.getBoolean(IS_CREATIVE) && data.getBoolean(IS_FLYING)) {
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(PlayerLeaveWorldEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player)){
                ModBasedDataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean(IS_CREATIVE)) {
                    data.addBoolean(LIGHT_LEVEL, false);
                    data.addBoolean(PASS_THROUGH_WORLD, false);
                }
            }
            return EventResult.DEFAULT;
        });
    }

}
