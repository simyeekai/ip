package duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {

    @Test
    void toString_withIsoDate_formatsAsMonthName() {
        Deadline d = new Deadline("return book", LocalDate.parse("2019-12-02")); // Dec 2 2019
        String s = d.toString();
        assertTrue(s.contains("[D][ ] return book"));
        assertTrue(s.contains("Dec 2 2019"));
        assertTrue(d.hasDate());
        assertEquals("2019-12-02", d.getByIsoOrText());
    }

    @Test
    void toString_withFreeText_keepsTextAndNoDate() {
        Deadline d = new Deadline("return book", "Sunday");
        String s = d.toString();
        assertTrue(s.contains("[D][ ] return book"));
        assertTrue(s.contains("Sunday"));
        assertFalse(d.hasDate());
        assertEquals("Sunday", d.getByIsoOrText());
    }
}
