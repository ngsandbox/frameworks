package org.ngsandbox.concurrent.atomiccounter;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

class MyAtomicCounter extends AtomicInteger {

    private static Unsafe unsafe = null;

    static {
        Field unsafeField;
        try {
            unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AtomicInteger countIncrement = new AtomicInteger(0);

    MyAtomicCounter(int counter) {
        super(counter);
    }

    int myIncrementAndGet() {

        long valueOffset = 0L;
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        int v;
        do {
            v = unsafe.getIntVolatile(this, valueOffset);
            countIncrement.incrementAndGet();
        } while (!unsafe.compareAndSwapInt(this, valueOffset, v, v + 1));

        return v;
    }

    int getIncrements() {
        return this.countIncrement.get();
    }
}
