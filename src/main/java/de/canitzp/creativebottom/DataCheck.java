package de.canitzp.creativebottom;

import de.ellpeck.rockbottom.api.data.set.DataSet;
import de.ellpeck.rockbottom.api.data.set.ModBasedDataSet;
import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

/**
 * @author canitzp
 */
public class DataCheck {

    public static final ResourceName IS_CREATIVE_KEY = new ResourceName(CreativeBottom.INSTANCE, "is_creative");
    public static final ResourceName FLY_MODE_KEY = new ResourceName(CreativeBottom.INSTANCE, "flight_mode");
    public static final ResourceName LIGHT_LEVEL_KEY = new ResourceName(CreativeBottom.INSTANCE, "light_level");
    public static final ResourceName GHOST_MODE_KEY = new ResourceName(CreativeBottom.INSTANCE, "ghost_mode");

    private ModBasedDataSet data;

    public DataCheck(Entity entity){
        this.data = entity.getAdditionalData();
    }

    public static DataCheck from(Entity entity){
        return new DataCheck(entity);
    }

    public static DataCheck fromOrCreate(Entity entity){
        if(entity.getAdditionalData() == null){
            entity.setAdditionalData(new ModBasedDataSet());
        }
        return from(entity);
    }

    public boolean isValid(){
        return this.data != null;
    }

    public void set(ResourceName key, boolean value){
        if(this.isValid()){
            this.data.addBoolean(key, value);
        }
    }

    public boolean isCreative(){
        return isValid() && data.getBoolean(IS_CREATIVE_KEY);
    }

    public void setCreative(boolean set){
        this.set(IS_CREATIVE_KEY, set);
    }

    public boolean isFlying(){
        return this.isCreative() && data.getBoolean(FLY_MODE_KEY);
    }

    public void setFlying(boolean set){
        this.set(FLY_MODE_KEY, set);
    }

    public boolean doesShowLightLevel(){
        return this.isCreative() && data.getBoolean(LIGHT_LEVEL_KEY);
    }

    public void setShowLightLevel(boolean set){
        this.set(LIGHT_LEVEL_KEY, set);
    }

    public boolean isGhost(){
        return this.isCreative() && data.getBoolean(GHOST_MODE_KEY);
    }

    public void setGhost(boolean set){
        this.set(GHOST_MODE_KEY, set);
    }

}
