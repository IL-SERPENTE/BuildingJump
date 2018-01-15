package eu.carrade.amaury.samagames.buildingjump.utils;

import junit.framework.TestCase;


public class UtilsTest extends TestCase
{
    public void testGetNextElement() throws Exception
    {
        assertEquals(TestEnum.VALUE_0, Utils.getNextElement(TestEnum.VALUE_0,  0));
        assertEquals(TestEnum.VALUE_1, Utils.getNextElement(TestEnum.VALUE_0,  1));
        assertEquals(TestEnum.VALUE_4, Utils.getNextElement(TestEnum.VALUE_0, -1));

        assertEquals(TestEnum.VALUE_0, Utils.getNextElement(TestEnum.VALUE_0, 5));
        assertEquals(TestEnum.VALUE_2, Utils.getNextElement(TestEnum.VALUE_0, 2));
        assertEquals(TestEnum.VALUE_3, Utils.getNextElement(TestEnum.VALUE_0, 8));

        assertEquals(TestEnum.VALUE_0, Utils.getNextElement(TestEnum.VALUE_0, -5));
        assertEquals(TestEnum.VALUE_2, Utils.getNextElement(TestEnum.VALUE_0, -8));

        assertEquals(TestSingleEnum.VALUE_0, Utils.getNextElement(TestSingleEnum.VALUE_0,  0));
        assertEquals(TestSingleEnum.VALUE_0, Utils.getNextElement(TestSingleEnum.VALUE_0,  1));
        assertEquals(TestSingleEnum.VALUE_0, Utils.getNextElement(TestSingleEnum.VALUE_0, -1));
        assertEquals(TestSingleEnum.VALUE_0, Utils.getNextElement(TestSingleEnum.VALUE_0,  4));
        assertEquals(TestSingleEnum.VALUE_0, Utils.getNextElement(TestSingleEnum.VALUE_0, -4));
    }

    private enum TestEnum
    {
        VALUE_0, VALUE_1, VALUE_2, VALUE_3, VALUE_4
    }

    private enum TestSingleEnum
    {
        VALUE_0
    }
}
