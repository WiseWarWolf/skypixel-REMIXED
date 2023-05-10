package io.prplz.skypixel.gui.settings;

import com.google.common.collect.ObjectArrays;
import io.prplz.skypixel.Settings;
import io.prplz.skypixel.Skypixel;
import io.prplz.skypixel.gui.BooleanPropertyButton;
import io.prplz.skypixel.gui.ChatColorPropertyPicker;
import io.prplz.skypixel.gui.EnumPropertyButton;
import io.prplz.skypixel.gui.Image;
import io.prplz.skypixel.gui.Label;
import io.prplz.skypixel.gui.Pane;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static io.prplz.skypixel.Translation.*;

public class SettingsGui extends GuiScreen {

    private final Skypixel skypixel = Skypixel.get();
    private final GuiScreen parent;
    private FeatureList featureList;
    private final List<Feature> features = new ArrayList<>();
    private Pane rightPane;

    public SettingsGui(GuiScreen parent) {
        this.parent = parent;
        Settings settings = skypixel.getSettings();
        features.add(new Feature(
                FEATURE_ENCHANTMENT_MENU.format(),
                Feature.enabledIf(settings.enchantmentGuiEnabled),
                new FeaturePane(
                        new BooleanPropertyButton(settings.enchantmentGuiEnabled, FEATURE_ENCHANTMENT_MENU_ENABLED::format, 200),
                        Label.of("Replaces Hypixel's enchantment table menu with a custom menu that looks similar to the one in regular Minecraft."),
                        new Image(new ResourceLocation("skypixel", "screenshots/enchant.png"))
                )
        ));
        features.add(new Feature(
                FEATURE_ANVIL_USES.format(),
                Feature.enabledIf(settings.anvilUsesEnabled),
                new FeaturePane(
                        new BooleanPropertyButton(settings.anvilUsesEnabled, FEATURE_ANVIL_USES_ENABLED::format, 200),
                        new EnumPropertyButton<>(settings.anvilUsesPosition, Settings.AnvilUsesPosition.class, FEATURE_ANVIL_USES_POSITION::format, 200),
                        new ChatColorPropertyPicker(settings.anvilUsesColor, FEATURE_TEXT_COLOR.format()),
                        Label.of("Shows the item's anvil uses in the tooltip."),
                        Label.of("A higher anvil use count incurs a higher cost when combining items in an anvil."),
                        new Image(new ResourceLocation("skypixel", "screenshots/anvil_uses.png"))
                )
        ));
        features.add(new Feature(
                FEATURE_ITEM_TIER.format(),
                Feature.enabledIf(() -> settings.minionItemTierEnabled.get() || settings.potionItemTierEnabled.get() || settings.armorItemTierEnabled.get()),
                new FeaturePane(
                        new BooleanPropertyButton(settings.minionItemTierEnabled, FEATURE_ITEM_TIER_MINION::format, 200),
                        new BooleanPropertyButton(settings.potionItemTierEnabled, FEATURE_ITEM_TIER_POTION::format, 200),
                        new BooleanPropertyButton(settings.armorItemTierEnabled, FEATURE_ITEM_TIER_ARMOR::format, 200),
                        new EnumPropertyButton<>(settings.itemTierType, Settings.NumeralType.class, FEATURE_NUMERAL_TYPE::format, 200),
                        new ChatColorPropertyPicker(settings.itemTierColor, FEATURE_TEXT_COLOR.format())
                )
        ));
        features.add(new Feature(
                FEATURE_HIDE_PLAYERS.format(),
                Feature.enabledIf(settings.hidePlayersNearNpcs),
                new FeaturePane(
                        new BooleanPropertyButton(settings.hidePlayersNearNpcs, FEATURE_HIDE_PLAYERS_NEAR_NPCS::format, 200)
                )
        ));
        features.add(new Feature(
                FEATURE_TAB_LIST_TWEAKS.format(),
                Feature.enabledIf(settings.hideNpcsOnTab),
                new FeaturePane(
                        new BooleanPropertyButton(settings.hideNpcsOnTab, FEATURE_HIDE_NPCS_ON_TAB::format, 200)
                )
        ));
        features.add(new Feature(
                FEATURE_HITBOXES.format(),
                Feature.enabledIf(() -> settings.mobHitboxes.get() || settings.playerHitboxes.get()),
                new FeaturePane(
                        new BooleanPropertyButton(settings.mobHitboxes, FEATURE_MOB_HITBOXES::format, 200),
                        new BooleanPropertyButton(settings.playerHitboxes, FEATURE_PLAYER_HITBOXES::format, 200)
                )
        ));
        features.add(new Feature(
                FEATURE_HIDE_END_PORTALS.format(),
                Feature.enabledIf(settings.hideEndPortals),
                new FeaturePane(
                        new BooleanPropertyButton(settings.hideEndPortals, FEATURE_HIDE_END_PORTALS_ENABLED::format, 200),
                        Label.of("Hides all end portal blocks, which are known to reduce frame rate."),
                        new Image(new ResourceLocation("skypixel", "screenshots/hide_end_portal.png"))
                )
        ));
        features.add(new Feature(
                FEATURE_CANCEL_ITEM_DAMAGE.format(),
                Feature.enabledIf(settings.cancelItemDamage),
                new FeaturePane(
                        new BooleanPropertyButton(settings.cancelItemDamage, FEATURE_CANCEL_ITEM_DAMAGE_ENABLED::format, 200),
                        Label.of("Prevents the game client from trying to apply damage to tools and weapons."),
                        Label.of("This fixes some buggy behaviour caused by the server constantly updating your items.")
                )
        ));
        features.add(new Feature(
                FEATURE_CANCEL_INVENTORY_DRAG.format(),
                Feature.enabledIf(settings.cancelInventoryDrag),
                new FeaturePane(
                        new BooleanPropertyButton(settings.cancelInventoryDrag, FEATURE_CANCEL_INVENTORY_DRAG_ENABLED::format, 200),
                        Label.of("Disables the item dragging mechanic in a few affected Hypixel menus that don't properly support it, making them less glitchy."),
                        Label.of("This feature may be be removed once Hypixel fixes the bug on their end."),
                        Label.of("Affected menus: Enchantment, Brewing Stand, Anvil, Minions, Runic Pedestal and Reforging.")
                )
        ));
        features.add(new Feature(
                FEATURE_KEYBINDS.format(),
                Feature.enabledIf(skypixel.getKeybinds()::isAnyKeybindEnabled),
                new FeaturePane(
                        Label.of("Keybinds can be configured in Minecraft's controls menu.")
                )
        ));
        features.add(new Feature(
                FEATURE_CLIENT_COMMAND_FIX.format(),
                Feature.enabledIf(settings.forgeClientCommandFix),
                new FeaturePane(
                        new BooleanPropertyButton(settings.forgeClientCommandFix, FEATURE_CLIENT_COMMAND_FIX_ENABLED::format, 200),
                        Label.of("Applies a fix for Forge client commands intercepting chat which does not start with a forward slash."),
                        Label.of("For example, without this fix you can't send a chat message starting with \"skypixel\".")
                )
        ));
        featureList = new FeatureList(this, features);

        Label[] labels = new Label[]{Label.of("Select a feature on the left to get started.")};
        if (parent != null) {
            labels = ObjectArrays.concat(labels, Label.of("You can access this menu in-game using the \u00a7e/skypixel\u00a7r command."));
        }
        rightPane = new FeaturePane(labels);
    }

    @Override
    public void initGui() {
        featureList.setPosition(10, 10);
        featureList.setSize(150, height - 20);
        rightPane.setPosition(featureList.getWidth() + 20, 40);
        rightPane.setSize(width - featureList.getWidth() - 30, height - 50);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glTranslatef((featureList.getWidth() + 10 + width) / 2.0f, 10.0f, 0.0f);
        GL11.glScalef(2.0f, 2.0f, 1.0f);
        drawCenteredString(fontRendererObj, "Skypixel", 0, 0, 0xFFFFFF);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        featureList.draw(mouseX, mouseY, partialTicks);
        rightPane.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        featureList.update();
        rightPane.update();
    }

    @Override
    public void handleMouseInput() {
        featureList.handleMouseInput();
        rightPane.handleMouseInput();
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    public void setSelectedFeature(Feature feature) {
        rightPane = feature.getGui();
        initGui();
        skypixel.trySaveSettings("Failed to write " + skypixel.getSettingsFile());
    }

    @Override
    public void onGuiClosed() {
        skypixel.trySaveSettings("Failed to write " + skypixel.getSettingsFile());
    }
}
