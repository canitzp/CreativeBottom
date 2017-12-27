package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.item.ItemMeta;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author canitzp
 */
public class ContainerCreative extends ItemContainer{

    public ContainerCreative(AbstractEntityPlayer player) {
        super(player, player.getInv(), new CreativeInventory());
        this.addSlotGrid(player.getInv(), 0, 8, 0, -40, 8);
        this.addSlotGrid(this.containedInventories[1], 0, this.containedInventories[1].getSlotAmount(), -60, -15, 14);
    }

    @Override
    public IResourceName getName() {
        return RockBottomAPI.createRes(CreativeBottom.INSTANCE, "creative");
    }

    public static class CreativeInventory implements IInventory {
        List<ItemInstance> items = new ArrayList<>();

        public CreativeInventory(){
            for(Item item : RockBottomAPI.ITEM_REGISTRY.getUnmodifiable().values()){
                if(item instanceof ItemMeta){
                    for(int i = 0; i <= item.getHighestPossibleMeta(); i++){
                        this.items.add(new ItemInstance(item, 1, i));
                    }
                } else {
                    this.items.add(new ItemInstance(item));
                }
            }
        }

        @Override
        public void set(int id, ItemInstance instance) {}

        @Override
        public ItemInstance add(int id, int amount) {
            return this.items.get(id).setAmount(amount);
        }

        @Override
        public ItemInstance remove(int id, int amount) {
            return null;
        }

        @Override
        public ItemInstance get(int id) {
            return this.items.size() > id ? add(id, RockBottomAPI.getGame().getInput().isKeyDown(Keyboard.KEY_LCONTROL) ? this.items.get(id).getMaxAmount() : 1) : null;
        }

        @Override
        public int getSlotAmount() {
            return this.items.size() + 1;
        }

        @Override
        public void notifyChange(int slot) {
        }

        @Override
        public void addChangeCallback(BiConsumer<IInventory, Integer> callback) {

        }

        @Override
        public void removeChangeCallback(BiConsumer<IInventory, Integer> callback) {

        }

        @Override
        public ItemInstance addToSlot(int slot, ItemInstance instance, boolean simulate) {
            return instance;
        }

    }
}