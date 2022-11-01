package com.bmxApp.listener;

import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Component
public class InsertEventListener implements PostInsertEventListener {

	@Override
	public boolean requiresPostCommitHandling(EntityPersister persister) {
		return false;
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
	    System.out.println("The Event comes here with data");
	}

}
