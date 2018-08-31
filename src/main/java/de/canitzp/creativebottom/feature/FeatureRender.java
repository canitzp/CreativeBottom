package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.CreativeBottom;
import de.canitzp.creativebottom.DataCheck;
import de.canitzp.creativebottom.Util;
import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.assets.font.FormattingCode;
import de.ellpeck.rockbottom.api.assets.font.IFont;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.ComponentRenderEvent;
import de.ellpeck.rockbottom.api.event.impl.OverlayRenderEvent;
import de.ellpeck.rockbottom.api.event.impl.WorldRenderEvent;
import de.ellpeck.rockbottom.api.gui.component.ComponentClickableText;
import de.ellpeck.rockbottom.api.gui.component.GuiComponent;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.world.IWorld;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.lang.reflect.Field;

/**
 * @author canitzp
 */
public class FeatureRender implements IFeature{

    private String[] cachedMainMenuString;

    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb) {
        handler.registerListener(ComponentRenderEvent.class, (result, event) -> {
            if(event.gui != null){
                if(event.gui.getName().toString().equals("rockbottom/main_menu")){ // Adding additional text to the MainMenu
                    GuiComponent component = event.component;
                    if(component instanceof ComponentClickableText && Util.doesArrayContainsString(((ComponentClickableText) component).getText(), " - API")){
                        if(cachedMainMenuString == null){
                            cachedMainMenuString = new String[2];
                            cachedMainMenuString[0] = ((ComponentClickableText) component).getText()[0];
                            cachedMainMenuString[1] = "w/ Creative Bottom " + cb.getVersion();
                        }
                        try {
                            Field field = Util.getField(ComponentClickableText.class, "text");
                            field.set(component, cachedMainMenuString);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        component.setPos(component.getX(), event.gui.getHeight() - 13);
                    }
                    return EventResult.MODIFIED;
                }
            } else {
                if(event.id == 8){ // Prevent the heart bar from rendering if the player has creative mode active
                    if (DataCheck.from(game.getPlayer()).isCreative()) {
                        event.component.setActive(false);
                        return EventResult.CANCELLED;
                    } else {
                        event.component.setActive(true);
                    }
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(OverlayRenderEvent.class, (result, event) -> {
            if (event.player != null) {
                DataCheck data = DataCheck.from(event.player);
                if (data.isCreative()) {
                    IFont font = event.assetManager.getFont();
                    font.drawString(0.0F, 00.0F, FormattingCode.ORANGE.toString() + "Creative Bottom (" + cb.getVersion() + ")", 0.175F);
                    font.drawString(0.0F, 03.6F, String.format(" Flying     (%s): %s", CreativeBottom.flyingState.getDisplayName(), data.isFlying() ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 07.2F, String.format(" Ghost      (%s): %s", CreativeBottom.ghostState.getDisplayName(), data.isGhost() ? "ON" : "OFF"), 0.175F);
                    font.drawString(0.0F, 10.8F, String.format(" Light-Level(%s): %s", CreativeBottom.lightLevelState.getDisplayName(), data.doesShowLightLevel() ? "SHOWN" : "HIDDEN"), 0.175F);
                }
            }
            return EventResult.DEFAULT;
        });
        handler.registerListener(WorldRenderEvent.class, (result, event) -> {
            if (event.player != null) {
                AbstractEntityPlayer player = event.player;
                IWorld world = player.world;
                if (world != null && DataCheck.from(player).doesShowLightLevel()) {
                    IFont font = event.assetManager.getFont();
                    for(int x = -10; x < 10; x++){
                        int realX = (int)player.getX() + x;
                        for(int y = -10; y < 10; y++){
                            int realY = (int)player.getY() + y;
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
    }

}
