package com.example.myprojectbackend.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class StringUtilTest {
    @Test
    public void isEmpty_NullString_ReturnsTrue() {
        assertTrue(StringUtil.isEmpty(null));
    }

    @Test
    public void isEmpty_EmptyString_ReturnsTrue() {
        assertTrue(StringUtil.isEmpty(""));
    }

    @Test
    public void isEmpty_NonEmptyString_ReturnsFalse() {
        assertFalse(StringUtil.isEmpty("Hello"));
    }

    @Test
    public void isNotEmpty_NullString_ReturnsFalse() {
        assertFalse(StringUtil.isNotEmpty(null));
    }

    @Test
    public void isNotEmpty_EmptyString_ReturnsFalse() {
        assertFalse(StringUtil.isNotEmpty(""));
    }

    @Test
    public void isNotEmpty_NonEmptyString_ReturnsTrue() {
        assertTrue(StringUtil.isNotEmpty("Hello"));
    }

    @Test
    public void formatLike_EmptyString_ReturnsNull() {
        assertNull(StringUtil.formatLike(""));
    }

    @Test
    public void formatLike_NonEmptyString_ReturnsFormattedString() {
        assertEquals("%Hello%", StringUtil.formatLike("Hello"));
    }

    @Test
    public void filterWhite_EmptyList_ReturnsEmptyList() {
        assertEquals(0, StringUtil.filterWhite(new ArrayList<>()).size());
    }

    @Test
    public void filterWhite_NonEmptyList_ReturnsFilteredList() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add(" ");
        list.add("World");
        assertEquals(2, StringUtil.filterWhite(list).size());
    }



    @Test
    public void genSixRandomNum_NoArgs_ReturnsSixDigitRandomNumber() {
        String randomNum = StringUtil.genSixRandomNum();
        assertTrue(randomNum.length() == 6);
    }

    @Test
    public void getRandomString_Length5_ReturnsRandomString() {
        String randomString = StringUtil.getRandomString(5);
        assertTrue(randomString.length() == 5);
    }

    @Test
    public void modifyString_InputWithSlash_RemovesSlash() {
        assertEquals("/Hello", StringUtil.modifyString("Hello/", "M"));
    }

    @Test
    public void modifyString_InputWithoutSlash_AddsSlash() {
        assertEquals("/Hello", StringUtil.modifyString("Hello", "M"));
    }

    @Test
    public void modifyString_InputWithIndex_RemovesIndex() {
        assertEquals("/Hello", StringUtil.modifyString("Hello/index", "C"));
    }

    @Test
    public void genurateUserId_NoArgs_ReturnsRandomUserId() {
        String userId = StringUtil.genurateUserId();
        assertTrue(userId.length() == 32);
    }
}
