package de.biniasonline.employeemgmt.service;

import javax.persistence.EntityManager;

import de.biniasonline.employeemgmt.model.Skill;

public class StatisticService {
	
	private EntityManager _entityManager;

	public StatisticService(EntityManager entityManager) {
		_entityManager = entityManager;
	}
	
	public float getKnowledge(Skill skill) {
		_entityManager.clear();
		long employeeCount = _entityManager.createQuery("SELECT count(employee) FROM Employee employee", Long.class).getSingleResult();	
		if (employeeCount > 0) {
			return ((float)skill.getEmployees().size()) / employeeCount * 100;
		} else {
			return 0;
		}
	}

}
