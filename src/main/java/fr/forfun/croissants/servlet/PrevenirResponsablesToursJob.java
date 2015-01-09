package fr.forfun.croissants.servlet;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fr.forfun.croissants.service.CycleService;
import fr.forfun.croissants.service.TransverseService;

public class PrevenirResponsablesToursJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		TransverseService transverseService = new TransverseService();
		try {
			CycleService cycleService = new CycleService();
			cycleService.setTransverseService(transverseService);
			cycleService.prevenirResponsablesTours();
		} catch (Exception e) {
			transverseService.tracerErreurTechnique(e);
		}
	}
	
}
