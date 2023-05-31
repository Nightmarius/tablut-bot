package ch.zuehlke.challenge.bot.client;

import ch.zuehlke.challenge.bot.util.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RestTemplateConfigurationTest {

    private ApplicationProperties applicationPropertiesMock;
    private RestTemplateConfiguration restTemplateConfiguration;

    @BeforeEach
    void setUp() {
        applicationPropertiesMock = mock(ApplicationProperties.class);
        restTemplateConfiguration = new RestTemplateConfiguration(applicationPropertiesMock);
    }

    @Test
    void restTemplate_withValidInput_isBuiltSuccessfully() throws URISyntaxException {
        when(applicationPropertiesMock.getBackendRootUri()).thenReturn("http://localhost:8080");
        when(applicationPropertiesMock.getToken()).thenReturn("1111");

        RestTemplate restTemplate = restTemplateConfiguration.restTemplate();

        assertThat(restTemplate).isNotNull();
        assertThat(restTemplate.getUriTemplateHandler().expand("/")).isEqualTo(new URI("http://localhost:8080/"));
        verify(applicationPropertiesMock, times(1)).getBackendRootUri();
        verify(applicationPropertiesMock, times(1)).getToken();
        verifyNoMoreInteractions(applicationPropertiesMock);
    }
}
