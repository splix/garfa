package com.the6hours.groovify

import com.google.apphosting.api.ApiProxy

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 10.10.11
 */
class TestEnvironment implements ApiProxy.Environment {

    String getAppId() {
        return 'Groovify Test'
    }

    String getVersionId() {
        return '1.0'
    }

    String getEmail() {
        return 'test@localhost'
    }

    boolean isLoggedIn() {
        return false
    }

    boolean isAdmin() {
        return false
    }

    String getAuthDomain() {
        return null
    }

    String getRequestNamespace() {
        return null
    }

    Map<String, Object> getAttributes() {
        return [:]
    }
}
