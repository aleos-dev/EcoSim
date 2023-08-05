package com.empty.ecosim.model.island;

import java.util.ArrayList;
import java.util.List;

public abstract class Territory {
    List<Cell> cells = new ArrayList<>();


    public abstract void clean();
}
