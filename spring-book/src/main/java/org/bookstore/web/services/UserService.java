package org.bookstore.web.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bookstore.web.models.AuthErrors;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bookstore.common.patterns.AuthPatterns.*;

@Slf4j
@Service
public class UserService {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final List<String> INVALID_DOMAINS = Arrays.asList("test.com");


    public Optional<AuthErrors> getAuthError(String email, char[] pwd) {
        log.debug("Start validate user info {}", email);
        List<String> errors = new ArrayList<>();

        isEmailValid(email).ifPresent(errors::add);

        if (ArrayUtils.isEmpty(pwd)) {
            errors.add(USER_PASSWORD_IS_NOT_PROVIDED);
        }

        return errors.isEmpty() ?
                Optional.empty() :
                Optional.of(new AuthErrors(email, pwd, errors));
    }

    private Optional<String> isEmailValid(String email) {
        if (StringUtils.isEmpty(email)) {
            return Optional.of(USER_EMAIL_IS_NOT_PROVIDED);
        }


        String lwEmail = email.toLowerCase();
        Optional<String> invalidDomain = INVALID_DOMAINS.stream()
                .filter(lwEmail::contains)
                .map(d -> INVALID_DOMAIN + d)
                .findFirst();
        if (invalidDomain.isPresent()) {
            return invalidDomain;
        }

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(lwEmail);
        if (!matcher.find()) {
            return Optional.of(INVALID_EMAIL_FORMAT + email);
        }

        return Optional.empty();
    }
}
