package ru.virgil.spring_tools.tools.security

import com.hazelcast.config.*
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.MapSession
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository
import org.springframework.session.hazelcast.HazelcastSessionSerializer
import org.springframework.session.hazelcast.PrincipalNameExtractor
import org.springframework.session.hazelcast.config.annotation.SpringSessionHazelcastInstance
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession
import org.springframework.session.web.http.HeaderHttpSessionIdResolver
import org.springframework.session.web.http.HttpSessionIdResolver


/**
 * Source: [https://docs.hazelcast.com/tutorials/spring-session-hazelcast](https://docs.hazelcast.com/tutorials/spring-session-hazelcast)
 */
@Configuration
@EnableHazelcastHttpSession
//@EnableConfigurationProperties(SecurityProperties::class)
class SessionsConfig(private val securityProperties: SecurityProperties) {

    @Bean
    fun httpSessionIdResolver(): HttpSessionIdResolver? =
        if (securityProperties.useXAuthToken) HeaderHttpSessionIdResolver.xAuthToken() else null

    @Bean
    @SpringSessionHazelcastInstance
    fun hazelcastInstance(): HazelcastInstance {
        val config = Config()
        config.clusterName = "spring-session-cluster"

        // Add this attribute to be able to query sessions by their PRINCIPAL_NAME_ATTRIBUTE's
        val attributeConfig = AttributeConfig()
            .setName(HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
            .setExtractorClassName(PrincipalNameExtractor::class.java.name)

        // Configure the sessions map
        config.getMapConfig(HazelcastIndexedSessionRepository.DEFAULT_SESSION_MAP_NAME)
            .addAttributeConfig(attributeConfig).addIndexConfig(
                IndexConfig(IndexType.HASH, HazelcastIndexedSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
            )

        // Use custom serializer to de/serialize sessions faster. This is optional.
        val serializerConfig = SerializerConfig()
        serializerConfig.setImplementation(HazelcastSessionSerializer()).typeClass = MapSession::class.java
        config.serializationConfig.addSerializerConfig(serializerConfig)
        return Hazelcast.newHazelcastInstance(config)
    }

}
