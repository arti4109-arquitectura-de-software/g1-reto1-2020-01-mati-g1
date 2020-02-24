package co.edu.uniandes.tianguix.dao;

import co.edu.uniandes.tianguix.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class OrderDaoMock implements OrdersDao {

    private final static Logger log = LoggerFactory.getLogger(OrderDaoMock.class);

    @Override
    public void saveOrder(OrderArrived orderArrived){
        log.info("order arrived saved {}", orderArrived.getAsset());
    }

    @Override
    public void saveMatch(Match match){
        log.info("match saved {}", match);
    }

    @Override
    public Collection<OrderArrived> getPurchaseCandidates(PurchaseOrderSaved purchase){
        return buildCandidatesMockCollection(OrderType.PURCHASE, purchase.getAsset());
    }

    @Override
    public Collection<OrderArrived> getSaleCandidates(SaleOrderSaved sale){
        return buildCandidatesMockCollection(OrderType.SALE, sale.getAsset());
    }

    private Collection<OrderArrived> buildCandidatesMockCollection(OrderType orderType, String asset) {
        Random random = new Random();
        List<OrderArrived> ordersArrived = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ordersArrived.add(
                buildOrderArrived(random,orderType, asset)
            );
        }
        return ordersArrived;
    }

    private OrderArrived buildOrderArrived(Random random, OrderType sale, String asset){
        return new OrderArrived()
            .withOrderType(sale)
            .withAssetAmount(random.nextInt())
            .withAsset(asset);
    }
}
