package com.bmxApp.service;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class EventListenerIntegrator implements Integrator, PostInsertEventListener {

	public final EventListenerIntegrator INSTANCE = new EventListenerIntegrator();

	@Override
	public boolean requiresPostCommitHandling(EntityPersister persister) {
		return false;
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		Integrator.super.integrate(metadata, sessionFactory, serviceRegistry);
	}

}
