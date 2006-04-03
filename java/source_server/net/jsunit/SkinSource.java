package net.jsunit;

import net.jsunit.results.Skin;

import java.util.List;

public interface SkinSource {
    List<Skin> getSkins();

    Skin getSkinById(int skinId);
}
