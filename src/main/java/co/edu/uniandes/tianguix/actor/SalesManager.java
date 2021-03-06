package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.model.CandidatesRetrieved;
import co.edu.uniandes.tianguix.model.SaleCandidatesRetrieved;
import co.edu.uniandes.tianguix.model.SavedSale;

import java.util.Optional;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class SalesManager extends AbstractBehavior<SavedSale> {

	private Optional<ActorRef<CandidatesRetrieved>> matchesActor = Optional.empty();

	private SalesManager(ActorContext<SavedSale> context) {

		super(context);
	}

	public static Behavior<SavedSale> create() {

		return Behaviors.setup(SalesManager::new);
	}

	@Override public Receive<SavedSale> createReceive() {

		return newReceiveBuilder()
				.onMessage(SavedSale.class, this::onArrivedSaveOrder)
				.build();
	}

	private Behavior<SavedSale> onArrivedSaveOrder(SavedSale savedSale) {

		var candidates = new OrderDaoMock().getSaleCandidates(savedSale);

		var candidatesRetrieved = new SaleCandidatesRetrieved()
				.withSaleCandidates(candidates)
				.withPurchase(savedSale.getArrivedOrder());

		var replayTo = matchesActor.orElseGet(() -> getContext().spawn(MatchesManager.create(), "matchesManager"));
		replayTo.tell(candidatesRetrieved);
		matchesActor = Optional.of(replayTo);

		return Behaviors.same();
	}

}
