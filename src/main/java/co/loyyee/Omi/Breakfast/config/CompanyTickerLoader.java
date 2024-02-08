package co.loyyee.Omi.Breakfast.config;

import co.loyyee.Omi.Breakfast.model.CompanyTicker;
import co.loyyee.Omi.Breakfast.repository.CompanyTickerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * Loading all the SEC provided publicly traded company
 * CIK - the id of the company
 * Ticker - ticker in the market
 * Title - title of the company
 *
 * Opt out by commenting out the @Component annotation
 */
//@Component
public class CompanyTickerLoader implements CommandLineRunner {
    final private Logger log = LoggerFactory.getLogger(CompanyTickerLoader.class);
    final private ObjectMapper mapper;
    final private CompanyTickerRepository repository;


    public CompanyTickerLoader(ObjectMapper mapper, CompanyTickerRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    /**
     * Callback used to run the bean.
     * Converting JSON to CompanyTicker and saving to the database
     * TypeReference is crucial for Class Cast, without it, it will throw ClassCastException
     *
     * @see TypeReference
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/company_tickers.json")) {
           Map<String, CompanyTicker> companyTickerMap = mapper.readValue(inputStream, new TypeReference<Map<String, CompanyTicker>>() {
           }) ;
           List<CompanyTicker> tickers = companyTickerMap.values().stream().collect(Collectors.toList());
           repository.saveAll(tickers);
        } catch (Exception e) {
            log.error("Failed to load company tickers", e);
        }

    }
}
