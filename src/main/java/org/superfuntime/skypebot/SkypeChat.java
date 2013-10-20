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

import com.skype.SkypeException;
import org.superfuntime.chatty.chat.Chat;

/**
 * User: Bogeyman
 * Date: 2.10.13
 * Time: 19:11
 */
public class SkypeChat extends Chat {
    private final com.skype.Chat skypeChat;

    public SkypeChat(com.skype.Chat skypeChat) {
        this.skypeChat = skypeChat;
    }

    @Override
    public void send(String message) {
        try {
            skypeChat.send(message);
        } catch (SkypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String... lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (i != 0) {
                sb.append("\n");
            }
            sb.append(line);
        }
        send(sb.toString());
    }

    @Override
    public String getName() {
        try {
            return skypeChat.getWindowTitle();
        } catch (SkypeException e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    @Override
    public Type getType() {
        try {
            switch (skypeChat.getStatus()) {
                case DIALOG:
                    return Type.PM;
                case MULTI_SUBSCRIBED:
                    return Type.GROUP;
            }
        } catch (SkypeException e) {
            e.printStackTrace();
        }
        return Type.OTHER;
    }

    @Override
    public String getChatProtocol() {
        return "skype";
    }
}
