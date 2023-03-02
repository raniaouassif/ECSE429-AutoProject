package BDDTest;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class IsItFriday {
    static String isItFriday(String today) {
        return "Nope";
    }
}
public class StepDefinitions {
    private String today;
    private String actualAnswer;
    @Given("an example scenario")
    public void anExampleScenario() {
    }

    @When("all step definitions are implemented")
    public void allStepDefinitionsAreImplemented() {
    }

    @Then("the scenario passes")
    public void theScenarioPasses() {
    }

    @Given("today is Sunday")
    public void today_is_Sunday() {
        today = "Sunday";
    }

    @Given("today is Friday")
    public void today_is_Friday() {
        today = "Friday";
    }

    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_Friday_yet() {
        actualAnswer = IsItFriday.isItFriday(today);
    }

    @Then("I should be told {string}")
    public void i_should_be_told(String expectedAnswer) {
        assertEquals(expectedAnswer, actualAnswer);
    }


}
