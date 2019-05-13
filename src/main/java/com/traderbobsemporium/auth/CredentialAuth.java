package main.java.com.traderbobsemporium.auth;
import javafx.scene.control.Alert;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;
import javax.security.auth.login.AccountLockedException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CredentialAuth {

    private Subject subject = SecurityUtils.getSubject();

    protected boolean credentialsAreValid(String username, String password) {
        try {
            subject.login(new UsernamePasswordToken(username, password));
            authSubjectRole();
            return true;
        } catch (UnknownAccountException uae) {
            Util.displayError("Account does not exist!", Alert.AlertType.ERROR);
        } catch (IncorrectCredentialsException ice) {
            Util.displayError("Username or password is incorrect!", Alert.AlertType.ERROR);
        } catch (ConcurrentAccessException cae){
            Util.displayError("Account already authenticated!", Alert.AlertType.ERROR);
        } catch (AccountLockedException e) {
            Util.displayError("Account is locked and you cannot login! Please notify an administrator to " +
                    "assign you your designated account role and permissions and/or to unlock your account.", Alert.AlertType.ERROR);
        } catch (DisabledAccountException e) {
            Util.displayError("This account is disabled. Please register with a new account. If there is an " +
                    "issue related to your account being disabled please notify an administrator" , Alert.AlertType.ERROR);
        } catch (AuthenticationException ae) {
            new ExceptionDialog(ae).showAndWait();
        }
        subject.logout();
        return false;
    }

    private void authSubjectRole() throws AccountLockedException {
        if (subject.hasRole(AccountRole.UNCONFIRMED.name()))
            throw new AccountLockedException();
        if (subject.hasRole(AccountRole.DISABLED.name()))
            throw new DisabledAccountException();
    }
}
