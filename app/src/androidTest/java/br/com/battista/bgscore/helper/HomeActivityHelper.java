package br.com.battista.bgscore.helper;

import br.com.battista.bgscore.model.User;

import static br.com.battista.bgscore.helper.TestConstant.DATA_USER_TEST_MAIL;
import static br.com.battista.bgscore.helper.TestConstant.DATA_USER_TEST_USERNAME;

public class HomeActivityHelper {

    private HomeActivityHelper() {
    }

    public static User createNewUser() {
        User user = new User()
                .username(DATA_USER_TEST_USERNAME)
                .mail(DATA_USER_TEST_MAIL)
                .lastBuildVersion(0);
        user.initEntity();
        user.welcome(Boolean.TRUE);
        return user;
    }

}
