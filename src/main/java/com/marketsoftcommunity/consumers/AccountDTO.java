package main.java.com.marketsoftcommunity.consumers;

import com.google.gson.Gson;
import main.java.com.marketsoftcommunity.model.Account;
import main.java.com.marketsoftcommunity.model.AccountPermission;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountDTO extends GeneralDTO<Account> {
    private ApiConsumer<AccountPermission> consumer = new ApiConsumer<AccountPermission>();


    public AccountDTO() {
        super("account", Account.class);
    }

    public List<AccountPermission> getPermissions(String user) throws IOException {

        return consumer.getAll("account/permissions/all?username=" + user, AccountPermission.class);
    }

    public void addPermissions(AccountPermission accountPermission) throws Exception {
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("permission", new Gson().toJson(accountPermission)));
        consumer.post(basicNameValuePairs, "account/permissions");
    }

    public void deletePermission(int id) throws IOException {
        consumer.delete(id + "?id=" + id);
    }
}
