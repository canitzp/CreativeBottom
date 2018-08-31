package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.*;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.event.EventResult;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.event.impl.ContainerOpenEvent;
import de.ellpeck.rockbottom.api.event.impl.GuiInitEvent;
import de.ellpeck.rockbottom.api.gui.GuiContainer;
import de.ellpeck.rockbottom.api.gui.component.*;
import de.ellpeck.rockbottom.api.gui.container.ContainerSlot;
import de.ellpeck.rockbottom.api.gui.container.ItemContainer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.util.BoundBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author canitzp
 */
public class FeatureDisplay implements IFeature {

    private final ContainerCreative.CreativeInventory inventory = new ContainerCreative.CreativeInventory();
    public static List<ContainerSlot> slotsAdded = new ArrayList<>();

    @Override
    public void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb) {
        handler.registerListener(GuiInitEvent.class, (result, event) -> {
            if(event.gui instanceof GuiContainer){
                GuiContainer gui = (GuiContainer) event.gui;
                int x = gui.getWidth() + 2;
                int y = (int) -((game.getRenderer().getHeightInGui() - event.gui.getHeight()) / 2) + 5;
                int height = (int) (game.getRenderer().getHeightInGui() - 10);
                int spaceWidth = (int) (((game.getRenderer().getWidthInGui() - event.gui.getWidth()) / 2) - 16) / 16;
                if(spaceWidth > 0){
                    BoundBox boundBox = new BoundBox(gui.getWidth() + gui.getX(), 5, game.getRenderer().getWidthInGui(), game.getRenderer().getHeightInGui());
                    ComponentMenu menu = new ComponentMenu(gui, x, y, height, spaceWidth, Math.round(height / 18.0F), boundBox);
                    int slotStart = game.getPlayer().getContainer().getSlotAmount();
                    for(int i = slotStart; i < inventory.getSlotAmount() + slotStart - 1; i++){
                        int finalI = i - slotStart;
                        ContainerSlot slot = new ContainerSlot(inventory, i, 0, 0){
                            @Override
                            public ItemInstance get(){
                                ItemInstance item = inventory.get(finalI);
                                if(CreativeBottom.controlKey.isDown()){
                                    item.setAmount(item.getMaxAmount());
                                }
                                return item;
                            }
                        };
                        game.getPlayer().getContainer().addSlot(slot);
                        slotsAdded.add(slot);
                        menu.add(new MenuComponent(16, 16).add(0, 5, new ComponentSlot(gui, slot, i, 0, 0)));
                    }
                    menu.add(new MenuComponent(16, 16).add(0, 5, new ComponentSlot(gui, new ContainerSlot(inventory, inventory.getSlotAmount() + slotStart, 0, 0), inventory.getSlotAmount(), 0, 0){
                        @Override
                        public boolean onMouseAction(IGameInstance game, int button, float x, float y) {
                            if(isMouseOver(game) && CreativeBottom.controlKey.isDown()){
                                IInventory inv = game.getPlayer().getInv();
                                for(int i = 0; i < inv.getSlotAmount(); i++){
                                    inv.set(i, null);
                                }
                                return true;
                            }
                            game.getPlayer().getContainer().holdingInst = null;
                            return true;
                        }
                    }));
                    menu.organize();
                    gui.getComponents().add(menu);
                }
            }
            return EventResult.DEFAULT;
        });
    }

}
