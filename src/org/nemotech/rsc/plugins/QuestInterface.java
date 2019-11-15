package org.nemotech.rsc.plugins;

import org.nemotech.rsc.model.player.Player;

public interface QuestInterface {

    public int getQuestID();

    public String getQuestName();

    public boolean isMembers();

    public void handleReward(Player player);

}