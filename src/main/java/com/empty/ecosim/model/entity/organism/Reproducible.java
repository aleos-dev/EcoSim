package com.empty.ecosim.model.entity.organism;

import java.util.Set;

public interface Reproducible {
   Set<? extends Organism> reproduce();
}
