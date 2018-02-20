package de.canitzp.creativebottom;

import de.canitzp.creativebottom.feature.*;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.data.settings.Keybind;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.mod.IMod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author canitzp
 */
public class CreativeBottom implements IMod {

    public static CreativeBottom INSTANCE;
    private static List<IFeature> features = new ArrayList<>();

    public static Keybind creativeState;
    public static Keybind flyingState;
    public static Keybind ghostState;
    public static Keybind lightLevelState;
    public static Keybind controlKey;
    public static Keybind upFly;
    public static Keybind downFly;
    public static Keybind middleMouse;

    public CreativeBottom(){
        INSTANCE = this;

        creativeState = new Keybind(RockBottomAPI.createRes(this, "change_creative_state_key"), GLFW.GLFW_KEY_M, false).register();
        flyingState = new Keybind(RockBottomAPI.createRes(this, "change_flying_state_key"), GLFW.GLFW_KEY_LEFT_ALT, false).register();
        ghostState = new Keybind(RockBottomAPI.createRes(this, "change_ghost_mode_state_key"), GLFW.GLFW_KEY_T, false).register();
        lightLevelState = new Keybind(RockBottomAPI.createRes(this, "change_light_level_state_key"), GLFW.GLFW_KEY_L, false).register();
        controlKey = new Keybind(RockBottomAPI.createRes(this, "control_key"), GLFW.GLFW_KEY_LEFT_CONTROL, false).register();
        upFly = new Keybind(RockBottomAPI.createRes(this, "fly_up_key"), GLFW.GLFW_KEY_W, false).register();
        downFly = new Keybind(RockBottomAPI.createRes(this, "fly_down_key"), GLFW.GLFW_KEY_S, false).register();
        middleMouse = new Keybind(RockBottomAPI.createRes(this, "middle_mouse_click"), GLFW.GLFW_MOUSE_BUTTON_MIDDLE, true).register();
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
        return "a.10.0";
    }

    @Override
    public String getResourceLocation() {
        return "assets/creativebottom";
    }

    @Override
    public String getDescription() {
        return "This mod should provide a simple creative game mode, a bit like the minecraft one for Rock Bottom";
    }

    @Override
    public void preInit(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {

    }

    @Override
    public void init(IGameInstance game, IApiHandler apiHandler, IEventHandler eventHandler) {
        if(!game.isDedicatedServer()){
            features.add(new FeatureCreativeBottom());
            features.add(new FeatureWorld());
            features.add(new FeaturePlayer());
            features.add(new FeatureRender());
            features.add(new FeatureDisplay());
            features.forEach(iFeature -> iFeature.init(game, apiHandler, eventHandler, this));
        } else {
            RockBottomAPI.logger().log(Level.SEVERE, "CreativeBottom can't be used on a dedicated server and disabled itself!");
        }

    }

}
