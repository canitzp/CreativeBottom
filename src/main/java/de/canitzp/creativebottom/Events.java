package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.*;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.gui.component.ComponentScrollBar;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

/**
 * @author canitzp
 */
public class Events {

    public static final ContainerCreative.CreativeInventory inventory = new ContainerCreative.CreativeInventory();

    public static void init(IEventHandler handler){
        handler.registerListener(EntityTickEvent.class, (result, event) -> {
            Entity entity = event.entity;
            if (entity instanceof AbstractEntityPlayer) {
                if (RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) entity)) {
                    boolean valid = RockBottomAPI.getGame().getGuiManager().getGui() == null;
                    Input input = RockBottomAPI.getGame().getInput();
                    DataSet data;
                    if (input.isKeyPressed(Keyboard.KEY_C) && valid) {
                        boolean before = false;
                        if (entity.getAdditionalData() != null) {
                            data = entity.getAdditionalData();
                            before = data.getBoolean("is_creative");
                        } else {
                            data = new DataSet();
                        }

                        data.addBoolean("is_creative", !before);
                        entity.fallAmount = 0;
                        data.addBoolean("is_flying", false);
                        data.addBoolean("pass_trough_world", false);
                        entity.setAdditionalData(data);
                    }

                    data = entity.getAdditionalData();
                    if (entity.getAdditionalData() != null && data.getBoolean("is_creative")) {
                        if (input.isKeyPressed(Keyboard.KEY_LMENU) && valid) {
                            data.addBoolean("is_flying", !data.getBoolean("is_flying"));
                            entity.setAdditionalData(data);
                        } else if (input.isKeyPressed(Keyboard.KEY_T) && valid) {
                            data.addBoolean("pass_trough_world", !data.getBoolean("pass_trough_world"));
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
        handler.registerListener(WorldRenderEvent.class, (result, event) -> {
            if (event.world != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                DataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    Font font = event.assetManager.getFont();
                    font.drawString(0.0F, 0.0F, FormattingCode.ORANGE.toString() + "Creative mode (" + CreativeBottom.INSTANCE.getVersion() + ")", 0.0175F);
                    font.drawString(0.0F, 0.4F, String.format(" Flying: %s", data.getBoolean("is_flying") ? "ON" : "OFF"), 0.0175F);
                    font.drawString(0.0F, 0.8F, String.format(" Ghost:  %s", data.getBoolean("pass_trough_world") ? "ON" : "OFF"), 0.0175F);
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
                        GuiCreative creative = new GuiCreative(RockBottomAPI.getGame().getPlayer(), inventory);
                        RockBottomAPI.getGame().getGuiManager().openGui(creative);
                        return EventResult.CANCELLED;
                    }
                } else if(event.gui.getName().getResourceName().equals("main_menu")){
                    RockBottomAPI.getGame().getGuiManager().openGui(new CustomMainMenu(event.gui));
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(ContainerOpenEvent.class, (result, event) -> {
            if (event.container != null && event.container.getName().getResourceName().equals("inventory")) {
                AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                DataSet data = player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    player.openContainer(new ContainerCreative(player, inventory));
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
    }

}
