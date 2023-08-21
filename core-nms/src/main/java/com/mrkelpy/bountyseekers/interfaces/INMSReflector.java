package com.mrkelpy.bountyseekers.interfaces;

import java.io.DataInputStream;
import java.io.DataOutput;

public interface INMSReflector {

    Class<?> getNMSClass(String name);

    NBTTagCompound getNBTTagCompound();

    NBTCompressedStreamTools getNBTCompressedStreamTools();

    ItemStack getItemStack();


    abstract class NBTTagCompound {

        public abstract Object create(Object nmsItemStack);

        public abstract Object create();
    }

    abstract class NBTCompressedStreamTools {

        public abstract void write(Object nbtTagCompound, DataOutput dataOutput);

        public Object read(DataInputStream dataInputStream) {
            return null;
        }
    }

    abstract class ItemStack {

        public abstract Object create(Object nbtTagCompound);

        public abstract Object save(Object itemStack, Object nbtTagCompound);
    }


}

