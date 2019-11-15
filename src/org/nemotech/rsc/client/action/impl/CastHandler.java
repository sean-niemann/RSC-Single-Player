package org.nemotech.rsc.client.action.impl;

import org.nemotech.rsc.client.sound.SoundEffect;
import org.nemotech.rsc.client.action.ActionHandler;
import org.nemotech.rsc.event.WalkMobToMobEvent;
import org.nemotech.rsc.event.WalkToMobEvent;
import org.nemotech.rsc.event.impl.FightEvent;
import org.nemotech.rsc.event.impl.RemoveObjectEvent;
import org.nemotech.rsc.external.definition.extra.ItemSmeltingDef;
import org.nemotech.rsc.external.definition.extra.ReqOreDef;
import org.nemotech.rsc.model.GameObject;
import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.Mob;
import org.nemotech.rsc.model.NPC;
import org.nemotech.rsc.model.Projectile;
import org.nemotech.rsc.model.World;
import org.nemotech.rsc.model.landscape.PathGenerator;
import org.nemotech.rsc.model.player.Player;
import org.nemotech.rsc.model.player.states.Action;
import org.nemotech.rsc.util.Formulae;
import org.nemotech.rsc.external.EntityManager;
import org.nemotech.rsc.external.definition.SpellDef;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.plugins.PluginManager;
import org.nemotech.rsc.event.WalkToPointEvent;
import org.nemotech.rsc.model.Item;
import org.nemotech.rsc.plugins.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class CastHandler implements ActionHandler {

    public void handleCastOnNpc(int npcIndex, int spellID) {
        Player player = World.getWorld().getPlayer();
        SpellDef spell = EntityManager.getSpell(spellID);
        if(!checkRequirements(player, spell)) {
            return;
        }
        if(spell.getType() == 2) {
            NPC affectedNpc = World.getWorld().getNpc(npcIndex);
            if(affectedNpc == null) { // this shouldn't happen
                player.resetPath();
                return;
            }
            if(affectedNpc.getID() == 35) {
                player.message("Delrith can not be attacked without the Silverlight sword");
                return;
            }

            if(affectedNpc.getID() == 364 && (player.getQuestStage(Plugin.TEMPLE_OF_IKOV) == -1
                    || player.getQuestStage(Plugin.TEMPLE_OF_IKOV) == -2)) {
                player.message("You have already completed this quest");
                return;
            }
            if(affectedNpc.getID() == 364 && !player.getInventory().wielding(726)) {
                player.message("You decide you don't want to attack Lucien since he is your friend");
                return;
            }

            if(player.withinRange(affectedNpc, 4)) {
                player.resetPath();
            }
            if(affectedNpc.getID() == 315) {
                /* FAMILY CREST CHRONOZ */
                if(spell.getName().contains("blast")) {
                    String elementalType = spell.getName().split(" ")[0].toLowerCase();
                    player.message("chronozon weakens");
                    if(!player.getAttribute("chronoz_" + elementalType, false)) {
                        player.setAttribute("chronoz_" + elementalType, true);
                    }
                }
            }

            if(PluginManager.getInstance().blockDefaultAction("PlayerMageNpc", new Object[]{player, affectedNpc, spellID})) {
                return;
            }

            if(!new PathGenerator(player.getX(), player.getY(), affectedNpc.getX(), affectedNpc.getY()).isValid()) {
                player.getSender().sendMessage("I can't get a clear shot from here");
                player.resetPath();
                return;
            }
            player.setFollowing(affectedNpc);
            player.setStatus(Action.CASTING_MOB);
            World.getWorld().getDelayedEventHandler().add(new WalkToMobEvent(player, affectedNpc, 5) {
                @Override
                public void arrived() {
                    if(!new PathGenerator(owner.getX(), owner.getY(), affectedMob.getX(), affectedMob.getY()).isValid()) {
                        owner.getSender().sendMessage("I can't get a clear shot from here");
                        owner.resetPath();
                        return;
                    }
                    player.resetFollowing();
                    owner.resetPath();
                    SpellDef spell = EntityManager.getSpell(spellID);
                    if(!canCast(owner) || affectedMob.getHits() <= 0 || owner.getStatus() != Action.CASTING_MOB) {
                        player.resetPath();
                        return;
                    }
                    if(!((NPC) affectedMob).getDef().isAttackable()) {
                        player.message("I can't attack that");
                        player.resetPath();
                        return;
                    }
                    if(affectedMob.getID() == 196) {
                        player.message("The dragon breathes fire at you");
                        int maxHit = 65;
                        if(player.getInventory().wielding(420)) {
                            maxHit = 10;
                            player.message("Your shield prevents some of the damage from the flames");
                        }
                        player.damage(Util.random(0, maxHit));
                    }
                    owner.reset();
                    switch(spellID) {
                        /*
                         * Confuse, reduces attack by 5% Weaken, reduces strength by 5%
                         * Curse reduces defense by 5%
                         * 
                         * Vulnerability, reduces defense by 10% Enfeeble, reduces
                         * strength by 10% Stun reduces attack by 10%
                         */
                        case 1: // confuse
                        case 5: // weaken
                        case 9: // curse
                        case 41: // vulnerability
                        case 44: // enfeeble
                        case 46: // stun
                            if(affectedMob instanceof NPC) {
                                NPC np = (NPC) affectedMob;
                                if(spellID == 1 || spellID == 46) {
                                    if(np.confused) {
                                        owner.getSender().sendMessage("Your opponent is already confused");
                                        return;
                                    }
                                }
                                if(spellID == 5 || spellID == 44) {
                                    if(np.isWeakend()) {
                                        owner.getSender().sendMessage("Your opponent is already weakend");
                                        return;
                                    }
                                }
                                if(spellID == 9 || spellID == 41) {
                                    if(np.cursed) {
                                        owner.getSender().sendMessage("Your opponent is already cursed");
                                        return;
                                    }
                                }
                            }
                            int stat = -1;
                            switch(spellID) {
                                case 1:
                                case 46:
                                    stat = 0;
                                    break;
                                case 5:
                                case 44:
                                    stat = 2;
                                    break;
                                case 9:
                                case 41:
                                    stat = 1;
                                    break;
                                default:
                                    break;
                            }

                            int statLv = -1;
                            if(affectedMob instanceof Player) {
                                Player affectedPlayer = (Player) affectedMob;
                                statLv = affectedPlayer.getCurStat(stat);
                            } else if(affectedMob instanceof NPC) {
                                NPC n = (NPC) affectedMob;
                                switch(stat) {
                                    case 0:
                                        statLv = n.getAttack();
                                        break;
                                    case 1:
                                        statLv = n.getDefense();
                                        break;
                                    case 2:
                                        statLv = n.getStrength();
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if(statLv == -1 || stat == -1) {
                                return;
                            }

                            if(affectedMob instanceof NPC) {
                                NPC n = (NPC) affectedMob;
                                switch(spellID) {
                                    case 1:
                                    case 46:
                                        n.confused = true;
                                        break;
                                    case 5:
                                    case 44:
                                        n.weakened = true;
                                        break;
                                    case 9:
                                    case 41:
                                        n.cursed = true;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if(!checkAndRemoveRunes(owner, spell)) {
                                return;
                            }

                            Projectile projectilez = new Projectile(owner, affectedMob, 1);
                            player.informOfProjectile(projectilez);
                            player.informOfModifiedHits(affectedMob);

                            finalizeSpell(owner, spell);
                            chasePlayer(owner, player, affectedNpc);
                            owner.getSender().sendInventory();
                            owner.getSender().sendStat(6);

                            return;
                        case 19: // Crumble undead
                            if(affectedMob instanceof Player) {
                                owner.getSender().sendMessage("This spell cannot be used on players");
                                return;
                            }
                            if(!checkAndRemoveRunes(owner, spell)) {
                                return;
                            }
                            NPC n = (NPC) affectedMob;
                            int damaga = Util.random(1, 5);

                            if(n.undead) {
                                damaga = Util.random(3, 13);
                            }

                            if(!n.undead) {
                                owner.getSender().sendMessage("This spell can only be used on the undead");
                                return;
                            }
                            if(Util.random(0, 8) == 2) {
                                damaga = 0;
                            }

                            Projectile projectilee = new Projectile(owner, affectedMob, 1);

                            affectedMob.setLastDamage(damaga);
                            int newp = affectedMob.getHits() - damaga;
                            affectedMob.setHits(newp);

                            ArrayList<Player> playersToInforms = new ArrayList<>();
                            playersToInforms.addAll(owner.getViewArea().getPlayersInView());
                            playersToInforms.addAll(affectedMob.getViewArea().getPlayersInView());

                            for(Player p : playersToInforms) {
                                p.informOfProjectile(projectilee);
                                p.informOfModifiedHits(affectedMob);
                            }
                            if(newp <= 0) {
                                affectedMob.killedBy(owner);
                            }
                            finalizeSpell(owner, spell);
                            chasePlayer(owner, player, affectedMob);
                            owner.getSender().sendInventory();
                            owner.getSender().sendStat(6);
                            return;
                        case 25:
                            if(affectedMob instanceof NPC) {
                                if(((NPC) affectedMob).getID() == 477) {
                                    player.getSender().sendMessage("The dragon seems immune to this spell");
                                    return;
                                }
                            }
                            boolean charged = false;
                            for(InvItem cape : owner.getInventory().getItems()) {
                                if(cape.getID() == 1000 && cape.isWielded()) {
                                    charged = true;
                                }
                            }
                            if(charged) {
                                if(!checkAndRemoveRunes(owner, spell)) {
                                    return;
                                }
                                int damag = Formulae.calcSpellHit(20, owner.getMagicPoints());
                                Projectile projectil = new Projectile(owner, affectedMob, 4);
                                affectedMob.setLastDamage(damag);
                                int newhp = affectedMob.getHits() - damag;
                                affectedMob.setHits(newhp);
                                player.informOfProjectile(projectil);
                                player.informOfModifiedHits(affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendStat(3);
                                } else if(affectedMob instanceof NPC) {
                                    NPC affectedNpc = (NPC) affectedMob;
                                    affectedNpc.addMageDamage(owner, damag);
                                }
                                if(newhp <= 0) {
                                    affectedMob.killedBy(owner);
                                }
                                owner.getSender().sendInventory();
                                owner.getSender().sendStat(6);
                                finalizeSpell(owner, spell);
                                break;
                            } else {
                                owner.getSender().sendMessage("You need to be wearing the Iban Staff to cast this spell!");
                                return;
                            }
                        case 33:
                            boolean hasCape = false;
                            for(InvItem cape : owner.getInventory().getItems()) {
                                if(cape.getID() == 1217 && cape.isWielded()) {
                                    hasCape = true;
                                }
                            }
                            if(hasCape) {
                                if(!owner.isCharged()) {
                                    //owner.getSender().sendMessage("@red@You are not charged!");
                                }
                                if(!checkAndRemoveRunes(owner, spell)) {
                                    return;
                                }
                                int damag = Formulae.calcGodSpells(owner, affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendMessage("Warning! " + owner.getUsername() + " is shooting at you!");
                                } else if(affectedMob instanceof NPC) {
                                    NPC affectedNpc = (NPC) affectedMob;
                                    affectedNpc.addMageDamage(owner, damag);
                                    double maxx = affectedNpc.getDef().hits;
                                    double cur = affectedNpc.getHits();
                                    int percent = (int) ((cur / maxx) * 100);
                                    /*if(PluginManager.getInstance().blockDefaultAction("NpcHealthPercentage", new Object[] { affectedNpc, percent })) {
                                return;
                            }*/
                                }
                                Projectile projectil = new Projectile(owner, affectedMob, 1);
                                godSpellObject(affectedMob, 33);
                                affectedMob.setLastDamage(damag);
                                int newhp = affectedMob.getHits() - damag;
                                affectedMob.setHits(newhp);
                                player.informOfProjectile(projectil);
                                player.informOfModifiedHits(affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendStat(3);
                                }
                                if(newhp <= 0) {
                                    affectedMob.killedBy(owner);
                                }
                                owner.getSender().sendInventory();
                                owner.getSender().sendStat(6);
                                finalizeSpell(owner, spell);
                                break;
                            } else {
                                owner.getSender().sendMessage("You need to be wearing the Staff of Guthix to cast this spell!");
                                return;
                            }
                        case 34:
                            boolean saradominCape = false;
                            for(InvItem cape : owner.getInventory().getItems()) {
                                if(cape.getID() == 1218 && cape.isWielded()) {
                                    saradominCape = true;
                                }
                            }
                            if(saradominCape) {
                                if(!owner.isCharged()) {
                                    //owner.getSender().sendMessage("@red@You are not charged!");
                                }
                                if(!checkAndRemoveRunes(owner, spell)) {
                                    return;
                                }
                                int damag = Formulae.calcGodSpells(owner, affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendMessage("Warning! " + owner.getUsername() + " is shooting at you!");
                                } else if(affectedMob instanceof NPC) {
                                    NPC affectedNpc = (NPC) affectedMob;
                                    affectedNpc.addMageDamage(owner, damag);
                                }
                                Projectile projectil = new Projectile(owner, affectedMob, 1);
                                godSpellObject(affectedMob, 34);
                                affectedMob.setLastDamage(damag);
                                int newhp = affectedMob.getHits() - damag;
                                affectedMob.setHits(newhp);
                                ArrayList<Player> playersToInfor = new ArrayList<>();
                                playersToInfor.addAll(owner.getViewArea().getPlayersInView());
                                playersToInfor.addAll(affectedMob.getViewArea().getPlayersInView());
                                for(Player p : playersToInfor) {
                                    p.informOfProjectile(projectil);
                                    p.informOfModifiedHits(affectedMob);
                                }
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendStat(3);
                                }
                                if(newhp <= 0) {
                                    affectedMob.killedBy(owner);
                                }
                                owner.getSender().sendInventory();
                                owner.getSender().sendStat(6);
                                finalizeSpell(owner, spell);
                                break;
                            } else {
                                owner.getSender().sendMessage("You need to be wearing the Staff of Saradomin to cast this spell!");
                                return;
                            }

                        case 35:
                            boolean hasZamorackCape = false;
                            for(InvItem cape : owner.getInventory().getItems()) {
                                if(cape.getID() == 1216 && cape.isWielded()) {
                                    hasZamorackCape = true;
                                }
                            }
                            if(hasZamorackCape) {
                                if(!owner.isCharged()) {
                                    //owner.getSender().sendMessage("@red@You are not charged!");
                                }
                                if(!checkAndRemoveRunes(owner, spell)) {
                                    return;
                                }
                                /*if(affectedMob instanceof Player && !owner.isDueling()) {
                            Player affectedPlayer = (Player)affectedMob;
                            owner.setSkulledOn(affectedPlayer);
                        }*/
                                int damag = Formulae.calcGodSpells(owner, affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendMessage("Warning! " + owner.getUsername() + " is shooting at you!");
                                } else if(affectedMob instanceof NPC) {
                                    NPC affectedNpc = (NPC) affectedMob;
                                    affectedNpc.addMageDamage(owner, damag);
                                }
                                Projectile projectil = new Projectile(owner, affectedMob, 1);
                                godSpellObject(affectedMob, 35);
                                affectedMob.setLastDamage(damag);

                                int newhp = affectedMob.getHits() - damag;
                                affectedMob.setHits(newhp);
                                player.informOfProjectile(projectil);
                                player.informOfModifiedHits(affectedMob);
                                if(affectedMob instanceof Player) {
                                    Player affectedPlayer = (Player) affectedMob;
                                    affectedPlayer.getSender().sendStat(3);
                                }
                                if(newhp <= 0) {
                                    affectedMob.killedBy(owner);
                                }
                                owner.getSender().sendInventory();
                                owner.getSender().sendStat(6);
                                finalizeSpell(owner, spell);
                                break;
                            } else {
                                owner.getSender().sendMessage("You need to be wearing the Staff of Zamarok to cast this spell");
                                return;
                            }

                        default:
                            if(!checkAndRemoveRunes(owner, spell)) {
                                return;
                            }
                            int max = -1;
                            for(int[] SPELLS : Constants.COMBAT_SPELLS) {
                                if(spell.getReqLevel() == SPELLS[0]) {
                                    max = SPELLS[1];
                                }
                            }
                            if(player.getMagicPoints() > 30) {
                                max += 1;
                            }
                            int damage = Formulae.calcSpellHit(max, owner.getMagicPoints());
                            if(affectedMob instanceof Player) {
                                Player affectedPlayer = (Player) affectedMob;
                                affectedPlayer.getSender().sendMessage("Warning! " + owner.getUsername() + " is shooting at you!");
                            }
                            if(affectedMob instanceof NPC) {
                                NPC npcc = (NPC) affectedMob;
                                npcc.addMageDamage(owner, damage);
                            }
                            Projectile projectile = new Projectile(owner, affectedMob, 1);
                            affectedMob.setLastDamage(damage);
                            int newHp = affectedMob.getHits() - damage;
                            affectedMob.setHits(newHp);

                            ArrayList<Player> playersToInform = new ArrayList<>();
                            playersToInform.addAll(owner.getViewArea().getPlayersInView());
                            playersToInform.addAll(affectedMob.getViewArea().getPlayersInView());
                            for(Player p : playersToInform) {
                                p.informOfProjectile(projectile);
                                p.informOfModifiedHits(affectedMob);
                            }
                            if(affectedMob instanceof Player) {
                                Player affectedPlayer = (Player) affectedMob;
                                affectedPlayer.getSender().sendStat(3);
                            }
                            if(newHp <= 0) {
                                if(affectedMob instanceof Player) {
                                    affectedMob.killedBy(owner);
                                }

                                if(owner instanceof Player) {
                                    Player toLoot = owner;
                                    if(owner instanceof Player && affectedMob instanceof NPC) {
                                        NPC npc = (NPC) affectedMob;
                                        toLoot = npc.handleLootAndXpDistribution(owner);
                                    }
                                    if(!(affectedMob instanceof Player)) {
                                        affectedMob.killedBy(toLoot);
                                    }
                                }
                            }
                            finalizeSpell(owner, spell);
                            if(newHp > 0) {
                                chasePlayer(owner, player, affectedMob);
                            }
                            break;
                    }
                    owner.getSender().sendInventory();
                    owner.getSender().sendStat(6);
                }
            });
        }
        player.getSender().sendInventory();
        player.getSender().sendStat(6);
    }

    public void handleCastOnLand(int spellID) {
        Player player = World.getWorld().getPlayer();
        SpellDef spell = EntityManager.getSpell(spellID);
        if(!checkRequirements(player, spell)) {
            return;
        }
        if(!checkAndRemoveRunes(player, spell)) {
            return;
        }
        switch(spellID) {
            case 7: // bones to bananas
                if(!checkAndRemoveRunes(player, spell)) {
                    return;
                }
                Iterator<InvItem> inventory = player.getInventory().iterator();
                int boneCount = 0;
                while(inventory.hasNext()) {
                    InvItem i = inventory.next();
                    if(i.getID() == 20) {
                        inventory.remove();
                        boneCount++;
                    }
                }
                if(boneCount == 0) {
                    player.message("You aren't holding any bones!");
                    return;
                }
                for(int i = 0; i < boneCount; i++) {
                    player.getInventory().add(new InvItem(249));
                }
                finalizeSpell(player, spell);
                break;
            case 47: // charge
                if (!player.getLocation().inMageArena()) {
                    if ((!player.getCache().hasKey("Flames of Zamorak_casts") && !player.getCache().hasKey("Saradomin strike_casts") && !player.getCache().hasKey("Claws of Guthix_casts"))
                            || ((player.getCache().hasKey("Saradomin strike_casts") && player.getCache().getInt("Saradomin strike_casts") < 100)) 
                            || ((player.getCache().hasKey("Flames of Zamorak_casts") && player.getCache().getInt("Flames of Zamorak_casts") < 100)) 
                            || ((player.getCache().hasKey("Claws of Guthix_casts") && player.getCache().getInt("Claws of Guthix_casts") < 100))) {
                        player.message("this spell can only be used in the mage arena");
                        return;
                    }
                }
                if(World.getWorld().getTile(player.getLocation()).hasGameObject()) {
                    player.getSender().sendMessage("You cannot charge here, please move to a different area");
                    return;
                }
                if(!checkAndRemoveRunes(player, spell)) {
                    return;
                }
                player.getSender().sendMessage("@gre@You feel charged with magical power");
                player.setCharged();
                godSpellObject(player, 47);
                finalizeSpell(player, spell);
                break;
        }
    }

    public void handleCastOnSelf(int spellID) {
        Player player = World.getWorld().getPlayer();
        SpellDef spell = EntityManager.getSpell(spellID);
        if(spell.getType() != 0) {
            return;
        }
        if(!checkRequirements(player, spell)) {
            return;
        }
        if(player.getLocation().wildernessLevel() >= 20) {
            player.message("A mysterious force blocks your teleport spell!");
            player.message("You can't use teleport after level 20 wilderness");
            return;
        }
        if(player.getLocation().inWilderness() && System.currentTimeMillis() - player.getCombatTimer() < 10000) {
            player.message("You need to stay out of combat for 10 seconds before using a teleport.");
            return;
        }
        if(player.getInventory().countId(1039) > 0) {
            player.message("You can't use teleport spells with ana in the barrel in your inventory.");
            return;
        }
        if(!player.getCache().hasKey("watchtower_scroll") && spellID == 31) {
            player.message("You cannot cast this spell");
            player.message("You need to finish the watchtower quest first");
            return;
        }
        if(spell.getName().equalsIgnoreCase("Ardougne teleport") && player.getQuestStage(Plugin.PLAGUE_CITY) != -1) {
            player.message("You don't know how to cast this spell yet");
            player.message("You need to do the plague city quest");
            player.resetPath();
            return;
        }
        if(player.getLocation().inModRoom()) {
            return;
        }
        if(!checkAndRemoveRunes(player, spell)) {
            return;
        }
        if(player.getLocation().inKaramja() || player.getLocation().inBrimhaven()) {
            while(player.getInventory().countId(318) > 0) {
                player.getInventory().remove(new InvItem(318));
            }
        }
        if(player.getInventory().hasItemId(812)) {
            player.message("the plague sample is too delicate...");
            player.message("it disintegrates in the crossing");
            while(player.getInventory().countId(812) > 0) {
                player.getInventory().remove(new InvItem(812));
            }
        }
        switch(spellID) {
            case 12: // Varrock
                player.teleport(120, 504, true);
                break;
            case 15: // Lumbridge
                player.teleport(120, 648, true);
                break;
            case 18: // Falador
                player.teleport(312, 552, true);
                break;
            case 22: // Camelot
                player.teleport(465, 456, true);
                break;
            case 26: // Ardougne
                player.teleport(588, 621, true);
                break;
            case 31: // Watchtower
                player.teleport(493, 3525, true);
                break;
            default:
                break;
        }
        finalizeSpell(player, spell);
        player.getSender().sendInventory();
        player.getSender().sendStat(6);
    }

    public void handleCastOnGroundItem(int x, int y, int itemID, int spellID) {
        Player player = World.getWorld().getPlayer();
        SpellDef spell = EntityManager.getSpell(spellID);
        if(!checkRequirements(player, spell)) {
            return;
        }
        Item item = null;
        for(Item i : player.getViewArea().getItemsInView()) {
            if(i.getID() == itemID && i.visibleTo(player) && i.getX() == x && i.getY() == y) {
                item = i;
                break;
            }
        }
        if(item == null) {
            return;
        }
        if(!new PathGenerator(player.getX(), player.getY(), item.getX(), item.getY()).isValid()) {
            player.getSender().sendMessage("I can't get a clear shot from here");
            player.resetPath();
            return;
        }
        final Item affectedItem = item;
        player.setStatus(Action.CASTING_GITEM);
        World.getWorld().getDelayedEventHandler().add(new WalkToPointEvent(player, affectedItem.getLocation(), 7, true) {
            @Override
            public void arrived() {
                owner.resetPath();
                if(!canCast(owner) || owner.getStatus() != Action.CASTING_GITEM || affectedItem.isRemoved()) {
                    return;
                }
                owner.reset();
                switch(spellID) {
                    case 16: // Telekinetic grab
                        /**
                         * ITEM 746 = HOLY GRAIL TROPHY - PREVENTION FROM TELEGRABBING IT ON FIRST ISLAND
                         *
                         */
                        if(affectedItem.getID() == 575 || affectedItem.getID() == 746 || affectedItem.getID() == 2115
                                || affectedItem.getID() == 2116 || affectedItem.getID() == 2117
                                || affectedItem.getID() == 2118 || affectedItem.getID() == 2119) {
                            player.message("You may not telegrab this item");
                            return;
                        }
                        if(affectedItem.getID() == 1093) {
                            player.message("I can't use telekinetic grab on the cat");
                            return;
                        }
                        if(affectedItem.getLocation().inBounds(97, 1428, 106, 1440)) {
                            player.message("Telekinetic grab cannot be used in here");
                            return;
                        }
                        if(!checkAndRemoveRunes(owner, spell)) {
                            return;
                        }
                        owner.getSender().sendTeleBubble(location.getX(), location.getY(), true);
                        for(Object o : owner.getWatchedPlayers().getAllEntities()) {
                            Player p = ((Player) o);
                            p.getSender().sendTeleBubble(location.getX(), location.getY(), true);
                        }
                        world.unregisterItem(affectedItem);
                        finalizeSpell(owner, spell);
                        owner.getInventory().add(new InvItem(affectedItem.getID(), affectedItem.getAmount()));
                        break;
                }
                owner.getSender().sendInventory();
                owner.getSender().sendStat(6);
            }
        });
        player.getSender().sendInventory();
        player.getSender().sendStat(6);
    }
    
    public void handleCastOnInventoryItem(int itemSlot, int spellID) {
        Player player = World.getWorld().getPlayer();
        SpellDef spell = EntityManager.getSpell(spellID);
        if(!checkRequirements(player, spell)) {
            return;
        }
        if (spell.getType() != 3) {
            return;
        }
        InvItem affectedItem = player.getInventory().get(itemSlot);
        if (affectedItem == null) { // This shoudln't happen
            player.resetPath();
            return;
        }
        switch(spellID) {
            case 3: // Enchant lvl-1 Sapphire amulet
                if (affectedItem.getID() == 302) {
                    if (!checkAndRemoveRunes(player, spell)) {
                        return;
                    }
                    player.getInventory().remove(affectedItem);
                    player.getInventory().add(new InvItem(314));
                    finalizeSpell(player, spell);
                } 
                else {
                    player.getSender().sendMessage("This spell cannot be used on this kind of item");
                }
                break;
            case 10: // Low level alchemy
                if (affectedItem.getID() == 10) {
                    player.getSender().sendMessage("Thats already alchemized!");
                    return;
                }
                if (!affectedItem.getDef().isTradable()) {
                    player.getSender().sendMessage("You cannot alchemize non-tradable items");
                    return;
                }
                if (!checkAndRemoveRunes(player, spell)) {
                    return;
                }
                if (player.getInventory().remove(affectedItem) > 1) {
                    int value = (int) (affectedItem.getDef().getPrice() * 0.4D * affectedItem.getAmount());
                    player.getInventory().add(new InvItem(10, value)); // 40%
                }
                finalizeSpell(player, spell);
                break;
            case 13: // Enchant lvl-2 emerald amulet
                if (affectedItem.getID() == 303) {
                    if (!checkAndRemoveRunes(player, spell)) {
                        return;
                    }
                    player.getInventory().remove(affectedItem);
                    player.getInventory().add(new InvItem(315));
                    finalizeSpell(player, spell);
                    } 
                else {
                    player.getSender().sendMessage("This spell cannot be used on this kind of item");
                }
                break;
            case 21: // Superheat item
                ItemSmeltingDef smeltingDef = affectedItem.getSmeltingDef();
                if (smeltingDef == null || affectedItem.getID() == 155) {
                    player.message("This spell can only be used on ore");
                    return;
                }
                for (ReqOreDef reqOre : smeltingDef.getReqOres()) {
                    if (player.getInventory().countId(reqOre.getId()) < reqOre.getAmount()) {
                        if (affectedItem.getID() == 151) {
                            smeltingDef = EntityManager.getItemSmeltingDef(9999);
                            break;
                        }
                        if (affectedItem.getID() == 202 || affectedItem.getID() == 150) {
                            player.message("You also need some " + (affectedItem.getID() == 202 ? "copper" : "tin")
                                    + " to make bronze");
                            return;
                        }
                        player.message("You need " + reqOre.getAmount() + " heaps of "
                                + EntityManager.getItem(reqOre.getId()).getName().toLowerCase() + " to smelt "
                                + affectedItem.getDef().getName().toLowerCase().replaceAll("ore", ""));
                        return;
                    }
                }

                if (player.getCurStat(13) < smeltingDef.getReqLevel()) {
                    player.message("You need to be at least level-" + smeltingDef.getReqLevel() + " smithing to smelt "
                            + EntityManager.getItem(smeltingDef.barId).getName().toLowerCase().replaceAll("bar", ""));
                    return;
                }
                if (!checkAndRemoveRunes(player, spell)) {
                    return;
                }
                InvItem bar = new InvItem(smeltingDef.getBarId());
                if (player.getInventory().remove(affectedItem) > -1) {
                    for (ReqOreDef reqOre : smeltingDef.getReqOres()) {
                        for (int i = 0; i < reqOre.getAmount(); i++) {
                            player.getInventory().remove(new InvItem(reqOre.getId()));
                        }
                    }
                    player.message("You make a bar of " + bar.getDef().getName().replace("bar", "").toLowerCase());
                    player.getInventory().add(bar);
                    player.incExp(13, smeltingDef.getExp(), true);
                }
                finalizeSpell(player, spell);
                break;
            case 24: // Enchant lvl-3 ruby amulet
                if (affectedItem.getID() == 304) {
                    if (!checkAndRemoveRunes(player, spell)) {
                        return;
                    }
                    player.getInventory().remove(affectedItem);
                    player.getInventory().add(new InvItem(316));
                    finalizeSpell(player, spell);
                    }
                else {
                    player.getSender().sendMessage("This spell cannot be used on this kind of item");
                }
                break;
            case 28: // High level alchemy
                if (affectedItem.getID() == 10) {
                    player.getSender().sendMessage("Thats already alchemized!");
                    return;
                }
                if (!affectedItem.getDef().isTradable()) {
                    player.getSender().sendMessage("You cannot alchemize non-tradable items");
                    return;
                }
                if (!checkAndRemoveRunes(player, spell)) {
                    return;
                }
                if (player.getInventory().remove(affectedItem) > -1) {
                    int value = (int) (affectedItem.getDef().getPrice() * 0.6D * affectedItem.getAmount());
                    player.getInventory().add(new InvItem(10, value)); // 60%
                }
                finalizeSpell(player, spell);
                break;
            case 30: // Enchant lvl-4 diamond amulet
                if (affectedItem.getID() == 305) {
                    if (!checkAndRemoveRunes(player, spell)) {
                        return;
                    }
                    player.getInventory().remove(affectedItem);
                    player.getInventory().add(new InvItem(317));
                    finalizeSpell(player, spell);
                    } 
                else {
                    player.getSender().sendMessage("This spell cannot be used on this kind of item");
                }
                break;
            case 43: // Enchant lvl-5 dragonstone amulet
                if(affectedItem.getID() == 610) { 
                    if(!checkAndRemoveRunes(player, spell)) { 
                        return; 
                    }
                    player.getInventory().remove(affectedItem);
                    player.getInventory().add(new InvItem(522));
                    finalizeSpell(player, spell); 
                }
                else {
                    player.getSender().sendMessage("This spell cannot be used on this kind of item"); 
                }
                break;
        
        }
        if (affectedItem.isWielded()) {
            player.getSender().sendSound(SoundEffect.CLICK);
            affectedItem.setWield(false);
            player.updateWornItems(affectedItem.getWieldableDef().getWieldPos(), player.getAppearance().getSprite(affectedItem.getWieldableDef().getWieldPos()));
            player.getSender().sendEquipmentStats();
        }
        player.getSender().sendInventory();
        player.getSender().sendStat(6);
    }

    // START HELPER METHODS
    
    private boolean checkRequirements(Player player, SpellDef spell) {
        if((player.isBusy() && !player.inCombat()) || player.isRanging()) {
            return false;
        }
        if(!canCast(player)) {
            return false;
        }

        player.reset();

        if(spell.isMembers() && !Constants.MEMBER_WORLD) {
            player.getSender().sendMessage("You need to login to a members world to use this spell");
            player.resetPath();
            return false;
        }

        if(player.getCurStat(6) < spell.getReqLevel()) {
            player.setSuspiciousPlayer(true);
            player.getSender().sendMessage("Your magic ability is not high enough for this spell");
            player.resetPath();
            return false;
        }
        if(!Formulae.castSpell(spell, player.getCurStat(6), player.getMagicPoints())) {
            player.getSender().sendMessage("The spell fails, you may try again in 20 seconds");
            player.getSender().sendSound(SoundEffect.SPELL_FAIL);
            player.setSpellFail();
            player.resetPath();
            return false;
        }
        return true;
    }

    private void finalizeSpell(Player player, SpellDef spell) {
        player.lastCast = System.currentTimeMillis();
        player.getSender().sendSound(SoundEffect.SPELL_OK);
        player.getSender().sendMessage("Cast spell successfully");
        player.incExp(6, spell.getExp(), true);
        player.setCastTimer();
    }

    private boolean canCast(Player player) {
        if(!player.castTimer()) {
            player.getSender().sendMessage("You must wait another " + player.getSpellWait() + " seconds to cast another spell");
            player.resetPath();
            return false;
        }
        return true;
    }

    private final TreeMap<Integer, InvItem[]> staffs = new TreeMap<Integer, InvItem[]>() {{
        put(31, new InvItem[]{new InvItem(197), new InvItem(615), new InvItem(682)}); // fire-rune
        put(32, new InvItem[]{new InvItem(102), new InvItem(616), new InvItem(683)}); // water-rune
        put(33, new InvItem[]{new InvItem(101), new InvItem(617), new InvItem(684)}); // air-rune
        put(34, new InvItem[]{new InvItem(103), new InvItem(618), new InvItem(685)}); // earth-rune
    }};

    private InvItem[] getStaffs(int runeID) {
        InvItem[] items = staffs.get(runeID);
        if(items == null) {
            return new InvItem[0];
        }
        return items;
    }

    private boolean checkAndRemoveRunes(Player player, SpellDef spell) {
        for(Entry<Integer, Integer> e : spell.getRunesRequired()) {
            boolean skipRune = false;
            for(InvItem staff : getStaffs(e.getKey())) {
                if(player.getInventory().contains(staff)) {
                    for(InvItem item : player.getInventory().getItems()) {
                        if(item.equals(staff) && item.isWielded()) {
                            skipRune = true;
                            break;
                        }
                    }
                }
            }
            if(skipRune) {
                continue;
            }
            if(player.getInventory().countId((e.getKey())) < (e.getValue())) {
                player.getSender().sendMessage("You don't have all the reagents you need for this spell");
                return false;
            }
        }
        for(Entry<Integer, Integer> e : spell.getRunesRequired()) {
            boolean skipRune = false;
            for(InvItem staff : getStaffs(e.getKey())) {
                if(player.getInventory().contains(staff)) {
                    for(InvItem item : player.getInventory().getItems()) {
                        if(item.equals(staff) && item.isWielded()) {
                            skipRune = true;
                            break;
                        }
                    }
                }
            }
            if(skipRune) {
                continue;
            }
            player.getInventory().remove((e.getKey()), (e.getValue()));
            player.getSender().sendInventory();
        }
        return true;
    }

    private void godSpellObject(Mob affectedMob, int spell) {
        switch(spell) {
            case 33:
                GameObject guthix = new GameObject(affectedMob.getLocation(), 1142, 0, 0);
                World.getWorld().registerGameObject(guthix);
                World.getWorld().getDelayedEventHandler().add(new RemoveObjectEvent(guthix, 500));
                break;
            case 34:
                GameObject sara = new GameObject(affectedMob.getLocation(), 1031, 0, 0);
                World.getWorld().registerGameObject(sara);
                World.getWorld().getDelayedEventHandler().add(new RemoveObjectEvent(sara, 500));
                break;
            case 35:
                GameObject zammy = new GameObject(affectedMob.getLocation(), 1036, 0, 0);
                World.getWorld().registerGameObject(zammy);
                World.getWorld().getDelayedEventHandler().add(new RemoveObjectEvent(zammy, 500));
                break;
            case 47:
                GameObject charge = new GameObject(affectedMob.getLocation(), 1147, 0, 0);
                World.getWorld().registerGameObject(charge);
                World.getWorld().getDelayedEventHandler().add(new RemoveObjectEvent(charge, 500));
                break;
        }
    }

    private void chasePlayer(Player owner, final Player player, Mob affectedMob) {
        if(affectedMob instanceof NPC) {
            final NPC npc = (NPC) affectedMob;

            if(npc.isBusy() || npc.getChasing() != null) {
                return;
            }

            npc.resetPath();
            npc.setChasing(player);

            // Radius is 0 to prevent wallhacking by
            // NPCs. Easiest method I can come up with
            // for now.
            World.getWorld().getDelayedEventHandler().add(new WalkMobToMobEvent(affectedMob, owner, 0) {
                @Override
                public void arrived() {
                    if(affectedMob.isBusy() || owner.isBusy()) {
                        npc.setChasing(null);
                        return;
                    }
                    if(affectedMob.inCombat() || owner.inCombat()) {
                        npc.setChasing(null);
                        return;
                    }
                    npc.resetPath();

                    player.reset();
                    player.resetPath();
                    player.setBusy(true);
                    player.setStatus(Action.FIGHTING_MOB);
                    player.getSender().sendSound(SoundEffect.UNDER_ATTACK);
                    player.getSender().sendMessage("You are under attack!");

                    npc.setLocation(player.getLocation(), true);
                    for(Player p : npc.getViewArea().getPlayersInView()) {
                        p.removeWatchedNpc(npc);
                    }

                    player.setSprite(9);
                    player.setOpponent(npc);
                    player.setCombatTimer();

                    npc.setBusy(true);
                    npc.setSprite(8);
                    npc.setOpponent(player);
                    npc.setCombatTimer();

                    npc.setChasing(null);
                    FightEvent fighting = new FightEvent(player, npc, true);
                    fighting.setLastRun(0);
                    World.getWorld().getDelayedEventHandler().add(fighting);
                }

                @Override
                public void failed() {
                    npc.setChasing(null);
                }
            });
        }
    }

}
