package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.Registries;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.item.ItemMeta;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author canitzp
 */
public class ContainerCreative extends ItemContainer{

    public ContainerCreative(AbstractEntityPlayer player) {
        super(player);
    }

    @Override
    public ResourceName getName() {
        return new ResourceName(CreativeBottom.INSTANCE, "creative");
    }

    public static class CreativeInventory implements IInventory {
        List<ItemInstance> items = new ArrayList<>();

        public CreativeInventory(){
            for(Item item : Registries.ITEM_REGISTRY.getUnmodifiable().values()){
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
        public void set(int id, ItemInstance instance) {
        }

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
            return this.items.size() > id ? add(id, 1) : null;
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
    
        @Override
        public boolean containsResource(IUseInfo info){
            return false;
        }
    
        @Override
        public boolean containsItem(ItemInstance inst){
            return false;
        }
    
        @Override
        public int getItemIndex(ItemInstance inst){
            return 0;
        }
    
        @Override
        public ItemInstance add(ItemInstance instance, boolean simulate){
            return null;
        }
    
        @Override
        public ItemInstance addExistingFirst(ItemInstance instance, boolean simulate){
            return null;
        }
    
        @Override
        public void fillRandomly(Random random, List<ItemInstance> items){
        
        }
    
    }
}