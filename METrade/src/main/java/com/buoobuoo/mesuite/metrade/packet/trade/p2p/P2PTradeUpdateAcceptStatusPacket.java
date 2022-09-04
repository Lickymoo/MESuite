package com.buoobuoo.mesuite.metrade.packet.trade.p2p;

import com.buoobuoo.mesuite.melinker.redis.packet.MEPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class P2PTradeUpdateAcceptStatusPacket extends MEPacket {

    private final UUID uuid;
    private final boolean status;

}
