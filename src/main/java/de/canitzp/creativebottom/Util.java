package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.Tile;
import de.ellpeck.rockbottom.api.world.layer.TileLayer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author canitzp
 */
public class Util {

    public static <T> boolean doesArrayContains(T[] ary, T obj){
        for(T inst : ary){
            if(obj.equals(inst)){
                return true;
            }
        }
        return false;
    }

    public static boolean doesArrayContainsString(String[] ary, String str){
        for(String s : ary){
            if(s.contains(str)){
                return true;
            }
        }
        return false;
    }

    public static Field getField(Class c, String fieldName) throws NoSuchFieldException {
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Item getTileUnderMouse(AbstractEntityPlayer player){
        for(TileLayer layer : TileLayer.getLayersByInteractionPrio()){
            int x = de.ellpeck.rockbottom.api.util.Util.floor(RockBottomAPI.getGame().getRenderer().getMousedTileX());
            int y = de.ellpeck.rockbottom.api.util.Util.floor(RockBottomAPI.getGame().getRenderer().getMousedTileY());
            Tile tile = player.world.getState(layer, x, y).getTile();
            if(tile != null){
                Item item = tile.getItem();
                if(item == null){
                    List<ItemInstance> drops = tile.getDrops(player.world, x, y, layer, player);
                    if(!drops.isEmpty()){
                        item = drops.get(0).getItem();
                    }
                }
                return item;
            }
        }
        return null;
    }

    public static int getContainerGuiWidth(ItemContainer container) {
        if(container != null){
            int mostX = 0;
            for(int i = 0; i < container.getSlotAmount(); i++){
                ContainerSlot slot = container.getSlot(i);
                mostX = Math.max(mostX, slot.x);
            }
            return mostX + 16;
        }
        return 0;
    }

    public static int getContainerGuiHeight(ItemContainer container) {
        if(container != null){
            int mostY = 0;
            for(int i = 0; i < container.getSlotAmount(); i++){
                ContainerSlot slot = container.getSlot(i);
                mostY = Math.max(mostY, slot.y);
            }
            return mostY + 16;
        }
        return 0;
    }

}
