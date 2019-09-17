package br.com.battista.bgscore.helper

import br.com.battista.bgscore.helper.TestConstant.DATA_USER_TEST_MAIL
import br.com.battista.bgscore.helper.TestConstant.DATA_USER_TEST_USERNAME

import br.com.battista.bgscore.model.User

object HomeActivityHelper {

    fun createNewUser(): User {
        val user = User()
                .username(DATA_USER_TEST_USERNAME)
                .mail(DATA_USER_TEST_MAIL)
                .lastBuildVersion(0)
        user.initEntity()
        user.welcome(true)
        return user
    }

}
