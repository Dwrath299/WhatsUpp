package edu.byui.whatsupp;

/**
 * Created by MikeyG on 2/28/2018.
 */

import org.junit.Test;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Unit4Test {

    @Test
    public void yo() {
        assertThat(ThingToDo.yoWaddup(), is(true));
    }
}
