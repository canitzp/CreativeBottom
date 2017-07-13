package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.data.IDataManager;
import de.ellpeck.rockbottom.api.data.settings.IPropSettings;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author canitzp
 */
public class CreativeSettings implements IPropSettings {

    public Map<String, String> stringsToSave = new HashMap<>();

    @Override
    public void load(Properties props) {
        for(String key : props.stringPropertyNames()){
            this.stringsToSave.put(key, props.getProperty(key));
        }
    }

    @Override
    public void save(Properties props) {
        for(Map.Entry<String, String> entry : stringsToSave.entrySet()){
            props.setProperty(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public File getFile(IDataManager manager) {
        return new File(manager.getGameDir(), "mods" + File.separator + "config" + File.separator + "creative_bottom.properties");
    }

    @Override
    public String getName() {
        return "Creative Bottom Settings";
    }

}
