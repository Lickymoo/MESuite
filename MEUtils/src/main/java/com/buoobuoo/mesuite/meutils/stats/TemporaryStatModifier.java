package com.buoobuoo.mesuite.meutils.stats;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class TemporaryStatModifier {
    private final Consumer<EntityStatInstance> instanceConsumer;
    private final Class<?> modifyingClass;

    public TemporaryStatModifier(Consumer<EntityStatInstance> instanceConsumer){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = null;
    }

    public TemporaryStatModifier(Consumer<EntityStatInstance> instanceConsumer, Class<?> clazz){
        this.instanceConsumer = instanceConsumer;
        this.modifyingClass = clazz;
    }
}
