package nc.isi.fragaria_reflection.services;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;
import nc.isi.fragaria_reflection.QaRegistry;

import org.junit.Test;

public class ResourceFinderImplTest extends TestCase {
	private ReflectionFactory reflectionFactory;
	private static final String TEST_REGEXP = ".*\\.yaml";
	private static final String[] packages = { "nc.isi.fragaria_reflection" };

	@Override
	protected void setUp() throws Exception {
		reflectionFactory = QaRegistry.INSTANCE.getRegistry().getService(
				ReflectionFactory.class);
	}

	@Test
	public void testGetRessourcesMatching() {
		ResourceFinder resourceFinder = new ResourceFinderImpl(
				reflectionFactory, Arrays.asList(packages));
		Set<File> files = resourceFinder.getResourcesMatching(TEST_REGEXP);
		assertNotNull("ne doit jamais retourner une liste null", files);
		assertTrue("aucun fichier trouvé", files.size() > 0);
		for (File file : files) {
			assertTrue("erreur de matching", file.getName()
					.matches(TEST_REGEXP));
		}
	}

}
