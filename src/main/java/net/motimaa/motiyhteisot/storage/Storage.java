package net.motimaa.motiyhteisot.storage;

public interface Storage {

    void save(final Saveable saveable);
    void delete(final Saveable saveable);
    void load(boolean debug);

}
