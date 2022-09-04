package com.buoobuoo.mesuite.melinker.redis.packet.impl.net.playerdata.virtualplayer;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.Getter;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Getter
public class PlayerEquipmentPacket extends MEPacket {
    private final UUID uuid;

    private final String equipmentSlot;
    private final String itemStack; //base 64

    public PlayerEquipmentPacket(UUID uuid, String slot, ItemStack itemStack){
        this.uuid = uuid;
        this.equipmentSlot = slot;
        this.itemStack = serialize(itemStack);
    }

    public ItemStack getItemStack(){
        return deserialize(itemStack);
    }


    private String serialize(ItemStack obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(obj);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    private ItemStack deserialize(String str) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(str));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (Exception e) {
            return null;
        }
    }
}