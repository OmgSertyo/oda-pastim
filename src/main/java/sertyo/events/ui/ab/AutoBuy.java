package sertyo.events.ui.ab;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.util.text.ITextComponent;
import obf.sertyo.nativeobf.Native;
import sertyo.events.event.misc.EventMessage;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.ui.ab.manager.*;

import java.util.List;
import java.util.stream.Collectors;

import static sertyo.events.utility.Utility.mc;
@Native
public class AutoBuy {
    public static long update;
    private long clickUptime;
    private boolean click;
    public static boolean send;
    public  AutoBuy() {
        EventManager.register(this);
    }
    public static boolean enabled = false;
    private boolean packet, packetSend;
    public long idkTime;

    private HistoryItem lastItem;
    public static String autoPayCom = "";
    public static int balance;

    @EventTarget
    private void onPacketEvent(EventPacket event) {
        if (idkTime > System.currentTimeMillis() && event.getPacket() instanceof CClickWindowPacket clickWindowPacket && clickWindowPacket.id != 1337) {
            event.setCancelled(true);
        }

        if (event.getPacket() instanceof CClickWindowPacket clickSlotC2SPacket) {
            if (packet && lastItem != null) {
                lastItem.stack = clickSlotC2SPacket.getClickedItem();
                packet = false;
                packetSend = true;
            }
        }
    }

    @EventTarget
    private void onReceiveMessageEvent(EventMessage event) {
        if (lastItem != null && packetSend) {
            if (event.getMessage().equals("Не так быстро!")) {
                lastItem = null;
                packetSend = false;
                return;
            }
            lastItem.purchased = !event.getMessage().contains("Этот предмет нельзя купить, т.к он больше не продается.");
            HistoryManager.add(lastItem);
            lastItem = null;
            packetSend = false;
        }
    }

    //hms-damage
    //hms-armor
    //hms-speed
    //hms-health

    @EventTarget
    private void onTick(EventUpdate event) {
        // for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            //     Multimap<Attribute, AttributeModifier> multimap = mc.player.getHeldItemMainhand().getAttributeModifiers(equipmentslottype);
            //     if (!multimap.isEmpty()) {
                //         for (Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                    //             System.out.println(entry.getKey().getAttributeName() + " " + entry.getValue().getAmount());
                    //         }
                //     }
            // }

        // if (!mc.player.getHeldItemMainhand().isEmpty()) {
            //     for (String s : mc.player.getHeldItemMainhand().getTag().keySet()) {
                //         System.out.println(s + " " + mc.player.getHeldItemMainhand().getTag().get(s).getString());
                //     }
            // }

        //for (AutoBuyItem item : AutoBuyManager.getItems()) {
            //        if (item instanceof CustomAutoBuyItem customAutoBuyItem) {
                //                if (customAutoBuyItem.tryBuy(mc.player.getHeldItemMainhand(), 1) || customAutoBuyItem.tryBuy(mc.player.getHeldItemOffhand(), 1)) {
                    //                          if (mc.player.ticksExisted % 8 == 0) {
                        //                      mc.ingameGUI.getChatGUI().printChatMessage(ITextComponent.getTextComponentOrEmpty("щас купил"));
                        //                  }
                    //              }
                //          }
            //      }
//
        //  if (!mc.player.isOnGround() && !mc.player.getHeldItemMainhand().isEmpty()) {
        //      // StringBuilder s = new StringBuilder();
        //      // for (ITextComponent iTextComponent : mc.player.getHeldItemMainhand().getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL)) {
        //          //     s.append(iTextComponent.getString()).append("\n");
        //          // }
        //      // List<String> list = new ArrayList<>();
        //      // for (INBT enchantment : mc.player.getHeldItemMainhand().getEnchantmentTagList()) {
        //      //     String tag = enchantment.toString().replace("{", "").replace("}", "");
        //      //     StringBuilder lvl = new StringBuilder();
        //      //     for (char c : tag.split(",")[0].toCharArray()) {
        //      //         try {
        //      //             lvl.append(Integer.parseInt(String.valueOf(c)));
        //      //         } catch (Exception ignored) {
        //      //         }
        //      //     }
        //      //     StringBuilder enchantName = new StringBuilder();
        //      //     boolean targ = false;
        //      //     for (char c : tag.split(",")[1].toCharArray()) {
        //      //         if (c == '\"') {
        //      //             if (!targ) {
        //      //                 targ = true;
        //      //                 continue;
        //      //             } else {
        //      //                 break;
        //      //             }
        //      //         }
        //      //         if (targ) {
        //      //             enchantName.append(c);
        //      //         }
        //      //     }
        //      // s.append(enchantName.toString().split(":")[1]).append(":").append(lvl);
        //    //  mc.keyboardListener.setClipboardString(s.toString());
        //  }

        if (mc.currentScreen == null || !enabled) return;
        Screen screen = mc.currentScreen;

        boolean ah = screen.getTitle().getString().contains("Аукцион");
        boolean accept = screen.getTitle().getString().contains("Покупка предмета");

        if (ah || accept) {
            if (mc.currentScreen instanceof ChestScreen chestScreen) {
                if (ah) {
                    if (mc.player.ticksExisted % 20 == 0) {
                        try {
                            String name = autoPayCom.split(" ")[0];
                            int s = Integer.parseInt(autoPayCom.split(" ")[1]);
                            if (balance - s > 0) {
                                mc.player.sendChatMessage("/pay " + name + " " + (balance - s));
                            }
                        } catch (Exception ignore) {
                        }
                    }
                    for (int i = 0; i < chestScreen.getContainer().getInventory().size(); i++) {
                        ItemStack stack = chestScreen.getContainer().getInventory().get(i);

                        String name = stack.getDisplayName().getString();

                        if (name.equals("Активные товары на продаже") || name.equals("Завершенные товары") || name.equals("Следующая страница ▶")
                                || name.contains("помощь по аукциону") || name.equals("как продать товар?") || name.equals("сортировка")
                                || name.equals("Категории предметорв") || name.equals("◀ Предыдущая страница"))
                            continue;

                        List<ITextComponent> info = screen.getTooltipFromItem(stack).stream().filter(text -> text.getString().contains("▍")).toList();
                        List<ITextComponent> sumList = info.stream().filter(text -> text.getString().contains("¤")).collect(Collectors.toList());

                        String author = "";
                        try {
                            author = info.stream().filter(text -> text.getString().contains("Продавец")).toList().get(0).getString().split(":")[1];
                        } catch (Exception ignore) {
                        }

                        if (IgnoreManager.getIgnore().contains(author.replace(" ", ""))) continue;

                        int[] sum = calcSum(sumList);

                        if (sum != null) {
                            int price = sum[0];
                            if (price <= 0) return;
                            for (AutoBuyItem item : AutoBuyManager.getItems()) {
                                if (item instanceof CustomAutoBuyItem) {
                                    if (((CustomAutoBuyItem) item).tryBuy(stack, price) && System.currentTimeMillis() > idkTime) {
                                        click(i, 1337);
                                        if (lastItem == null) {
                                            lastItem = new HistoryItem(price, stack);
                                        }
                                        packet = true;
                                        idkTime = System.currentTimeMillis() + 1000;
                                    }                                    if (((CustomAutoBuyItem) item).tryBuy(stack, price) && System.currentTimeMillis() > idkTime) {
                                        click(i, 1337);
                                        if (lastItem == null) {
                                            lastItem = new HistoryItem(price, stack);
                                        }
                                        packet = true;
                                        idkTime = System.currentTimeMillis() + 1000;
                                    }
                                }

                            }
                        }

                        if (stack.getItem().equals(Items.EMERALD) && name.equals("Обновить аукцион ↻") && i == 47 && System.currentTimeMillis() > update && !packet && System.currentTimeMillis() > idkTime) {
                            click(i, 0);
                            update = System.currentTimeMillis() + Minecraft.kd;
                        }
                    }
                }
                if (accept) {
                    if (click && System.currentTimeMillis() > clickUptime) {
                        click = false;
                    }
                    for (int i = 0; i < chestScreen.getContainer().getInventory().size(); i++) {
                        ItemStack stack = chestScreen.getContainer().getInventory().get(i);
                        String name = stack.getDisplayName().getString();
                        if (stack.getItem().equals(Items.GREEN_STAINED_GLASS_PANE) && name.contains("Купить") && !click) {
                            click(i, 0);
                            click = true;
                            clickUptime = System.currentTimeMillis() + 1000L;
                        }
                    }
                } else {
                    click = false;
                }
            }
        }
    }

    private void click(int slot, int id) {
        mc.playerController.windowClick(mc.player.openContainer.windowId, slot, 0, ClickType.PICKUP, mc.player, id);
    }

    private int[] calcSum(List<ITextComponent> s) {
        if (s == null || s.isEmpty()) return null;
        int i1 = -1;
        int i2 = -1;
        for (ITextComponent t : s) {
            String string = t.getString();
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : string.toCharArray()) {
                try {
                    stringBuilder.append(Integer.parseInt(String.valueOf(c)));
                } catch (Exception ignore) {}
            }
            if (!stringBuilder.toString().isEmpty()) {
                if (i1 < 0) {
                    i1 = Integer.parseInt(stringBuilder.toString());
                } else {
                    i2 = Integer.parseInt(stringBuilder.toString());
                }
            }
        }
        return new int[]{i1, i2};
    }
}
