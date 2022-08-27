package com.example.producingwebservice;

import io.spring.guides.gs_producing_web_service.Country;
import io.spring.guides.gs_producing_web_service.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.*;

@WebServiceServerTest
class ProducingWebServiceApplicationTests {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @Autowired
    private MockWebServiceClient client;

    @MockBean
    private CountryRepository countryRepository;

    @Test
    void givenXmlRequest_whenServiceInvoked_thenValidResponse() throws IOException {
        Country country = new Country();
        country.setName("Spain");
        country.setCapital("Madrid");
        country.setCurrency(Currency.EUR);
        country.setPopulation(46704314);
        when(countryRepository.findCountry("Spain")).thenReturn(country);


        // В запросе нужно убрать SOAP обертку и первым  должен идти сам элемент
        StringSource request = new StringSource(
                        "<gs:getCountryRequest xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\">" +
                        "<gs:name>Spain</gs:name>" +
                        "</gs:getCountryRequest>"
        );

        StringSource expectedResponse = new StringSource(
                        "<ns2:getCountryResponse xmlns:ns2=\"http://spring.io/guides/gs-producing-web-service\">" +
                        "<ns2:country>" +
                        "<ns2:name>Spain</ns2:name>" +
                        "<ns2:population>46704314</ns2:population>" +
                        "<ns2:capital>Madrid</ns2:capital>" +
                        "<ns2:currency>EUR</ns2:currency>" +
                        "</ns2:country></ns2:getCountryResponse>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(validPayload(new ClassPathResource("countries.xsd")))
                .andExpect(payload(expectedResponse));
    }


    @Test
    void with_void_name_in_request() {

        StringSource request = new StringSource(
                "<gs:getCountryRequest xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\">" +
                        "<gs:name></gs:name>" +
                        "</gs:getCountryRequest>"
        );

        client.sendRequest(withPayload(request))
                .andExpect(serverOrReceiverFault());
    }

}
