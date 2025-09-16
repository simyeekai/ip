package luke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void requireIndex_validWithinBounds_returnsIndex() {
        int size = 5;
        assertEquals(1, Parser.requireIndex("1", size));
        assertEquals(5, Parser.requireIndex(" 5 ", size));
    }

    @Test
    void requireIndex_zeroOrNegative_throws() {
        LukeException ex1 = assertThrows(LukeException.class, () -> Parser.requireIndex("0", 3));
        assertTrue(ex1.getMessage().toLowerCase().contains("range"));

        LukeException ex2 = assertThrows(LukeException.class, () -> Parser.requireIndex("-1", 3));
        assertTrue(ex2.getMessage().toLowerCase().contains("range"));
    }

    @Test
    void requireIndex_outOfBounds_throws() {
        LukeException ex = assertThrows(LukeException.class, () -> Parser.requireIndex("10", 2));
        assertTrue(ex.getMessage().toLowerCase().contains("range"));
    }

    @Test
    void requireIndex_notANumber_throws() {
        LukeException ex = assertThrows(LukeException.class, () -> Parser.requireIndex("abc", 2));
        assertTrue(ex.getMessage().toLowerCase().contains("valid"));
    }

    @Test
    void splitOnce_happyPath_splitsIntoTwo() {
        String[] parts = Parser.splitOnce("do thing /by 2019-12-02", "/by");
        assertEquals("do thing", parts[0]);
        assertEquals("2019-12-02", parts[1]);
    }

    @Test
    void splitOnce_missingDelimiter_secondEmpty() {
        String[] parts = Parser.splitOnce("do thing", "/by");
        assertEquals("do thing", parts[0]);
        assertEquals("", parts[1]);
    }

    @Test
    void splitTwo_happyPath_splitsIntoThree() {
        String[] parts = Parser.splitTwo("meet /from Mon 2pm /to 4pm", "/from", "/to");
        assertArrayEquals(new String[]{"meet", "Mon 2pm", "4pm"}, parts);
    }

    @Test
    void splitTwo_missingPieces_blanksAsSpecified() {
        String[] parts = Parser.splitTwo("meet only from", "/from", "/to");
        assertEquals(3, parts.length);
        assertEquals("meet only from", parts[0]);
        assertEquals("", parts[1]);
        assertEquals("", parts[2]);
    }
}
