package co.loyyee.Omi.Drafter.service.springai;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Testing for SpringAI get/post/put endpoints
 * <br>
 * TODO:
 * 1. Upload method is correct
 * 2. test upload file type
 * 3. test for null RequestParam
 * 4. test for empty RequestParam
 * 5. Throw correct exception and message.
 * */
@SpringBootTest
class SpringOpenAiServiceTest {
	
	private TestRestTemplate restTemplate;
	
	@Test
	void shouldAcceptPdf() {
		ResponseEntity<String> resp = restTemplate.getForEntity("/drafter/upload/pdf", String.class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
	}
}
