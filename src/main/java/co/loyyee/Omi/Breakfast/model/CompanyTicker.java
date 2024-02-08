package co.loyyee.Omi.Breakfast.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record CompanyTicker(
        @Id
        int id,
        @JsonProperty("cik_str")
        String cikStr,
        String ticker,
        String title,
        LocalDateTime created_at,
        @FutureOrPresent
        LocalDateTime updated_at
) {
}
