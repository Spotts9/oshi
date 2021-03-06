/**
 * MIT License
 *
 * Copyright (c) 2010 - 2020 The OSHI Project Contributors: https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package oshi.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/*
 * Tests EdidUtil
 */
public class EdidUtilTest {

    private final static String EDID_HEADER = "00FFFFFFFFFFFF00";
    private final static String EDID_MANUFID = "0610";
    private final static String EDID_PRODCODE = "2792";
    private final static String EDID_SERIAL = "250C2C16";
    private final static String EDID_WKYR = "2C16";
    private final static String EDID_VERSION = "0104";
    private final static String EDID_VIDEO = "B53C2278226FB1A7554C9E250C5054000000";
    private final static String EDID_TIMING = "01010101010101010101010101010101";
    private final static String EDID_DESC1 = "565E00A0A0A029503020350055502100001A";
    private final static String EDID_DESC2 = "1A1D008051D01C204080350055502100001C";
    private final static String EDID_DESC3 = "000000FF004330324A4D325046463247430A";
    private final static String EDID_DESC4 = "000000FC005468756E646572626F6C740A20";
    private final static String EDID_DESC5 = "000000FA004330324A4D325046463247430A";
    private final static String EDID_DESC6 = "000000FB005468756E646572626F6C740A20";
    private final static String EDID_DESC7 = "000000FD004330324A4D325046463247430A";
    private final static String EDID_DESC8 = "000000FE005468756E646572626F6C740A20";
    private final static String EDID_EXTS = "01";
    private final static String EDID_CHKSUM = "C7";
    private final static String EDID_STR = EDID_HEADER + EDID_MANUFID + EDID_PRODCODE + EDID_SERIAL + EDID_WKYR
            + EDID_VERSION + EDID_VIDEO + EDID_TIMING + EDID_DESC1 + EDID_DESC2 + EDID_DESC3 + EDID_DESC4 + EDID_EXTS
            + EDID_CHKSUM;
    private final static String EDID_STR2 = EDID_HEADER + EDID_MANUFID + EDID_PRODCODE + EDID_SERIAL + EDID_WKYR
            + EDID_VERSION + EDID_VIDEO + EDID_TIMING + EDID_DESC5 + EDID_DESC6 + EDID_DESC7 + EDID_DESC8 + EDID_EXTS
            + EDID_CHKSUM;
    private final static byte[] EDID = ParseUtil.hexStringToByteArray(EDID_STR);

    @Test
    public void testGetEdidAttrs() {
        assertEquals("Parse manufacturerId.", "A", EdidUtil.getManufacturerID(EDID));
        assertEquals("Parse productId.", "9227", EdidUtil.getProductID(EDID));
        assertEquals("Parse serialNo.", "162C0C25", EdidUtil.getSerialNo(EDID));
        assertEquals("Parse week.", (byte) 44, EdidUtil.getWeek(EDID));
        assertEquals("Parse year.", 2012, EdidUtil.getYear(EDID));
        assertEquals("Parse version.", "1.4", EdidUtil.getVersion(EDID));
        assertTrue("Parse \"isDigital\".", EdidUtil.isDigital(EDID));
        assertEquals("Parse hcm.", 60, EdidUtil.getHcm(EDID));
        assertEquals("Parse vcm.",34, EdidUtil.getVcm(EDID));
    }

    @Test
    public void testGetDescriptors() {
        byte[][] descs = EdidUtil.getDescriptors(EDID);
        for (int i = 0; i < 4; i++) {
            int type = EdidUtil.getDescriptorType(descs[i]);
            String timing = EdidUtil.getTimingDescriptor(descs[i]);
            String range = EdidUtil.getDescriptorRangeLimits(descs[i]);
            switch (i) {
            case 0:
                assertEquals("Parse first type.",0x565E00A0, type);
                assertEquals("Parse first timing.","Clock 241MHz, Active Pixels 2560x1440 ", timing);
                assertEquals("Parse first range.","Field Rate -96-41 Hz vertical, 80-48 Hz horizontal, Max clock: 320 MHz", range);
                break;
            case 1:
                assertEquals("Parse second type.",0x1A1D0080, type);
                assertEquals("Parse second timing.","Clock 74MHz, Active Pixels 1280x720 ", timing);
                assertEquals("Parse second range.","Field Rate -48-28 Hz vertical, 32-64 Hz horizontal, Max clock: -1280 MHz", range);
                break;
            case 2:
                assertEquals("Parse third type.",0xFF, type);
                assertEquals("Parse third descriptorText.","C02JM2PFF2GC", EdidUtil.getDescriptorText(descs[i]));
                assertEquals("Parse third descriptors completely.",EDID_DESC3, ParseUtil.byteArrayToHexString(descs[i]));
                break;
            case 3:
                assertEquals("Parse fourth type.",0xFC, type);
                assertEquals("Parse fourth descriptorText.","Thunderbolt", EdidUtil.getDescriptorText(descs[i]));
                break;
            default:
            }
        }
    }

    @Test
    public void testToString() {
        String[] toString = EdidUtil.toString(EDID).split("\\n");
        assertEquals("Parse edId toString.",6, toString.length);
        toString = EdidUtil.toString(ParseUtil.hexStringToByteArray(EDID_STR2)).split("\\n");
        assertEquals("Parse edId2 toString.",6, toString.length);
    };
}
