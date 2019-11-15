package org.nemotech.rsc.plugins.items;

import org.nemotech.rsc.model.player.InvItem;
import org.nemotech.rsc.model.MenuHandler;
import org.nemotech.rsc.model.player.Player;

import org.nemotech.rsc.util.Util;
import org.nemotech.rsc.plugins.Plugin;
import org.nemotech.rsc.plugins.listeners.action.InvActionListener;
import org.nemotech.rsc.plugins.listeners.executive.InvActionExecutiveListener;

public class InvAction extends Plugin implements InvActionListener, InvActionExecutiveListener {

    @Override
    public boolean blockInvAction(InvItem item, Player player) {
        if(inArray(item.getID(), 668, 1073, 537, 777, 781, 752, 706, 918, 920, 921, 922,
                926, 799, 936, 1099, 1060, 1039, 996, 1004, 1005, 1031, 1086, 1087, 1181, 1141,
                1142, 1143, 1144, 1173, 1025, 1174, 793)) {
            return true;
        }
        return false;
    }

    @Override
    public void onInvAction(InvItem item, Player p) {
        if(item.getID() == 793) {
            p.message("you open the oyster shell");
            if(Util.random(0, 1) == 1) {
                p.getInventory().replace(793, 792);
                p.getSender().sendInventory();
            } else {
                p.getInventory().replace(793, 791);
                p.getSender().sendInventory();
            }
        }
        if(item.getID() == 1025) {
            p.getSender().sendAlert("THE TALE OF SCORPIUS: %A HISTORY OF ASTROLOGY IN RUNESCAPE. %At the start of the Fourth age, A learned man by the name of Scorpius, known well for his powers of vision and magic, sought communion with the gods of the world. After many years of study he developed a machine infused with magical power, that had the ability to pierce into the very heavens itself, a huge eye that gave the user sight like never before. As Time passed Scorpius grew adept at his skill, and followed the star movements themselves which he mapped and named, and are still used to this very day. Before long Scorpius used his knowledge for predicting the future and gaining dark knowledge. Years after his death, the plans of Scorpius were uncovered at an ancient Zamorakian worship site, and the heavenly eye was again constructed. Since then many have learned the ways of the Astrologer. Some claim his ghost still wanders ever seeking his master Zamorak, and will grant those adept in the arts of the Astrologer a blessing of power. Here ends the tale of how Astrology entered the known world.", true);
        }
        if(item.getID() == 668) {
            if(p.getCache().hasKey("barone") && 
                    p.getCache().hasKey("bartwo") && 
                    p.getCache().hasKey("barthree") && 
                    p.getCache().hasKey("barfour") && 
                    p.getCache().hasKey("barfive") && 
                    p.getCache().hasKey("barsix")) {
                p.message("You are to drunk to be able to read the barcrawl card");
                return;

            }
            message(p, "The official Alfred Grimhand barcrawl");
            p.message(!p.getCache().hasKey("barone") ? "The jolly boar inn - not completed" : "The jolly boar inn - completed");
            sleep(800);
            p.message(!p.getCache().hasKey("bartwo") ? "The blue moon inn - not completed" : "The blue moon inn - completed");
            sleep(800);
            p.message(!p.getCache().hasKey("barthree") ? "The rising sun - not completed" : "The rising sun - completed");
            sleep(800);
            p.message(!p.getCache().hasKey("barfour") ? "The dead man's chest - not completed" : "The dead man's chest - completed");
            sleep(800);
            p.message(!p.getCache().hasKey("barfive") ? "The forester's arms - not completed" : "The forester's arms - completed");
            sleep(800);
            p.message(!p.getCache().hasKey("barsix") ? "The rusty anchor - not completed" : "The rusty anchor - completed");
        }
        if(item.getID() == 1073) {
            String[] options;
            options = new String[]{"Constructing the cannon", "Making ammo", "firing the cannon", "warrenty"};
            message(p, "the manual has four pages");
            p.setMenuHandler(new MenuHandler(options) {
                @Override
                public void handleReply(int option, String reply) {
                    if(owner.isBusy()) {
                        return;
                    }
                    switch(option) {
                    case 0:
                        owner.getSender().sendAlert("Constructing the cannon% %" 
                                +
                                "To construct the cannon, firstly set down Dwarf cannon base on the ground.% %" 
                                +
                                "Next add the Dwarf cannon stand to the Dwarf cannon base.% %"
                                +
                                "Then add the Dwarf cannon barrels (this can be tiring work).% %"
                                +
                                "Last of all add the Dwarf cannon furnace which powers the cannon.% %"
                                + 
                                "You should now have a fully set up dwarf multi cannon ready to go splat some nasty creatures.% % % %"
                                +
                                "@red@WARNING: You should be well rested before attempting to @red@lift the heavy cannon", true);
                        break;
                    case 1:
                        owner.getSender().sendAlert("Making ammo% %" 
                                +
                                "The ammo for the cannon is made from steel bars.% %" 
                                +
                                "Firstly you must heat up a steel bar in a furnace% %"
                                +
                                "Then pour the molten steel into a cannon ammo mould% %"
                                +
                                "You should now have a ready to fire multi cannon ball% %", true);
                        break;
                    case 2:
                        owner.getSender().sendAlert("Firing the cannon% %" 
                                +
                                "The cannon will only fire when monsters are available to target.% %" 
                                +
                                "If you are carrying enough ammo the multi cannon will fire up to 20 rounds before stopping.% %"
                                +
                                "The cannon will automatically target non friendly creatures.% %"
                                +
                                "@red@Warning - firing the cannon is exhausting work and can @red@leave adventurers too fatigued to carry the cannon, so @red@rest well before using", true);
                        break;
                    case 3:
                        owner.getSender().sendAlert("@red@Dwarf cannon warrenty% %" 
                                +
                                "If your cannon is stolen or lost, after or during being set up, the dwarf engineer will happily replace the parts% %" 
                                +
                                "However cannon parts that were given away or dropped will not be replaced for free% %"
                                +
                                "It is only possible to operate one cannon at a time% % % % % %"
                                +
                                "by order of the dwarwven black guard", true);
                        break;
                    }
                }
            });
            p.getSender().sendMenu(options);
        } /** Tree gnome translation - grand tree quest **/
        if(item.getID() == 918) {
            message(p, "the book contains the alphabet...",
                    "translated into the old gnome tounge");
            // http://i.imgur.com/XmSmukw.png
            p.getSender().sendAlert(
                    "@yel@A = @red@:v  @yel@B = @red@x:   @yel@C = @red@za% %"
                            + 
                            "@yel@D = @red@qe  @yel@E = @red@:::   @yel@F = @red@hb% %"
                            + 
                            "@yel@G = @red@qa  @yel@H = @red@x   @yel@I = @red@xa% %"
                            +
                            "@yel@J = @red@ve  @yel@K = @red@vo   @yel@L = @red@va% %"
                            +
                            "@yel@M = @red@ql  @yel@N = @red@ha   @yel@O = @red@ho% %"
                            +
                            "@yel@P = @red@ni  @yel@Q = @red@na   @yel@R = @red@qi% %"
                            +
                            "@yel@S = @red@sol  @yel@T = @red@lat   @yel@U = @red@z% %"
                            +
                            "@yel@V = @red@::  @yel@W = @red@h:   @yel@X = @red@:i:% %"
                            +
                            "@yel@Y = @red@im  @yel@Z = @red@dim% %"
                            , true);
        }
        /** Gloughs journal - grand tree quest **/
        if(item.getID() == 921) {
            p.message("the book contains several hurried notes");
            int menu = showMenu(p, "the migration failed", "they must be stopped", "gaining support");
            if(menu == 0) {
                p.getSender().sendAlert("@red@The migration failed" + " %" + " %"
                        + "After spending half a century hiding underground you would think that the great migration would have improved life on runescape for tree gnomes. However, rather than the great liberation promised to us by king Healthorg at the end of the last age, we have been forced to live in hiding ,up trees or in the gnome maze, laughed at and mocked by man. Living in constant fear of human aggression, we are in a no better situation now then when we lived in the caves% %"
                        + "Change must come soon", true); 
            } else if(menu == 1) {
                p.getSender().sendAlert("@red@They must be stopped" + " %" + " %"
                        + "Today I heard of three more gnomes slain by Khazard's human troops for fun, I cannot control my anger" + " %" + " %"
                        + "Humanity seems to have aquired a level of arrogance comparable to that of zamorak, killing and pillaging at will. We are small and at heart not warriors, but something must be done, we will pickup arms and go forth into the human world. We will defend ourselves and we will pursue justice for all gnomes who fell at the hands of humans", true); 
            } else if(menu == 2) {
                p.getSender().sendAlert("@red@gaining support" + " %" + " %"
                        + "Some of the local gnomes seem strangly deluded about humans, many actually believe that humans are not all naturally evil but instead vary from person to person" + " %" + " %"
                        + "This sort of talk could be the end for the tree gnomes and i must continue to convince my fellow gnome folk the cold truth about these human creatures, how they will not stop until all gnome life is destroyed - unless  we can destroy them first", true); 
            }
        }
        /** shipyard invoice - grand tree quest **/
        if(item.getID() == 922) {
            message(p, "you open the invoice");
            p.getSender().sendAlert(
                    "@red@Order" 
                            + " %" + " %" + 
                            "30 karamja battleships to be constructed in karamja"
                            + " %" + " %" + 
                            "Timber needed - 2000 tons"
                            + " %" + " %" + 
                            "Troops to be carried - 300"
                            , true); 
        }
        /** Glough's notes - grand tree quest **/
        if(item.getID() == 926) {
            message(p, "the notes contain sketched maps and diagrams", "the text reads");
            p.getSender().sendAlert(
                    "@red@invasion" 
                            + " %" + " %" + 
                            "Troops board three fleets at karamja"
                            + " %" + " %" + 
                            "Fleet one attacks misthalin from south"
                            + " %" + " %" + 
                            "Fleet two groups at crandor and attacks Asgarnia from west coast"
                            + " %" + " %" + 
                            "Fleet three sails north attack Kandarin from south rienforced by gnome foot soldiers leaving gnome stronghold"
                            + " %" + " %" + 
                            "All prisoners to be slain"
                            , true); 
        }
        /** War ship - just for fun! **/
        if(item.getID() == 920) {
            message(p, "you pretend to sail the ship across the floor",
                    "you soon become very bored",
                    "and realise you look quite silly");
        }
        if(item.getID() == 537) {
            message(p, "Pentember the 3rd",
                    "The experiment is going well - moved it to the wooden shed in the garden",
                    "It does too much damage in the house",
                    "Pentember the 6th",
                    "Don't want people getting in back garden to see the experiment",
                    "A guy called Professer Odenstein is fitting me a new security system",
                    "Pentember the 8th",
                    "The security system is done - by zamorak is it contrived!",
                    "Now to open my own back door",
                    "I lure a rat out of a hole in the back porch",
                    "I fit a magic curved piece of metal to its back",
                    "The rat goes back in the hole, and the door unlocks",
                    "The prof tells me that this is cutting edge technology!");
        }
        if(item.getID() == 777) {
            p.message("you rub together the dry sticks");
            if(p.getMaxStat(11) < 30) {
                p.message("you need a firemaking level of 30 or above");
                p.message("the sticks smoke momentarily then die out");
                return;
            }
            message(p, "The sticks catch alight");
            if(removeItem(p, 773, 1)) {
                p.message("you place the smouldering twigs to your torch");
                p.message("your torch lights");
                p.getInventory().add(new InvItem(774));
                p.getSender().sendInventory();
            } else {
                p.message("the sticks smoke momentarily then die out");
            }
        }
        if(item.getID() == 781) {
            message(p, "The handwriting on this note is very scruffy",
                    "as far as you can make out it says",
                    "Got a bncket of nnlk",
                    "Tlen qrind sorne lhoculate",
                    "vnith a pestal and rnortar",
                    "ald the grourd dlocolate to tho milt",
                    "fnales add 5cme snape gras5",
                    "you guess it really says something slightly different");
        }
        if(item.getID() == 752) {
            message(p, "You memorise what is written on the scroll",
                    "You can now cast the Ardougne teleport spell",
                    "Provided you have the required runes and magic level",
                    "The scroll crumbles to dust");
            removeItem(p, 752, 1);
        }
        /** TOURIST GUIDE BOOK - REMAKE THIS CODE LATER **/
        if(item.getID() == 706) {
            message(p, "You read the guide");
            playerTalk(p, null, "This book is your guide to the vibrant city of Ardougne",
                    "Ardougne is an exciting modern city",
                    "Located on the sunny south coast of Kandarin");
            message(p, "Pick a chapter to read");
            int chapter = showMenu(p,
                    "Ardougne city of shopping",
                    "Ardougne city of history",
                    "Ardougne city of fun",
                    "The area surrounding Ardougne",
                    "I don't want to read this rubbish");
            if(chapter == 0) {
                message(p, "Ardougne city of shopping",
                        "Come sample the delights of the Ardougne market",
                        "The biggest in the known world",
                        "From spices to silk",
                        "There is something here for everyone",
                        "Other popular shops in the area include Zeneshas the armourer",
                        "And the adventurers supply store");
            } else if(chapter == 1) {
                message(p, "Ardougne, city of history",
                        "Ardougne is an important historical city",
                        "One historic building is the magnificent Handelmort mansion",
                        "Currently owned by Lord Franis Bradley Handelmort",
                        "Ardougne castle in the east side of the city",
                        "Is now open to the public",
                        "and members of the holy order of ardougne paladins",
                        "Still wander the streets");
            } else if(chapter == 2) {
                message(p, "Ardougne city of fun",
                        "If you're looking for entertainment in Ardougne",
                        "Why not pay a visit to Ardougne city zoo",
                        "Or relax for a drink in the flying horse inn",
                        "Or slaughter rats in Ardougne sewers");
            } else if(chapter == 3) {
                message(p, "The area surrounding Ardougne",
                        "If you want to go further afield",
                        "Why not have a look at the pillars of Zanash",
                        "The mysterious marble pillars west of the city",
                        "Or the town of Brimhaven, on exotic Karamja",
                        "Is only a short boat ride away",
                        "Ships leaving regularily from Ardougne harbour");
            } else if(chapter == 4) {
                playerTalk(p,null, "I don't want to read this rubbish");
            }
        }
        if(item.getID() == 799) {
            /** COORDINATES CHECK I BELIEVE?! **/
            /** Biohazard quest **/
            p.message("you open the cage");
            if((p.getCache().hasKey("bird_feed") || p.getQuestStage(BIOHAZARD) == 3) && p.getLocation().inBounds(617, 582, 622, 590)) {
                message(p, "the pigeons fly towards the watch tower",
                        "they begin pecking at the bird feed",
                        "the mourners are frantically trying to scare the pigeons away");
                if(p.getQuestStage(BIOHAZARD) == 2) {
                    p.updateQuestStage(BIOHAZARD, 3);
                }
                if(p.getCache().hasKey("bird_feed")) {
                    p.getCache().remove("bird_feed");
                }
                p.getInventory().replace(799, 798);
                p.getSender().sendInventory();
            } else {
                p.message("the pigeons don't want to leave");
            }
        }//JANGERBERRIES EAT SHIT
        if(item.getID() == 936) {
            message(p, "You eat the Jangerberries");
            removeItem(p, 936, 1);
            int Attack = p.getMaxStat(0) + 2;
            int Strength = p.getMaxStat(0) + 1;
            if(p.getCurStat(3) < p.getMaxStat(3)) {
                p.setCurStat(3, p.getCurStat(3) + 2);
            }
            if(p.getCurStat(5) < p.getMaxStat(5)) {
                p.setCurStat(5, p.getCurStat(5) + 1);
            }
            if(p.getCurStat(1) < 1) {
                p.setCurStat(1, 0);
            } else {
                p.setCurStat(1, p.getCurStat(1) - 1);
            }
            if(p.getCurStat(0) < Attack) {
                p.setCurStat(0, p.getCurStat(0) + 1);
            }
            if(p.getCurStat(2) < Strength) {
                p.setCurStat(2, p.getCurStat(2) + 1);
            }
            p.getSender().sendStats();
            p.message("They taste very bitter");
        } 
        if(item.getID() == 1099) { 
            p.getSender().sendAlert("@red@*** Shantay Disclaimer***% %@gre@The Desert is a VERY Dangerous place.% %@red@Do not enter if you're scared of dying.% %@gre@Beware of high temperatures, sand storms, and slavers% %@red@No responsibility is taken by Shantay% %@gre@If anything bad happens to you under any circumstances.", true);
        } 
        if(item.getID() == 1060) {
            message(p, "The plans look very technical!",
                    "But you can see that this item will require ",
                    "a bronze bar and at least 10 feathers.");
        }
        if(item.getID() == 1039) {
            message(p, "Ana looks pretty angry, she starts shouting at you.",
                    "@gre@Ana: Get me out of here!",
                    "@gre@Ana: Do you hear me!",
                    "@gre@Ana: Get me out of here I say!");
        }
        if(item.getID() == 996) {
            message(p, "the journal is old and worn");
            p.message("it reads...");
            p.getSender().sendAlert("@dre@I came to cleanse these mountain passes of the dark forces%@dre@that dwell here, I knew my journey would be treacherous.% %@dre@I have deposited Spheres of Light in some of the tunnels%@dre@These spheres are a beacon of safety for all who come.%@dre@I still feel iban relentlessly tugging at my weak soul.% %@dre@The spheres were created by Saradominist mages.%@dre@When held they boost faith and courage%@dre@bringing out any innate goodness to ones heart%@dre@illuminating the dark caverns with the light of saradomin%@dre@bringing fear and pain to all who embrace the dark side.% %@dre@My men are still repelled by 'ibans well', it seems as if%@dre@there pure hearts bar them from entering%@dre@ibans realm%@dre@my turn has come, I dare not admit it to my loyal men%@dre@But I fear for my soul", true);

        }
        if(item.getID() == 1004) {
            message(p, "you carefully search the doll");
            if(p.getCache().hasKey("poison_on_doll")) {
                message(p, "Blood has been smeared onto the doll");
            }
            if(p.getCache().hasKey("cons_on_doll")) {
                message(p, "Crushed bones have been smeared onto the doll");
            } 
            if(p.getCache().hasKey("ash_on_doll")) {
                message(p, "Burnt ash has been smeared onto the doll");
            }
            if(p.getCache().hasKey("shadow_on_doll")) {
                message(p, "A dark liquid has been poured over the doll");
            }
            p.message("the doll is made from old wood and cloth");
        }
        if(item.getID() == 1005) {
            message(p, "the journal is old and covered in dust");
            p.message("inside are several chapters...");
            String[] chapters = { "intro", "iban", "the ressurection", "the four elements" };
            int menu = showMenu(p, chapters);
            if(menu >= 0 && menu <= 3) {
                message(p, "you turn to the page titled '" + chapters[menu] + "'");
            }
            if(menu == 0) {
                p.getSender().sendAlert("% %@dre@Gather round, all ye followers of the dark arts.% %"
                    + "@dre@Read carefully the words that I hereby inscribe,% %"
                    + "@dre@as I detail the heady the brew that is responsible for my% %"
                    + "@dre@greatest creation yet. I am Kardia, the most wretched% %"
                    + "@dre@witch in all the land,scorned by beauty and the world.% %"
                    + "@dre@See what I have created: the most powerful force% %"
                    + "@dre@of darkness ever to be seen in human form!", true);
            } else if(menu == 1) {
                p.getSender().sendAlert("@dre@Iban was a Black Knight who had learned to fight under the%"
                    + "@dre@great Darkquerius himself. Together they had taken on the%"
                    + "@dre@might of the White Knights, and the blood of a hundred%"
                    + "@dre@soldiers had been wiped from Iban's sword.% %"
                    
                    + "@dre@In many respects Iban was not so different from the White%"
                    + "@dre@Knights that he so mercilessly slaughtered: noble and%"
                    + "@dre@educated with a taste for the finer things in life. But there%"
                    + "@dre@was something that made him different: ambition. No, not%"
                    + "@dre@the simple desire to succeed or lead one's fellow man.%"
                    + "@dre@This was an ambition that hungered for something beyond%"
                    + "@dre@the mortal realm", true);
                int subMenu = showMenu(p, "continue reading", "close book");
                if(subMenu == 0) {
                    p.getSender().sendAlert("%@dre@..that was almost godlike in its insatiability.% %"

                        + "@dre@But therein lay the essence of his darkness. At its most%"
                        + "@dre@base level, Iban's fundamental impulse was a desire to%"
                        + "@dre@control the hearts and minds of his fellow man. To take%"
                        + "@dre@them beyond the pale of mere allegiance, and corrupt%"
                        + "@dre@them into a pure force for evil.% %"

                        + "@dre@This was the fantasy that chased him in his dreams. A%"
                        + "@dre@whole legion of soul-less beings, their minds demented%"
                        + "@dre@from the sheer power that he had channelled through to%"
                        + "@dre@them.%"
                        + "@dre@But dreams was all they ever were. As a mere mortal-%"
                        + "@dre@heroic though he was- this was an ambition that Iban was%"
                        + "@dre@unable to achieve. Meeting his demise in the White%"
                        + "@dre@Knights' now famous Dawn Ascent, Iban died with the%"
                        + "@dre@bitter taste of failure in his mouth. Little did he know that%"
                        + "@dre@his death was only the beginning.", true);
                }
            } else if(menu == 2) {
                p.getSender().sendAlert("% %@dre@I knew of Iban's life, though of course we had never met.% %"
                    + "@dre@And using the power of my dark arts, I vowed to% %"
                    + "@dre@resurrect this once great warrior. I would raise him again,% %"
                    + "@dre@to fulfill the promise of his human life: to be a% %"
                    + "@dre@Master of the Undead.% %", true);
            } else if(menu == 3) {
                p.message("there are four more chapters");
                String[] elementChapters = { "flesh", "blood", "shadow", "conscience" };
                int subMenu = showMenu(p, elementChapters);
                if(subMenu >= 0 && subMenu <= 3) {
                    message(p, "you turn to the page titled '" + elementChapters[subMenu] + "'");
                }
                if(subMenu == 0) {
                    p.getSender().sendAlert("@dre@Ibans Flesh% %"
                        + "@dre@Taking a small doll to represent Iban, I smeared my effigy% %"
                        + "@dre@with the four crucial elements that constitute a life.% %"
                        + "@dre@Rooting around the desolate battlefield, I had been able to% %"
                        + "@dre@steal a piece of Iban's cold flesh.% %"
                        + "@dre@Now clasping some in my own hand, I smeared it over% %"
                        + "@dre@my miniature idol, all the while chanting Iban's name.", true); 
                } else if(subMenu == 1) {
                    p.getSender().sendAlert("@dre@Ibans Blood% %"
                        + "@dre@I also needed some blood. By now, Iban's body was just a% %"
                        + "@dre@hardened vessel-his life blood had literally drained from% %"
                        + "@dre@him. But these caverns are home to the giant spider,% %"
                        + "@dre@a venomous creature that is known to feed on human% %"
                        + "@dre@blood. Killing one of these spiders, I wiped my carved doll% %"
                        + "@dre@in its blood.", true);
                } else if(subMenu == 2) {
                    p.getSender().sendAlert("@dre@Ibans Shadow% %"
                        + "@dre@Then came the hard part: recreating the parts of a man%"
                        + "@dre@that cannot be seen or touched: those intangible things%"
                        + "@dre@that are life itself. Using all the mystical force that I could%"
                        + "@dre@muster, I performed the ancient ritual of Incantia, a spell%"
                        + "@dre@so powerful that it nearly stole the life from my frail and%"
                        + "@dre@withered body. Opening my eyes again, I saw the three%"
                        + "@dre@demons that had been summoned. Standing in a triangle,%"
                        + "@dre@their energy was focused on the doll. These demons%"
                        + "@dre@would be the keepers of Iban's shadow. Black as night,%"
                        + "@dre@their shared spirit would follow his undead body like an%"
                        + "@dre@angel of death.", true);
                } else if(subMenu == 3) {
                    p.getSender().sendAlert("@dre@Ibans conscience% %"
                        + "@dre@Finally, I had to construct that most unique thing, the one%"
                        + "@dre@element which seperates man from every other beast- his%"
                        + "@dre@conscience. A zombie does not need a mind: his is a%"
                        + "@dre@mindless destruction, borne of simple bloodlust. But for all%"
                        + "@dre@of Iban's life, he himself choose to take the evil path-%"
                        + "@dre@driven by such monstrous ambition. This is what gave%"
                        + "@dre@him such potential- potential that I would now harness to%"
                        + "@dre@the fullest.", true);
                    int conscienceMenu1 = showMenu(p, "continue reading", "close book");
                    if(conscienceMenu1 == 0) {
                        p.getSender().sendAlert("% %@dre@Locked inside a old wooden cage sat a beautiful white%"
                            + "@dre@dove. A symbol of freedom and hope, but oblivious to the%"
                            + "@dre@darkness of the world- just like a newborn child. Taking it%"
                            + "@dre@from the cage, I cradled the creature in my hands,%"
                            + "@dre@stroking its downy feathers. With Iban's resurrection%"
                            + "@dre@almost complete, I looked into the bird's innocent blue%"
                            + "@dre@eyes. Placing a gentle kiss upon its head, I then strangled%"
                            + "@dre@the bird- extinguising its life between my pustulating%"
                            + "@dre@fingers. Truly this dead dove would now be Iban's%"
                            + "@dre@conscience: a mind that started with the innocence of%"
                            + "@dre@every other living creature, but chose to follow evil.", true);
                        int conscienceMenu2 = showMenu(p, "continue reading", "close book");
                        if(conscienceMenu2 == 0) {
                            p.getSender().sendAlert("% %@dre@Crumbling some of the dove's bones onto the doll, I cast%"
                                + "@dre@my mind's eye onto Iban's body. My ritual was complete,%"
                                + "@dre@soon he would be coming to life. I, Kardia, had resurrected%"
                                + "@dre@the legendary Iban, the most powerful evil being ever to%"
                                + "@dre@take human form. And I alone knew that the same%"
                                + "@dre@process that I had used to create him, was also capable%"
                                + "@dre@of destroying him.% %"

                                + "@dre@But now I was exhausted. As I closed my eyes to sleep, I%"
                                + "@dre@was settled by a strange feeling of contentment%"
                                + "@dre@anticipation of the evil that Iban would soon unleash.", true);
                        }
                    }
                }
            }
        }
        if(item.getID() == 1031) {
            message(p, "the staff is broken", "you must have a dark mage repair it");
            p.message("before it can be used");
        } 
        if(item.getID() == 1086) {
            p.message("You eat the nightshade...");
            removeItem(p, 1086, 1);
            playerTalk(p, null, "Ahhhh! what have I done !");
            p.damage((int) ((getCurrentLevel(p, HITS) * 0.2D) + 10));
            p.message("The nightshade was highly poisonous");
        } 
        if(item.getID() == 1087) {
            p.message("You search the robe");
            p.message("You find nothing");
        } 
        if(item.getID() == 1181) {
            if(p.getCache().hasKey("watchtower_scroll") && p.getQuestStage(WATCHTOWER) == -1) {
                return;
            }
            message(p, "You memorise what is written on the scroll");
            p.message("You can now cast the Watchtower teleport spell");
            p.message("Provided you have the required runes and magic level");
            p.message("The scroll crumbles to dust");
            removeItem(p, 1181, 1);
            if(!p.getCache().hasKey("watchtower_scroll")) {
                p.getCache().store("watchtower_scroll", true);
            }
        } 
        if(item.getID() == 1141) {
            p.getSender().sendAlert(
                    "Volatile chemicals - Notes on experimental chemistry. % %"
                            + "In order to ease the mining Process, my colleagues and I "
                            + "decided we needed something stronger than picks to delve "
                            + "under the digsite. As I already had an intermediate knowledge of "
                            + "herblaw, I experimented on certain chemicals, and invented a "
                            + "compound of tremendous power, which, if subjected to a spark "
                            + "would literally explode. We used vials of this compound with great results, "
                            + "as it enabled us to reach further than ever before. "
                            + "Here is what I have left of the compound's recipe: % %"
                            + "1 measure of ammonium nitrate powder,% "
                            + "1 measure of nitroglycerin,% "
                            + "1 measure of ground charcoal.% "
                            + "1 measure of ?% "
                            + "Unfortunately the last ingredient was not written down, but we "
                            + "understand that a certain root grows around these parts that was used to very good effect...", true);
        } 
        if(item.getID() == 1142) {
            playerTalk(p, null, "It says:", "The holder of this certificate has passed the level 1 exam in earth sciences");
        } 
        if(item.getID() == 1143) {
            playerTalk(p, null, "It says:",
                    "The holder of this certificate has passed the level 2 exam in earth sciences");
        } 
        if(item.getID() == 1144) {
            playerTalk(p, null, "It says:",
                    "The holder of this certificate has passed the level 3 exam in earth sciences");
        } 
        if(item.getID() == 1173) {
            playerTalk(p, null, "It says 'I give permission for the bearer to use the mineshafts on site",
                    "Signed Terrance Balando, Archaeological expert, City of Varrock");
        } 
        if(item.getID() == 1174) {
            playerTalk(p, null, "It says:",
                    "Tremble mortal, before the altar of our dread lord zaros");
        }
    }
}