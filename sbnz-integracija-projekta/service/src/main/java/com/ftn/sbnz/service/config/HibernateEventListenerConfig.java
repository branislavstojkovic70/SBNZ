package com.ftn.sbnz.service.config;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ftn.sbnz.service.eventlisteners.CustomHibernateEventListener;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Configuration
public class HibernateEventListenerConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CustomHibernateEventListener customHibernateEventListener;

    @PostConstruct
    public void registerListeners() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(customHibernateEventListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(customHibernateEventListener);
    }
}

