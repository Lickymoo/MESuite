package com.buoobuoo.mesuite.melinker.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MELoc {
    private String worldName;

    private double x;
    private double y;
    private double z;

    private float pitch;
    private float yaw;
}
