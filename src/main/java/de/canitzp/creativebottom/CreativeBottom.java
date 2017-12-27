package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.font.IFont;
import de.ellpeck.rockbottom.api.data.settings.Keybind;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.mod.IMod;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;
import org.lwjgl.input.Keyboard;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author canitzp
 */
public class CreativeBottom implements IMod {

    public static CreativeBottom INSTANCE;

    public static Keybind creativeState;
    public static Keybind flyingState;
    public static Keybind ghostState;

    public CreativeBottom(){
        INSTANCE = this;

        creativeState = new Keybind(RockBottomAPI.createRes(this, "change_creative_state_key"), Keyboard.KEY_C, false).register();
        flyingState = new Keybind(RockBottomAPI.createRes(this, "change_flying_state_key"), Keyboard.KEY_LMENU, false).register();
        ghostState = new Keybind(RockBottomAPI.createRes(this, "change_ghost_mode_state_key"), Keyboard.KEY_T, false).register();
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
        return "a.8.0";
    }

    @Override
    public String getResourceLocation() {
        return "/assets/creativebottom";
    }

    @Override
    public String getDescription() {
        return "This mod should provide a simple creative game mode, a bit like the minecraft one for Rock Bottom";
    }

    @Override
    public void preInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {}

    @Override
    public void init(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {
        Events.init(eventHandler);
    }

}
