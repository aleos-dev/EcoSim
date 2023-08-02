package com.empty.ecosim.model.plants;

import com.empty.ecosim.model.Edible;

public abstract class Plant implements Edible {
    protected int weight;
    public enum Type {GRASS}

    public abstract void reproduce();
}
