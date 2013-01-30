package nc.isi.fragaria_reflection.utils;

import junit.framework.TestCase;
import nc.isi.fragaria_reflection.utils.FragariaFileUtils;

import org.junit.Test;

public class FileUtilsTest extends TestCase {
	private static final String FULL_FILE_NAME = "test.extension";
	private static final String FINAL_FILE_NAME = "test";

	@Test
	public void testRemoveExtension() {
		assertEquals("l'extension n'a pas été retirée correctement",
				FINAL_FILE_NAME,
				FragariaFileUtils.removeExtension(FULL_FILE_NAME));
	}

}
