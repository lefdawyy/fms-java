package com.fuzzyDucks.fms.Auth;

import com.fuzzyDucks.fms.Logger.ILogger;
import com.fuzzyDucks.fms.Logger.LoggingHandler;
import com.fuzzyDucks.fms.User.enums.UserFieldName;
import org.bson.Document;

import com.fuzzyDucks.fms.Auth.JWT.JWTService;
import com.fuzzyDucks.fms.Cache.Cache;
import com.fuzzyDucks.fms.User.UserService;
import com.fuzzyDucks.fms.User.UserUtils;

public class AuthService {

    private static final Cache cache = Cache.getInstance();
    private static final JWTService jwtService = new JWTService();
    private static final ILogger logger= LoggingHandler.getInstance();
    private AuthService() {
    }

    public static String getToken(Document user) {
        jwtService.signToken(user);
        return jwtService.getToken();
    }

    public static Boolean validateUser(String username, String password) {
        Document user = UserService.getUser(username);
        if (user != null && UserUtils.checkPassword(password, user.getString(UserFieldName.PASSWORD.getValue()))) {
            logger.logInfo("User: " + username + " validated successfully");
            return true;
        }
        logger.logWarning("User: " + username + " failed to validate credentials");
        throw new IllegalArgumentException("Invalid username or password");
    }

    public static void login(String username, String password) {
        if (validateUser(username, password)) {
            jwtService.signToken(UserService.getUser(username));
            String token = jwtService.getToken();
            cache.put("token", token);
            logger.logInfo("User: " + username + " logged in successfully");
            return;
        }
        logger.logWarning("User: " + username + " failed to log in");
        throw new IllegalArgumentException("Invalid username or password 2");
    }

}
