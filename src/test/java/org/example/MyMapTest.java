package org.example;

import junit.framework.TestCase;
import java.util.Random;



public class MyMapTest extends TestCase {



    public void testGet() {
        MyMap<String, String> map = new MyMap<>(10);

        int putNumber = 50000;                      // Кладем 50 000 значений
        for (int i = 0; i < putNumber; i++) {       //
            map.put("_" + i + "_", "VALUE_" + i);   //
        }

        Random random = new Random();

        // Достаем случайное значение 10 000 раз
        for (int i = 0; i < 10000; i++) {
            int randInt = random.nextInt(putNumber);
            String actual = map.get("_" + randInt + "_");
            String expected = "VALUE_" + randInt;

            assertEquals(expected, actual);
        }
    }




    public void testPutAndRemove() {
        MyMap<String, String> map = new MyMap<>(100);

        int putNumber = 50000;                      // Кладем 50 000 значений
        for (int i = 0; i < putNumber; i++) {       //
            map.put("_" + i + "_", "VALUE_" + i);   //
        }

        for (int i = 0; i < putNumber; i += 2) {       // Удаляем четные
            map.remove("_" + i + "_");             //
        }

        for (int i = 0; i < putNumber; i += 2) {       // Получаем четные
            String actual = map.get("_" + i + "_");    //
            assertNull(actual);                        //
        }

        for (int i = 1; i < putNumber; i += 2) {       // Получаем нечетные
            String actual = map.get("_" + i + "_");    //
            String expected = "VALUE_" + i;            //
            assertEquals(expected, actual);            //
        }
    }



    public void testGetSize() {
        MyMap<String, String> map = new MyMap<>(100);

        int putNumber = 50000;                      // Кладем 50 000 значений
        for (int i = 0; i < putNumber; i++) {       //
            map.put("_" + i + "_", "VALUE_" + i);   //
        }

        int actual = map.getSize();
        int expected = putNumber;

        assertEquals(expected, actual);
    }


}