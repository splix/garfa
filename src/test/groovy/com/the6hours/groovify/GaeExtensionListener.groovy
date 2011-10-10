package com.the6hours.groovify

import org.spockframework.runtime.AbstractRunListener
import com.google.apphosting.api.ApiProxy
import com.google.appengine.tools.development.testing.LocalServiceTestHelper
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig
import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig
import com.google.appengine.tools.development.ApiProxyLocal
import com.google.appengine.tools.development.LocalServerEnvironment
import com.google.appengine.tools.development.ApiProxyLocalFactory
import org.spockframework.runtime.model.SpecInfo
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.spockframework.runtime.model.FeatureInfo

/**
 * Инициализирует среду GAE для теста
 *
 * @since 23.08.11
 * @author Igor Artamonov
 */
class GaeExtensionListener extends AbstractRunListener {

    private static final Logger log = LoggerFactory.getLogger(this)

    LocalServiceTestHelper helper

    @Override
    void beforeSpec(SpecInfo spec) {
        log.info("Init GAE for feature '$spec.name'")
        ApiProxyLocalFactory proxyFactory = new ApiProxyLocalFactory()
        LocalServerEnvironment env = [
                getAppDir: { new File("src/main/webapp") },
                getAddress: { "localhost" },
                getPort: { 8080 },
                waitForServerToStart: {  }
        ] as LocalServerEnvironment
        ApiProxyLocal proxy = proxyFactory.create(env)

        LocalURLFetchServiceTestConfig localURLFetchServiceTestConfig = new LocalURLFetchServiceTestConfig();
        LocalTaskQueueTestConfig taskQueueTestConfig = new LocalTaskQueueTestConfig()
        taskQueueTestConfig.disableAutoTaskExecution = true

        LocalDatastoreServiceTestConfig datastoreService = new LocalDatastoreServiceTestConfig()
        datastoreService.noStorage = true

        helper = new LocalServiceTestHelper(localURLFetchServiceTestConfig, taskQueueTestConfig, datastoreService)

        ApiProxy.setEnvironmentForCurrentThread(new TestEnvironment())
        ApiProxy.setDelegate(proxy)
        log.info("GAE for feature '$spec.name' is initialized")
    }

    @Override
    void beforeFeature(FeatureInfo feature) {
        log.info("Init GAE for feature '$feature.name'")
        helper.setUp()
    }


    @Override
    void afterFeature(FeatureInfo feature) {
        log.info("Cleanup GAE after feature '$feature.name'")
        helper.tearDown()
    }

}
