package com.bmxApp.service.database;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.listener.InsertEventListener;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

@Service
public class TransactionService {

	@Autowired
	InsertEventListener insertEventListener;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@PostConstruct
	private void init() {
		SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
		EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(insertEventListener);
	}
}
