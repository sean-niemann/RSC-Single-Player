package org.nemotech.rsc.event;

import org.nemotech.rsc.external.location.NPCLoc;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.landscape.Path;

public abstract class WalkMobToMobEvent extends DelayedEvent {

    protected Mob affectedMob;
    private NPCLoc loc = null;
    protected Mob owner;
    private int radius;
    private long startTime = 0L;

    public WalkMobToMobEvent(Mob owner, Mob affectedMob, int radius) {
        super(null, 500);

        if (owner.isRemoved()) {
            super.running = false;
            return;
        }

        if (owner instanceof NPC) {
            NPC npc = (NPC) owner;
            loc = npc.getLoc();


            if (affectedMob.getX() < (loc.getMinX() - 4) || affectedMob.getX() > (loc.getMaxX() + 4) || affectedMob.getY() < (loc.getMinY() - 4) || affectedMob.getY() > (loc.getMaxY() + 4)) {
                super.running = false;
                return;
            }
        }

        this.owner = owner;
        owner.setPath(new Path(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()));

        this.affectedMob = affectedMob;
        this.radius = radius;

        if (owner.withinRange(affectedMob, radius)) {
            arrived();
            super.running = false;
            return;
        }

        startTime = System.currentTimeMillis();
    }

    public abstract void arrived();

    public void failed() {
    }

    public Mob getAffectedMob() {
        return affectedMob;
    }

    public final void run() {
        if (owner.isRemoved()) {
            super.running = false;
            return;
        }

        if (owner.withinRange(affectedMob, radius))
            arrived();
        else if (owner.hasMoved())
            return; // We're still moving
        else {
            if (System.currentTimeMillis() - startTime <= 10000) { // Make NPCs give a 10 second chase
                if (loc != null) {
                    if (affectedMob.getX() < (loc.getMinX() - 4) || affectedMob.getX() > (loc.getMaxX() + 4) || affectedMob.getY() < (loc.getMinY() - 4) || affectedMob.getY() > (loc.getMaxY() + 4)) {
                        super.running = false;
                        failed();
                        return;
                    }
                }

                if (owner.isBusy())
                    return;

                owner.setPath(new Path(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()));
                return;
            } else
                failed();
        }
        super.running = false;
    }

}