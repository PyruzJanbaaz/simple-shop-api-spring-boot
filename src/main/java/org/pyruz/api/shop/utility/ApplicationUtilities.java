package org.pyruz.api.shop.utility;

import org.pyruz.api.shop.configuration.ApplicationProperties;
import org.pyruz.api.shop.model.entity.Contact;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class ApplicationUtilities {

    static ApplicationProperties applicationProperties;

    public static ApplicationUtilities getInstance(ApplicationProperties applicationProperties) {
        ApplicationUtilities.applicationProperties = applicationProperties;
        return new ApplicationUtilities();
    }

    //-> Get current request
    public HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    // -> Generate pin code for send by SMS(Verification of mobile number)
    public Contact generatePinCode(Contact contact) {
        Calendar calendar = Calendar.getInstance();
        Date currentDateTime = calendar.getTime();
        Random random = new Random(System.currentTimeMillis());
        int expirationMinutes = applicationProperties.getCode("activation.code.expiration.time");
        int activationCode;
        calendar.add(Calendar.MINUTE, expirationMinutes);
        if (contact.getExpirationPinCode() == null || currentDateTime.after(contact.getExpirationPinCode())) {
            activationCode = applicationProperties.getCode("activation.code.min.rang") + random.nextInt(applicationProperties.getCode("activation.code.max.rang") - applicationProperties.getCode("activation.code.min.rang") + 1);
            while (activationCode > applicationProperties.getCode("activation.code.max.rang")) {
                activationCode = applicationProperties.getCode("activation.code.min.rang") + random.nextInt(applicationProperties.getCode("activation.code.max.rang"));
            }
            contact.setTempPinCode(activationCode);
            contact.setExpirationPinCode(calendar.getTime());
        }
        return contact;
    }
}
