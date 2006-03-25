package net.jsunit.action;

import net.jsunit.SkinSource;
import net.jsunit.results.Skin;

public interface SkinAware {

    public void setSkin(Skin skin);

    public Skin getSkin();

    SkinSource getSkinSource();
}
