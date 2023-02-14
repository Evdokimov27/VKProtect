package ru.evdokimov.vkprotect;

import java.io.File;
import java.io.IOException;

public class Storage {
    private File file;


    public Storage(String name) {
        file = new File(Main.getInstance().getDataFolder(), name);
        try {
            if (!file.exists() && !file.createNewFile()) throw new IOException();
        } catch (IOException e)
        {
            throw new RuntimeException("failed", e);
        }
    }


}
