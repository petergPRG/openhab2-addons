/**
 * Copyright (c) 2010-2019 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.elerotransmitterstick.internal.handler;

import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.BridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.elerotransmitterstick.internal.config.EleroTransmitterStickConfig;
import org.openhab.binding.elerotransmitterstick.internal.stick.TransmitterStick;
import org.openhab.binding.elerotransmitterstick.internal.stick.TransmitterStick.StickListener;

/**
 * The {@link EleroTransmitterStickHandler} is responsible for managing the connection to an elero transmitter stick.
 *
 * @author Volker Bier - Initial contribution
 */
public class EleroTransmitterStickHandler extends BaseBridgeHandler implements BridgeHandler {
    private TransmitterStick stick;

    public EleroTransmitterStickHandler(Bridge bridge) {
        super(bridge);

        stick = new TransmitterStick(new StickListener() {
            @Override
            public void connectionEstablished() {
                updateStatus(ThingStatus.ONLINE);
            }

            @Override
            public void connectionDropped(Exception e) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, e.getMessage());
            }
        });
    }

    @Override
    public void handleCommand(ChannelUID channelUid, Command command) {
    }

    @Override
    public void dispose() {
        stick.dispose();

        super.dispose();
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);

        stick.initialize(getConfig().as(EleroTransmitterStickConfig.class), scheduler);
    }

    public TransmitterStick getStick() {
        return stick;
    }

    public void addStatusListener(int channelId, StatusListener listener) {
        stick.addStatusListener(channelId, listener);
    }

    public void removeStatusListener(int channelId, StatusListener listener) {
        stick.removeStatusListener(channelId, listener);
    }
}
