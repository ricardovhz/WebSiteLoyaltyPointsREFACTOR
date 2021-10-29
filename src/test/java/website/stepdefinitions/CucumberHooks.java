package website.stepdefinitions;

import java.util.Collection;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class CucumberHooks {

	@Before
	public void setUp(Scenario scenario) throws Exception {

		Collection<String> tags = scenario.getSourceTagNames();
		System.out.println("At Hooks: Setup: " + scenario.getName());

		tags.forEach(tag -> {

			System.out.println("Tags: " + tag);

		});

	}

	@After
	public void tearDown(Scenario scenario) throws Exception {
		System.out.println("At Hooks: TearDown: " + scenario.getName());
	}

}
