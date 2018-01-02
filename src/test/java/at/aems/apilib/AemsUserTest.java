package at.aems.apilib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AemsUserTest {
	
	private AemsUser testUser = new AemsUser(0, "Test", "user");
	
	@Test
	public void testAuthStringWithSalt() {
		String salt = "salt";
		String expected = "6d7dbc8e8a1fcee06432a3b05abdeb4746961db75ca6a4c5b9fb0876f9e73c55e6935634398fa255d411382101b93f3694a916ade91626e2b46254bb4d10d77b";
		assertEquals("authentication strings should be equal", expected, testUser.getAuthString(salt));
	}
	
	@Test
	public void testAuthStringWithoutSalt() {
		String expected = "52881845c5c1db6586bd7f1297ff1bdb39d12937bc339cab950cb3818346e473c2d3c9cdb2da7dc89a8e39cb036d72302011dc6ae89914455e4e6f013523d727";
		assertEquals("authentication string should be equal", expected, testUser.getAuthString(null));
		assertEquals("authentication string should be equal", expected, testUser.getAuthString(""));
	}
	
	@Test
	public void testUserCredentials() {
		assertEquals(0, testUser.getUserId());
		assertEquals("Test", testUser.getUsername());
		assertEquals("user", testUser.getPassword());
	}
}
