package it.polimi.ingsw.model.goal;

import junit.framework.TestCase;

import java.io.IOException;

public class GoalBufferTest extends TestCase {

    public void testGetgoal() throws IOException {
        GoalBuffer g=new GoalBuffer();
        assertNotNull(g.getgoal());
    }
}