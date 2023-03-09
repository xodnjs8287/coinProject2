package com.example.coinProject.common;

import java.util.List;
import lombok.Data;

@Data(staticConstructor = "of")
public class Type {

    private final TradeType type;
    private final List<String> codes;
    private Boolean isOnlySnapShot = false;
    private Boolean isOnlyRealTime = true;
}
