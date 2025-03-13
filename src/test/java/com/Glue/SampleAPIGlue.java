package com.Glue;

import com.steps.SampleAPISteps;
import com.utilities.Utilities;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Steps;

public class SampleAPIGlue {

    @Steps
    SampleAPISteps sampleAPISteps;

    @Given("^the payload request for the add service \"([^\"]*)\",updated with the data \"([^\"]*)\"$")
    public void thePayloadRequestForTheAddServiceUpdatedWithTheData(String scenarioname, String xmlfilepath) throws Throwable {
        sampleAPISteps.updatePayLoadWithData(scenarioname,xmlfilepath);
    }

    @When("^user invoke the add service with \"([^\"]*)\"$")
    public void userInvokeTheAddServiceWith(String soapaction)  {

        sampleAPISteps.invokeAddService(soapaction);


    }

    @Then("^status description should be \"([^\"]*)\"$")
    public void statusDescriptionShouldBe(int code)  {
        sampleAPISteps.verifyStatuscode(code);
    }

    @Then("^the reponse recieved has the value \"([^\"]*)\"$")
    public void theReponseRecievedHasTheValue(String expectedvalue) throws Throwable {
        sampleAPISteps.verifytheOutputVaue(expectedvalue);

    }
}
