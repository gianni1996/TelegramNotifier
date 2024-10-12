package com.trello_talk.trello_talk.util;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class IpTracker {

    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
