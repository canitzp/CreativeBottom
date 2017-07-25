package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.data.settings.Settings;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.IEventListener;
import de.ellpeck.rockbottom.api.event.impl.*;
import de.ellpeck.rockbottom.api.mod.IMod;
import de.ellpeck.rockbottom.api.world.IWorld;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author canitzp
 */
public class CreativeBottom implements IMod {

    public static final Map<String, Font> FONTS = new HashMap<>();

    public static CreativeSettings settings = new CreativeSettings();
    public static CreativeBottom INSTANCE;

    public CreativeBottom(){
        INSTANCE = this;
    }

    @Override
    public String getDisplayName() {
        return "Creative Bottom";
    }

    @Override
    public String getId() {
        return "creativebottom";
    }

    @Override
    public String getVersion() {
        return "a.5.0";
    }

    @Override
    public String getResourceLocation() {
        return null;
    }

    @Override
    public String getDescription() {
        return "This mod should provide a simple creative game mode, a bit like the minecraft one for Rock Bottom";
    }

    @Override
    public void preInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {
        FONTS.put("Default", game.getAssetManager().getFont());
        addFont("Old Default", "default");
        addFont("Vera Mono by Gnome", "VeraMono");
        addFont("Monospaced Typewriter by Manfred Klein", "MonospaceTypewriter");
        addFont("Fantasque Sans Mono by belluzj", "FantasqueSansMono-Regular");

        game.getDataManager().loadPropSettings(settings);
        if(settings.stringsToSave.containsKey("font")){
            String fontName = settings.stringsToSave.get("font");
            if(FONTS.containsKey(fontName)){
                try {
                    FontButton.setFont(game, FONTS.get(fontName));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void init(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {
        eventHandler.registerListener(EntityTickEvent.class, (result, event) -> {
            Entity entity = event.entity;
            if (entity instanceof AbstractEntityPlayer) {
                if(RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) entity)){
                    Input input = RockBottomAPI.getGame().getInput();
                    DataSet data;
                    if (input.isKeyPressed(Keyboard.KEY_C)) {
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
                        if (input.isKeyPressed(Keyboard.KEY_LMENU)) {
                            data.addBoolean("is_flying", !data.getBoolean("is_flying"));
                            entity.setAdditionalData(data);
                        } else if(input.isKeyPressed(Keyboard.KEY_T)){
                            data.addBoolean("pass_trough_world", !data.getBoolean("pass_trough_world"));
                            entity.setAdditionalData(data);
                        }

                        if (data.getBoolean("is_flying")) {
                            ((AbstractEntityPlayer) entity).motionY = 0.025D;
                            if (input.isKeyDown(Keyboard.KEY_W)) {
                                entity.motionY += !input.isKeyDown(157) && !input.isKeyDown(29) ? 0.2D : 0.4D;
                            } else if (input.isKeyDown(Keyboard.KEY_S)) {
                                entity.motionY -= !input.isKeyDown(157) && !input.isKeyDown(29) ? 0.2D : 0.4D;
                            } else if (input.isKeyDown(Keyboard.KEY_LCONTROL) || input.isKeyDown(Keyboard.KEY_RCONTROL)) {
                                if (input.isKeyDown(Keyboard.KEY_A)) {
                                    ((AbstractEntityPlayer) entity).move(0);
                                } else if (input.isKeyDown(Keyboard.KEY_D)) {
                                    ((AbstractEntityPlayer) entity).move(1);
                                }
                            }
                        }
                    }
                } else {
                    DataSet data = entity.getAdditionalData();
                    if(data != null && data.getBoolean("is_creative")){
                        entity.fallAmount = 0;
                    }
                }
                return EventResult.MODIFIED;
            } else {
                return EventResult.DEFAULT;
            }
        });
        eventHandler.registerListener(EntityDamageEvent.class, (result, event) -> event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity) && event.entity.getAdditionalData() != null && event.entity.getAdditionalData().getBoolean("is_creative") ? EventResult.CANCELLED : EventResult.DEFAULT);
        eventHandler.registerListener(WorldRenderEvent.class, (result, event) -> {
            if (event.world != null && RockBottomAPI.getNet().isThePlayer(event.player)) {
                DataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    Font font = event.assetManager.getFont();
                    font.drawString(0.0F, 0.0F, FormattingCode.ORANGE.toString() + "Creative mode (" + getVersion() + ")", 0.0175F);
                    font.drawString(0.0F, 0.4F, String.format(" Flying: %s", data.getBoolean("is_flying") ? "ON" : "OFF"), 0.0175F);
                    font.drawString(0.0F, 0.8F, String.format(" Ghost:  %s", data.getBoolean("pass_trough_world") ? "ON" : "OFF"), 0.0175F);
                }
            }
            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(ComponentRenderEvent.class, (result, event) -> {
            if (event.gui == null && event.id == 8) {
                DataSet data = RockBottomAPI.getGame().getPlayer().getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(GuiOpenEvent.class, (result, event) -> {
            if (event.gui != null) {
                if(event.gui.getClass().getName().equals("de.ellpeck.rockbottom.gui.GuiInventory")){
                    AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                    DataSet data = player.getAdditionalData();
                    if (data != null && data.getBoolean("is_creative")) {
                        RockBottomAPI.getGame().getGuiManager().openGui(new GuiCreative(RockBottomAPI.getGame().getPlayer()));
                        return EventResult.CANCELLED;
                    }
                } else if(event.gui.getClass().getName().equals("de.ellpeck.rockbottom.gui.menu.GuiMainMenu")){
                    RockBottomAPI.getGame().getGuiManager().openGui(new CustomMainMenu());
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(ContainerOpenEvent.class, (result, event) -> {
            if (event.container != null && event.container.getClass().getName().equals("de.ellpeck.rockbottom.gui.container.ContainerInventory")) {
                AbstractEntityPlayer player = RockBottomAPI.getGame().getPlayer();
                DataSet data = player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    player.openContainer(new ContainerCreative(player));
                    return EventResult.CANCELLED;
                }
            }

            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(EntityDeathEvent.class, (result, event) -> {
            if(event.entity instanceof AbstractEntityPlayer && RockBottomAPI.getNet().isThePlayer((AbstractEntityPlayer) event.entity)){
                DataSet data = event.entity.getAdditionalData();
                if(data != null && data.getBoolean("is_creative")){
                    return EventResult.CANCELLED;
                }
            }
            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(AddBreakProgressEvent.class, (result, event) -> {
            if(RockBottomAPI.getNet().isThePlayer(event.player)){
                DataSet data = event.player.getAdditionalData();
                if (data != null && data.getBoolean("is_creative")) {
                    event.progressAdded = 1;
                    return EventResult.MODIFIED;
                }
            }
            return EventResult.DEFAULT;
        });
        eventHandler.registerListener(WorldObjectCollisionEvent.class, (result, event) -> {
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

    private static InputStream loadFont(String name){
        return RockBottomAPI.getGame().getAssetManager().getResourceStream("/assets/creativebottom/fonts/" + name);
    }

    private static void addFont(String fontName, String fileName){
        try {
            FONTS.put(fontName, Font.fromStream(loadFont(fileName + ".png"), loadFont(fileName + ".info"), fontName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
