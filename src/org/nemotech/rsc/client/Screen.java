package org.nemotech.rsc.client;

import java.awt.Component;

public class Screen extends Surface {

    public mudclient mc;

    public Screen(int width, int height, int k, Component component) {
        super(width, height, k, component);
    }

    @Override
    public void spriteClipping(int x, int y, int w, int h, int id, int tx, int ty) {
        if (id >= 50000) {
            mc.drawTeleportBubble(x, y, w, h, id - 50000, tx, ty);
            return;
        }
        if (id >= 40000) {
            mc.drawItem(x, y, w, h, id - 40000, tx, ty);
            return;
        }
        if (id >= 20000) {
            mc.drawNpc(x, y, w, h, id - 20000, tx, ty);
            return;
        }
        if (id >= 5000) {
            mc.drawPlayer(x, y, w, h, id - 5000, tx, ty);
            return;
        }
        super.spriteClipping(x, y, w, h, id);
    }
    
}