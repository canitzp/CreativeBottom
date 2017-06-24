package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInvChangeCallback;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class ContainerCreative extends ItemContainer{

    public ContainerCreative(AbstractEntityPlayer player) {
        super(player, player.getInv(), new CreativeInventory());
        this.addSlotGrid(player.getInv(), 0, 8, 0, -40, 8);
        this.addSlotGrid(this.containedInventories[1], 0, this.containedInventories[1].getSlotAmount(), -60, -15, 14);
    }

    public static class CreativeInventory implements IInventory {
        List<Item> items = new ArrayList<>(RockBottomAPI.ITEM_REGISTRY.getUnmodifiable().values());

        public void set(int id, ItemInstance instance) { }

        public ItemInstance add(int id, int amount) {
            return new ItemInstance(this.items.get(id), amount);
        }

        public ItemInstance remove(int id, int amount) {
            return null;
        }

        public ItemInstance get(int id) {
            return this.items.size() > id ? new ItemInstance(this.items.get(id), RockBottomAPI.getGame().getContainer().getInput().isKeyDown(Keyboard.KEY_LSHIFT) ? this.items.get(id).getMaxAmount() : 1) : null;
        }

        public int getSlotAmount() {
            return RockBottomAPI.ITEM_REGISTRY.getSize() + 1;
        }

        public void notifyChange(int slot) {
        }

        public void addChangeCallback(IInvChangeCallback callback) {
        }

        public void removeChangeCallback(IInvChangeCallback callback) {
        }
    }
}