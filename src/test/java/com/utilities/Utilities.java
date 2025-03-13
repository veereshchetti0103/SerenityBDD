package com.utilities;

import com.csvreader.CsvReader;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.junit.Assert;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Utilities {

    public static final Hashtable<String, String> DICT_TO_READ = new Hashtable<String, String>();

    public static Hashtable getData(String scenarioName) {
        try {

            if (!DICT_TO_READ.isEmpty()) {
                DICT_TO_READ.clear();
            }


            String testEnv = getTestEnvironment();
            String testDataPath = "";

            switch (testEnv.toLowerCase()) {
                case "test":
                    testDataPath = "src/test/resources/inputdata/testInputDatasheet.csv";
                    break;
                default:
                    Assert.fail("Environment value passed is not correct :" + testEnv.toLowerCase());
            }

            int blnTestDataFlag = 0;

/*

Code to read data from yam file

            if (testDataPath.contains("yaml")) {
                try (InputStream in = new FileInputStream(new File(testDataPath))) {
                    Yaml yaml = new Yaml();
                    Iterable<Object> itr = yaml.loadAll(in);
                    String value;
                    for (Object o : itr) {
                        Map<String, String> map = (Map<String, String>) o;

                        for (Map.Entry<String, String> e : map.entrySet()) {

                            if (e.getKey().equalsIgnoreCase(field)) {
                                value = String.valueOf(e.getValue());
                                switch (value) {
                                    case "true":
                                        DICT_TO_READ.put(field, "Yes");
                                        break;
                                    case "false":
                                        DICT_TO_READ.put(field, "No");
                                        break;
                                    default:
                                        DICT_TO_READ.put(field, String.valueOf(e.getValue()));
                                        break;
                                }
                                break;
                            }

                        }


                    }

                }


            }
*/
            CsvReader csvReaderObj = new CsvReader(testDataPath);
            csvReaderObj.readHeaders();

            while (csvReaderObj.readRecord()) {
                String scenarioNameCSSheet = csvReaderObj.get("TestcaseName").trim();
                String scenarioInstanceCSSheet = csvReaderObj.get("Testcaseinstance").trim();

                if ((scenarioName.equalsIgnoreCase(scenarioNameCSSheet)) && (1 == Integer.parseInt(scenarioInstanceCSSheet))) {

                    for (int i = 1; i < csvReaderObj.getColumnCount() / 2 + 1; i++) {

                        String field = csvReaderObj.get("Field" + i).trim();
                        String value = csvReaderObj.get("Value" + i).trim();

                        if (field.equalsIgnoreCase("")) {
                            break;
                        }

                        DICT_TO_READ.put(field, value);

                    }
                    blnTestDataFlag = 0;
                    break;

                } else {
                    blnTestDataFlag = 1;

                }
            }
            if (blnTestDataFlag == 1) {
                Assert.fail("No data present for testcase" + scenarioName);
            }
        } catch (FileNotFoundException e) {
            Assert.fail("Test Data file not found");
        } catch (IOException e) {
            throw new
                    RuntimeException(e.getMessage());
        }
        return DICT_TO_READ;
    }


    public static String updateXMLPayLoadRequestWithTestData(String scenarioName, String xmlPayloadTextFileName) {
        String content = "";
        try {
            Path paths = Paths.get("src/test/resources/payload/" + xmlPayloadTextFileName + ".txt");
            Charset charset = StandardCharsets.UTF_8;
            content = new String(Files.readAllBytes(paths), charset);
            Hashtable table = getData(scenarioName);
            Set<String> keys = table.keySet();
            for (String key : keys) {

                content = content.replace("{{" + key + "}}", table.get(key).toString());
            }


        } catch (Exception e) {
            Assert.fail("Unable to parse xml with the test data" + e.getMessage());

        }
        return content;
    }


    public static String getDataForField(String scenarioName, String fieldName) {
        Hashtable table = getData(scenarioName);
        return (String) table.get(fieldName);
    }

    public static String getTestEnvironment() {
        String testEnv;
        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
        testEnv = variables.getProperty("endpoint.test.env");
        return testEnv;
    }


    public static String getEnvEndPointBasedOnEnvvariable(String environment) {

        EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();

        String testURLOBP = "";

        switch (environment.toLowerCase()) {
            case "test":
                testURLOBP = variables.getProperty("endpoint.test.url");
                break;
            default:
                Assert.fail("Environment value passedd in properties file is not correct" + environment.toLowerCase());
        }
        return testURLOBP;

    }

}
