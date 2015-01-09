package fr.forfun.croissants.servlet;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import fr.forfun.croissants.service.CycleService;
import fr.forfun.croissants.service.TransverseService;

public class CalculerTousLesCyclesJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		TransverseService transverseService = new TransverseService();
		try {
			CycleService cycleService = new CycleService();
			cycleService.setTransverseService(transverseService);
			cycleService.calculerTousLesCycles();
			
			//Lancement du job de prevention des responsables des tours du lendemain
			JobDetail job = JobBuilder.newJob(PrevenirResponsablesToursJob.class)
				    .withIdentity("prevenirResponsablesToursJob", "group1")
				    .build();
			
			Trigger trigger = TriggerBuilder.newTrigger()
				    .withIdentity("triggerPrevenirResponsablesToursJob", "group1")
				    .startNow()
				    .build();
			
			
			// Tell quartz to schedule the job using our trigger
			context.getScheduler().scheduleJob(job, trigger);
		} catch (Exception e) {
			transverseService = new TransverseService();
			transverseService.tracerErreurTechnique(e);
		}
	}
	
}
