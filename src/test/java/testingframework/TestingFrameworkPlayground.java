package testingframework;

import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.testing.*;
import net.minestom.testing.miniclient.MiniClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used to present a draft of how a Minestom testing framework could work.
 */
public class TestingFrameworkPlayground {

    @MinestomTest
    public void mySecondTest(TestEnvironment env) {
        System.out.println("hello");
    }

    @MinestomTest
    public void myTest(TestEnvironment env) {
        // Connects two clients
        MiniClient clientA = env.newClient();
        MiniClient clientB = env.newClient();

        // send a packet through client A
        ClientPlayerPositionPacket packet = new ClientPlayerPositionPacket();
        packet.x = 1;
        packet.y = 0;
        packet.z = 0;
        packet.onGround = true;
        clientA.sendPacket(packet);

        // Waits for all packets to be sent on the server
        env.waitNetworkIdle();

        // Checks that the second client received an EntityTeleportPacket and returns the first one
        //  should throw if 0 or > 1 are found
        /*EntityTeleportPacket teleportPacket = clientB.expectSingle(EntityTeleportPacket.class);

        // check the packet
        assertEquals(clientA.getEntityId(), teleportPacket.getId());
        assertEquals(1.0f, teleportPacket.position.getX(), 10e-16);
        assertEquals(0.0f, teleportPacket.position.getY(), 10e-16);
        assertEquals(0.0f, teleportPacket.position.getZ(), 10e-16);
        assertTrue(teleportPacket.onGround);*/
    }

    @MinestomTestCollection(value = "MyCollection", independent = true)
    public static class IndependentTests {

        private boolean beforeCalled = false;

        @BeforeEachMinestomTest
        public void before(TestEnvironment env) {
            beforeCalled = true;
        }

        @MinestomTest
        public void test1(TestEnvironment env) {
            assertTrue(beforeCalled);
            System.out.println("hello independent");
        }

        @MinestomTest
        public void test2(TestEnvironment env) {
            assertTrue(beforeCalled);
        }

        @AfterEachMinestomTest
        public void after(TestEnvironment env) {
            beforeCalled = false;
        }
    }
}