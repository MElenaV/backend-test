package homeWork4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "id"
})

@Data
public class AddToShoppingListResponse {
  @JsonProperty("status")
  public String status;
  @JsonProperty("id")
  public Integer id;
}
