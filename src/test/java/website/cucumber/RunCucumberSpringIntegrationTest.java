package website.cucumber;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

// https://thepracticaldeveloper.com/cucumber-tests-spring-boot-dependency-injection/
// extraGlue = "io.tpd.springbootcucumber.bagcommons"

@RunWith(Cucumber.class)
@CucumberOptions(glue = "website.stepdefinitions", dryRun = false, monochrome = false, tags = "not @ignored ", features = "src/test/resources/features", plugin = {
		"pretty", "html:target/cucumber/backend", "json:target/cucumber/cucumber.json" }, publish = true)
public class RunCucumberSpringIntegrationTest {
}
