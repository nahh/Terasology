/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.nui.mainMenu;

import org.terasology.config.Config;
import org.terasology.engine.GameEngine;
import org.terasology.engine.TerasologyConstants;
import org.terasology.engine.modes.StateLoading;
import org.terasology.entitySystem.systems.In;
import org.terasology.network.JoinStatus;
import org.terasology.network.NetworkSystem;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.UIScreenLayer;
import org.terasology.rendering.nui.UIScreenLayerUtil;
import org.terasology.rendering.nui.baseWidgets.ButtonEventListener;
import org.terasology.rendering.nui.baseWidgets.UIButton;
import org.terasology.rendering.nui.baseWidgets.UIText;

/**
 * @author Immortius
 */
public class JoinServerPopup extends UIScreenLayer {

    @In
    private Config config;

    @In
    private NUIManager nuiManager;

    @In
    private NetworkSystem networkSystem;

    @In
    private GameEngine engine;

    @Override
    public void initialise() {
        UIScreenLayerUtil.trySubscribe(this, "join", new ButtonEventListener() {
            @Override
            public void onButtonActivated(UIButton button) {
                nuiManager.popScreen();
                UIText address = find("address", UIText.class);
                JoinStatus status = networkSystem.join(address.getText(), TerasologyConstants.DEFAULT_PORT);
                if (status.getStatus() != JoinStatus.Status.FAILED) {
                    engine.changeState(new StateLoading(status));
                } else {
                    nuiManager.pushScreen("engine:errorMessagePopup", ErrorMessagePopup.class)
                            .setError("Failed to Join", "Could not connect to server - " + status.getErrorMessage());
                }
            }
        });

        UIScreenLayerUtil.trySubscribe(this, "cancel", new ButtonEventListener() {
            @Override
            public void onButtonActivated(UIButton button) {
                nuiManager.popScreen();
            }
        });
    }

    @Override
    public boolean isLowerLayerVisible() {
        return true;
    }
}
