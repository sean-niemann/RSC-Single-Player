package org.nemotech.rsc.client.update.impl;

import org.nemotech.rsc.model.ChatMessage;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.client.Mob;
import org.nemotech.rsc.client.update.Updater;
import org.nemotech.rsc.util.StatefulEntityCollection;
import org.nemotech.rsc.external.EntityManager;

import java.util.Collection;
import java.util.List;

public class NPCUpdater extends Updater {
    
    public boolean sectionLoaded = false;
    
    private Mob getLastNpc(int serverIndex) {
        for (int i1 = 0; i1 < mc.npcCacheCount; i1++) {
            if (mc.npcsCache[i1].serverIndex == serverIndex) {
                return mc.npcsCache[i1];
            }
        }
        return null;
    }
    
    @Override
    public void handlePositionUpdate(Player player) {
        StatefulEntityCollection<NPC> watchedNpcs = player.getWatchedNpcs();
        Collection<NPC> newNpcs = watchedNpcs.getNewEntities();
        Collection<NPC> knownNpcs = watchedNpcs.getKnownEntities();
        
        mc.npcCacheCount = mc.npcCount;
        mc.npcCount = 0;
        System.arraycopy(mc.npcs, 0, mc.npcsCache, 0, mc.npcCacheCount);
        
        for (NPC npc : knownNpcs) {
            //if(!sectionLoaded) return;
            Mob character = getLastNpc(npc.getIndex());
            if (npc.hasMoved()) {
                int sprite = npc.getSprite();
                int wayCur = character.waypointCurrent;
                int wayX = character.waypointsX[wayCur];
                int wayY = character.waypointsY[wayCur];
                if (sprite == 2 || sprite == 1 || sprite == 3)
                    wayX += mc.magicLoc;
                if (sprite == 6 || sprite == 5 || sprite == 7)
                    wayX -= mc.magicLoc;
                if (sprite == 4 || sprite == 3 || sprite == 5)
                    wayY += mc.magicLoc;
                if (sprite == 0 || sprite == 1 || sprite == 7)
                    wayY -= mc.magicLoc;
                character.animationNext = sprite;
                character.waypointCurrent = wayCur = (wayCur + 1) % 10;
                character.waypointsX[wayCur] = wayX;
                character.waypointsY[wayCur] = wayY;
            } else if (npc.spriteChanged()) {
                int nextSpriteOffset = npc.getSprite();
                if ((nextSpriteOffset & 0xc) == 12) {
                    continue;
                }
                character.animationNext = nextSpriteOffset;
            }
            mc.npcs[mc.npcCount++] = character;
        }
        
        for (NPC n : newNpcs) {
            int index = n.getIndex();
            int x = (n.getX() - mc.regionX) * mc.magicLoc + 64;
            int y = (n.getY() - mc.regionY) * mc.magicLoc + 64;
            int sprite = n.getSprite();
            int id = n.getID();
            if(x > 0 && y > 0) {
                mc.addNpc(index, x, y, sprite, id);
            }
            sectionLoaded = true;
        }
    }
    
    @Override
    public void handleGraphicsUpdate(Player player) {
        List<NPC> npcsNeedingHitsUpdate = player.getNpcsRequiringHitsUpdate();
        List<ChatMessage> npcMessagesNeedingDisplayed = player.getNpcMessagesNeedingDisplayed();

        int updateSize = npcMessagesNeedingDisplayed.size() + npcsNeedingHitsUpdate.size();
        if(updateSize > 0) {
            for (ChatMessage cm : npcMessagesNeedingDisplayed) {
                Mob characterNPC = mc.npcsServer[cm.getSender().getIndex()];
                characterNPC.messageTimeout = 150;
                characterNPC.message = cm.getMessage();
                mc.showMessage("@yel@" + EntityManager.getNPC(characterNPC.npcId).getName() + ": " + characterNPC.message, 5);
            }
            for (NPC n : npcsNeedingHitsUpdate) {
                Mob characterNPC = mc.npcsServer[n.getIndex()];
                characterNPC.damageTaken = n.getLastDamage();
                characterNPC.healthCurrent = n.getHits();
                characterNPC.healthMax = n.getDef().getHitpoints();
                characterNPC.combatTimer = 200;
            }
        }
    }
    
}