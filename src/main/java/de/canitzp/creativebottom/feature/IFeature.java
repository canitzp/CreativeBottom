package de.canitzp.creativebottom.feature;

import de.canitzp.creativebottom.CreativeBottom;
import de.ellpeck.rockbottom.api.IApiHandler;
import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.event.IEventHandler;

/**
 * @author canitzp
 */
public interface IFeature {

    void init(IGameInstance game, IApiHandler api, IEventHandler handler, CreativeBottom cb);

}
