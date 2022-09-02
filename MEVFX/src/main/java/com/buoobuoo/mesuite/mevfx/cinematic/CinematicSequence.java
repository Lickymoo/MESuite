package com.buoobuoo.mesuite.mevfx.cinematic;


import com.buoobuoo.mesuite.mevfx.MEVFXPlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

@Setter
@Getter
public class CinematicSequence {
    private final MEVFXPlugin plugin;

    public List<CinematicFrame> frames;
    public boolean exitFinish;
    public Consumer<Player> onFinish;

    public CinematicSequence(MEVFXPlugin plugin, boolean exitFinish, CinematicFrame... frames) {
        this.plugin = plugin;
        this.frames = Arrays.asList(frames);
        this.exitFinish = exitFinish;
    }

    public CinematicSequence(MEVFXPlugin plugin, CinematicFrame... frames) {
        this.plugin = plugin;
        this.frames = Arrays.asList(frames);
        this.exitFinish = false;
    }

    public void execute(Player player) {
        Deque<CinematicFrame> iterator = new ArrayDeque<>(frames);
        Bukkit.getScheduler().runTaskLater(plugin, () -> nextFrame(player, iterator), iterator.peek().delay);
    }

    public void nextFrame(Player player, Deque<CinematicFrame> iterator) {
        CinematicFrame frame = iterator.pop();
        frame.getFunction().accept(player);
        if(!iterator.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> nextFrame(player, iterator), iterator.peek().delay);
        }else {
            if(onFinish != null)
                onFinish.accept(player);
            if(exitFinish)
                plugin.getSpectatorManager().stopSpectatorMode(player);
        }
    }

    public static CinematicFrame[] mergeArrays(CinematicFrame[] ...arrays)
    {
        return Stream.of(arrays)
                .flatMap(Stream::of)
                .toArray(CinematicFrame[]::new);
    }

}

