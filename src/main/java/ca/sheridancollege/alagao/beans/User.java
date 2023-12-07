package ca.sheridancollege.alagao.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    private Long userID;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String encryptedPassword; // so will be encrypted if it is 1234 password
    @NonNull
    private BigDecimal balance;
    @NonNull
    private Boolean enabled; //properly activater?
    private int purchaseCount;

}
