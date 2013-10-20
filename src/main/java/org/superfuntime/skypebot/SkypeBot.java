/*
 * An easily extendable chat bot for any chat service.
 * Copyright (C) 2013 bogeymanEST
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.superfuntime.skypebot;

import com.skype.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.superfuntime.chatty.Bot;

/**
 * User: Bogeyman
 * Date: 1.10.13
 * Time: 18:03
 */
public class SkypeBot extends Bot implements ChatMessageListener {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void start() {
        try {
            if (!Skype.isRunning()) {
                throw new RuntimeException("Skype is not running");
            }
            final String userId = Skype.getProfile().getId();
            if (!userId.equals(getSettingsNode().getString("username"))) {
                throw new RuntimeException("Skype user not found! Found: " + userId);
            }
            SkypeClient.setSilentMode(true);
            Skype.setDaemon(false); //Prevent closing the program
            Skype.addChatMessageListener(this);
            Skype.getProfile().setFullName(getSettingsNode().getString("name"));
            logger.info("Hooked into Skype as " + userId);
        } catch (SkypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        logger.info("Stopped Skype bot");
    }

    @Override
    public void setName(String name) {
        try {
            Skype.getProfile().setFullName(name);
        } catch (SkypeException e) {
            e.printStackTrace();
        }
    }

    private org.superfuntime.chatty.chat.ChatMessage getChatMessage(ChatMessage msg) throws SkypeException {
        org.superfuntime.chatty.chat.ChatMessage.Type type = org.superfuntime.chatty.chat.ChatMessage.Type.OTHER;
        if (msg.getType() == ChatMessage.Type.SAID)
            type = org.superfuntime.chatty.chat.ChatMessage.Type.SAID;
        else if (msg.getType() == ChatMessage.Type.EMOTED)
            type = org.superfuntime.chatty.chat.ChatMessage.Type.EMOTED;
        return new org.superfuntime.chatty.chat.ChatMessage(msg.getContent(),
                                                            new SkypeUser(msg.getSender()),
                                                            new SkypeChat(msg.getChat()),
                                                            msg.getTime(), type);
    }

    @Override
    public void chatMessageReceived(ChatMessage msg) throws SkypeException {
        if (msg.getStatus() != ChatMessage.Status.RECEIVED) return;
        onChatMessageReceived(getChatMessage(msg));
    }

    @Override
    public void chatMessageSent(ChatMessage msg) throws SkypeException {
        if (msg.getStatus() != ChatMessage.Status.SENT) return;
        onChatMessageSent(getChatMessage(msg));
    }
}
