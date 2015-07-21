package common;

import org.junit.Before;

import play.test.Fixtures;
import play.test.FunctionalTest;

public abstract class TestCommon extends FunctionalTest {
	protected CommonApi commonApi = new CommonApi();

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.executeSQL("alter table users alter column id restart with 1");
		Fixtures.executeSQL("alter table devices alter column id restart with 1");
		Fixtures.executeSQL("alter table teams alter column id restart with 1");
		Fixtures.executeSQL("alter table posts alter column id restart with 1");
		Fixtures.executeSQL("alter table comments alter column id restart with 1");
		Fixtures.loadModels("data.yml");
	}
}
