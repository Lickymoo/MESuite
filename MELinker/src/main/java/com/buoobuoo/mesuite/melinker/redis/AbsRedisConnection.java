package com.buoobuoo.mesuite.melinker.redis;

import redis.clients.jedis.Jedis;

import java.util.function.Consumer;

public interface AbsRedisConnection {
    public void execute(Consumer<Jedis> consumer);

    public void executeAsync(Consumer<Jedis> consumer);
}
