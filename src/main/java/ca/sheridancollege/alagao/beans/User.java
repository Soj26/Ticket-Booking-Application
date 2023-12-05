package ca.sheridancollege.alagao.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    private double balance;
    @NonNull
    private Boolean enabled; //properly activater?
    private int purchaseCount;

}
