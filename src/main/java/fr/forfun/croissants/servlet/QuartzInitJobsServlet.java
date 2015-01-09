package fr.forfun.croissants.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.impl.StdSchedulerFactory;

import fr.forfun.croissants.service.TransverseService;


/**
 * Servlet implementation class QuartzInitJobsServlet
 */
public class QuartzInitJobsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuartzInitJobsServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
	        ServletContext servletContext = config.getServletContext();
	        StdSchedulerFactory factory = (StdSchedulerFactory) servletContext.getAttribute(QuartzInitializerListener.QUARTZ_FACTORY_KEY);
			Scheduler quartzScheduler = factory.getScheduler("MyQuartzScheduler");
			
			// define the job and tie it to our HelloJob class
			JobDetail job = JobBuilder.newJob(CalculerTousLesCyclesJob.class)
			    .withIdentity("calculerTousLesCyclesJob", "group1")
			    .build();

			//Lancement du job tous les jours a 14h
			CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("0 00 14 * * ?");
			Trigger trigger = TriggerBuilder.newTrigger()
			    .withIdentity("triggerCalculerTousLesCyclesJob", "group1")
			    .startNow()
			    .withSchedule(cronSchedule)
			    .build();

			// Tell quartz to schedule the job using our trigger
			quartzScheduler.scheduleJob(job, trigger);
			
		} catch (Exception e) {
			TransverseService transverseService = new TransverseService();
			transverseService.tracerErreurTechnique(e);
		}
	}

}
