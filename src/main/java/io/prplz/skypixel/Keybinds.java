package io.prplz.skypixel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

import java.time.Duration;

import static io.prplz.skypixel.Translation.*;

public class Keybinds {

    private final Skypixel skypixel;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final KeyBinding keyBindingMenu = new KeyBinding(KEY_MENU.key, 0, KEY_CATEGORIES_SKYPIXEL.key);
    private final KeyBinding keyBindingWarpHome = new KeyBinding(KEY_WARP_HOME.key, 0, KEY_CATEGORIES_SKYPIXEL.key);
    private final KeyBinding keyBindingWarpHub = new KeyBinding(KEY_WARP_HUB.key, 0, KEY_CATEGORIES_SKYPIXEL.key);
    private final KeyBinding keyBindingWarpGarden = new KeyBinding(KEY_WARP_GARDEN.key, 0, KEY_CATEGORIES_SKYPIXEL.key);
    private final KeyBinding keyBindingSetSpawn = new KeyBinding(KEY_SET_SPAWN.key, 0, KEY_CATEGORIES_SKYPIXEL.key);
    private final long commandCooldown = Duration.ofSeconds(1).toNanos();
    private long lastCommand = System.nanoTime();

    public Keybinds(Skypixel skypixel) {
        this.skypixel = skypixel;
    }

    public void register() {
        ClientRegistry.registerKeyBinding(keyBindingMenu);
        ClientRegistry.registerKeyBinding(keyBindingWarpHome);
        ClientRegistry.registerKeyBinding(keyBindingWarpHub);
        ClientRegistry.registerKeyBinding(keyBindingWarpGarden);
        ClientRegistry.registerKeyBinding(keyBindingSetSpawn);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean useCommand() {
        long now = System.nanoTime();
        if (now - lastCommand < commandCooldown) {
            return false;
        } else {
            lastCommand = now;
            return true;
        }
    }

    @SubscribeEvent
    public void onKeyboardInput(KeyInputEvent event) {
        if (mc.currentScreen != null || !skypixel.isInSkyblock()) {
            return;
        }

        while (keyBindingMenu.isPressed()) {
            if (useCommand()) {
                mc.thePlayer.sendChatMessage("/sbmenu");
            }
        }

        while (keyBindingWarpHome.isPressed()) {
            if (useCommand()) {
                mc.thePlayer.sendChatMessage("/warp home");
            }
        }

        while (keyBindingWarpHub.isPressed()) {
            if (useCommand()) {
                mc.thePlayer.sendChatMessage("/warp hub");
            }
        }

        while (keyBindingWarpGarden.isPressed()) {
            if (useCommand()) {
                mc.thePlayer.sendChatMessage("/warp garden");
            }
        }

        while (keyBindingSetSpawn.isPressed()) {
            if (useCommand()) {
                mc.thePlayer.sendChatMessage("/setspawn");
            }
        }
    }

    public boolean isAnyKeybindEnabled() {
        return keyBindingMenu.getKeyCode() != 0 || keyBindingWarpHome.getKeyCode() != 0 || keyBindingWarpHub.getKeyCode() != 0 || keyBindingWarpGarden.getKeyCode() != 0 || keyBindingSetSpawn.getKeyCode() != 0;
    }
}
