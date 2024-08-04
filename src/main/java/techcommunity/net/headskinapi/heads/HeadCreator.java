package techcommunity.net.headskinapi.heads;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import techcommunity.net.headskinapi.Colors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HeadCreator {
    private ItemStack head;

    private String name;
    private List<String> lore;
    private String texture;

    private HeadCreator() {
        this(new ItemStack(Material.PLAYER_HEAD, 1));
    }

    private HeadCreator(ItemStack head) {
        this.head = head;
        this.lore = new ArrayList<>();
    }

    public static HeadCreator builder() {
        return new HeadCreator();
    }

    public static HeadCreator edit(ItemStack head) {
        return new HeadCreator(head);
    }

    public HeadCreator setTexture(String texture) {
        this.texture = texture;
        return this;
    }

    public HeadCreator setName(String name) {
        this.name = name;
        return this;
    }

    public HeadCreator setLore(String... loreLines) {
        this.lore = List.of(loreLines);
        return this;
    }

    public HeadCreator addLore(String loreLine) {
        this.lore.add(loreLine);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getTexture() {
        return texture;
    }

    public ItemStack build() {
        if (texture != null)
        {
            SkullMeta skullMeta = (SkullMeta) head.getItemMeta();

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().put("textures", new Property("textures", texture));

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalAccessException ignored) { }

            head.setItemMeta(skullMeta);
        }

        ItemMeta headMeta = head.getItemMeta();

        if (headMeta != null)
        {
            if (name != null)
                headMeta.setDisplayName(Colors.color(name));
            if (lore != null) {
                lore = lore.stream().map(Colors::color).collect(Collectors.toList());
                headMeta.setLore(lore);
            }
        }

        head.setItemMeta(headMeta);

        return head;
    }
}
