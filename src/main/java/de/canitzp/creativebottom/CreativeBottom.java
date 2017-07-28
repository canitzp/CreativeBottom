package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.font.Font;
import de.ellpeck.rockbottom.api.event.IEventHandler;
import de.ellpeck.rockbottom.api.mod.IMod;

import java.io.InputStream;
import java.util.HashMap;
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
        return "a.6.0";
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
        Events.init(eventHandler);
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
