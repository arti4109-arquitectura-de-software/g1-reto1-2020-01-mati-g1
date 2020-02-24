package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.model.CandidatesRetrieved;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class MatchesManager extends AbstractBehavior<CandidatesRetrieved> {


	public static Behavior<CandidatesRetrieved> create() {
		return Behaviors.setup(MatchesManager::new);
	}

	public MatchesManager(ActorContext<CandidatesRetrieved> context) {

		super(context);
	}

	@Override public Receive<CandidatesRetrieved> createReceive() {

		return null;
	}
}
