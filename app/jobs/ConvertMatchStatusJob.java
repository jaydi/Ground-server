package jobs;

import javax.persistence.Query;

import models.MatchEntity.MatchStatus;
import play.db.jpa.JPA;
import play.jobs.Every;
import play.jobs.Job;

@Every("1mn")
public class ConvertMatchStatusJob extends Job {
	public void doJob() {
		Query query = JPA.em().createQuery("UPDATE MatchEntity SET status = ? WHERE status = ? AND endTime < now()");
		query.setParameter(1, MatchStatus.READY_SCORE.value());
		query.setParameter(2, MatchStatus.MATCHING_COMPLETED.value());
		query.executeUpdate();

		query = JPA.em().createQuery("DELETE MatchEntity WHERE status < ? and endTime < now()");
		query.setParameter(1, MatchStatus.MATCHING_COMPLETED.value());
		query.executeUpdate();
	}
}
