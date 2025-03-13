package com.steps;

import com.utilities.Utilities;
import io.restassured.path.xml.XmlPath;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

import static net.serenitybdd.rest.SerenityRest.given;

public class SampleAPISteps {

    @Step
    public void updatePayLoadWithData(String scenarioname, String xmlfilepath) {
        String payload = Utilities.updateXMLPayLoadRequestWithTestData(scenarioname, xmlfilepath);
        Serenity.setSessionVariable("requestpayload").to(payload);
        Serenity.recordReportData().withTitle("Request XML").andContents(payload);
        System.out.println("Payload : \n" + payload);
    }


    @Step
    public void invokeAddService(String soapaction) {

        String payloadwithrequestdata = Serenity.sessionVariableCalled("requestpayload").toString();
        String endpoint = Utilities.getEnvEndPointBasedOnEnvvariable(Utilities.getTestEnvironment());
        endpoint = endpoint + "?op=Add";
        System.out.println("Endpoint : \n" + endpoint);
        HashMap header = new HashMap();
        header.put("Content-Type", "text/xml");
        header.put("charset", "utf-8");
        header.put("SOAPAction", soapaction);
        String reponse = given().request().headers(header).when().body(payloadwithrequestdata).post(endpoint).prettyPrint();
        Serenity.setSessionVariable("response").to(reponse);
        System.out.println("Response : \n" + reponse);

    }

    @Step
    public XmlPath getXMLPath() {
        String responseToValidate = Serenity.sessionVariableCalled("response").toString();
        return new XmlPath(responseToValidate);
    }


    @Step
    public void verifyStatuscode(int code) {
        SerenityRest.then().statusCode(code);
    }

    @Step
    public void verifytheOutputVaue(String expectedvalue) {
        XmlPath xmlPath = getXMLPath();
        xmlPath.setRoot("Envelope.Body.AddResponse");
        String outputvalue = xmlPath.getString("AddResult.text()");
        Serenity.recordReportData().withTitle("Outputvalue").andContents(outputvalue);
        System.out.println("Output : \n" + outputvalue);

    }
}
